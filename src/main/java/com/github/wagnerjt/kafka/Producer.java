package com.github.wagnerjt.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wagnerjt.kafka.com.github.wagnerjt.kafka.models.Author;
import com.github.wagnerjt.kafka.com.github.wagnerjt.kafka.models.Data;
import com.github.wagnerjt.kafka.com.github.wagnerjt.kafka.models.Post;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class Producer {
    private static final String KAFKA_ADDRESS = "127.0.0.1:9092";
    private static final String TOPIC_KEY_AUTHOR = "id_";
    private static final String TOPIC_TARGET_AUTHOR = "author_topic";

    private static final String TOPIC_TARGET_POST = "post_topic";

    private Data data;
    private Properties kafkaConfig;
    private KafkaProducer<String, String> producer;
    private final Logger logger;

    public Producer(Data data, Properties kafkaConfig) {
        this.data = data;
        this.kafkaConfig = kafkaConfig;

        // Create new Log instance
        this.logger = LoggerFactory.getLogger(Producer.class);

        // create Producer
        this.producer = new KafkaProducer<>(kafkaConfig);
    }

    public void pubPosts() {
        ObjectMapper mapper = new ObjectMapper();

        for(Post post : data.getPosts()) {
            ProducerRecord<String, String> record = null;

            // try to create a new producer record with the JSON form of the author, no key on the topic. no reason to for this example
            try {
                record = new ProducerRecord<>(TOPIC_TARGET_POST, mapper.writeValueAsString(post));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            // send data to kafka with a callback on partition, topic, and offset, async
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    // if data was successfully sent, we want to log it.
                    if(exception == null) {
                        logger.info("Received new metadata: \n"
                                + "Topic: " + metadata.topic() + "\n"
                                + "Partition: " + metadata.partition() + "\n"
                                + "Offset: " + metadata.offset() + "\n"
                                + "Timestamp" + metadata.timestamp() + "\n");
                    }
                    else {
                        logger.error("Error while producing", exception);
                    }
                }
            });
        }
    }

    public void pubAuthors() {
        ObjectMapper mapper = new ObjectMapper();

        for(Author author : data.getAuthors()) {
            ProducerRecord<String, String> record = null;

            // try to create a new producer record with the JSON form of the author
            try {
                record = new ProducerRecord<>(
                        TOPIC_TARGET_AUTHOR, TOPIC_KEY_AUTHOR + author.getId(), mapper.writeValueAsString(author));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            // send data to kafka with a callback on partition, topic, and offset, async
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    // if data was successfully sent, we want to log it.
                    if(exception == null) {
                        logger.info("Received new metadata: \n"
                                + "Topic: " + metadata.topic() + "\n"
                                + "Partition: " + metadata.partition() + "\n"
                                + "Offset: " + metadata.offset() + "\n"
                                + "Timestamp" + metadata.timestamp() + "\n");
                    }
                    else {
                        logger.error("Error while producing", exception);
                    }
                }
            });
        }
    }

    /**
     * Close any last bits of data and the connection to Kafka
     */
    public void close() {
        if(this.producer != null) {
            producer.flush();
            producer.close();
        }
    }

    public static Producer create(Data data, Properties kafkaConfig) {
        return new Producer(data, kafkaConfig);
    }

    public static void main(String[] args) {
        // create Producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_ADDRESS);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());




    }
}
