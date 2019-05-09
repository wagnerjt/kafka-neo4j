package com.github.wagnerjt.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoKeys {
    private static final String KAFKA_ADDRESS = "127.0.0.1:9092";
    private static final String TOPIC_KEY = "id_";
    private static final String TOPIC_TARGET = "first_topic";

    public static void main(String[] args) {
        // Create new Log instance
        Logger logger = LoggerFactory.getLogger(ProducerDemoKeys.class);

        // create Producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_ADDRESS);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // create Producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);



        for(int i = 0; i < 10; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>(
                    TOPIC_TARGET, TOPIC_KEY + Integer.toString(i),
                    "hello world! with callback" + Integer.toString(i));

            // send data, async
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

        producer.flush();
        producer.close();
    }
}
