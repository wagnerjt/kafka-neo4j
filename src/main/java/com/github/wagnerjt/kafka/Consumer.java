package com.github.wagnerjt.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class Consumer {
    private static final String KAFKA_ADDRESS = "127.0.0.1:9092"; // localhost:9092
    private static final String CONSUMER_GROUP = "some_consumer_group_name";
    private static final Collection TOPICS = Collections.singleton("some_topic_name");

    public static void main(String[] args) {
        new Consumer().run();
    }

    public void run() {
        // Create new Log instance
        Logger logger = LoggerFactory.getLogger(ConsumerThread.class.getName());

        CountDownLatch countDownLatch = new CountDownLatch(1);

        logger.info("Creating Consumer Thread...");
        Runnable myConsumer = new ConsumerThread(countDownLatch, TOPICS, CONSUMER_GROUP);

        // Start the kafka thread
        Thread myThread = new Thread(myConsumer);
        myThread.start();

        // add a shutdown hook to close gracefully
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Caught shutdown hook");
            ((ConsumerThread) myConsumer).shutdown();
        }));

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            logger.error("Application got interrupted", e);
        } finally {
            logger.info("Application is closing");
        }
    }

    /**
     * Background thread for polling kafka,
     *  providing the latch for closing connections,
     *  deserializing JSON payloads
     */
    public class ConsumerThread implements Runnable {
        private CountDownLatch latch;
        private KafkaConsumer<String, String> consumer;
        private Logger logger;
        private Collection topics;
        private String consumerGroup;

        public ConsumerThread(CountDownLatch latch, Collection topics, String consumerGroup) {
            this.latch = latch;
            this.topics = topics;
            this.consumerGroup = consumerGroup;

            // Create new Log instance
            this.logger = LoggerFactory.getLogger(ConsumerThread.class.getName());

            // create Consumer properties
            Properties properties = new Properties();
            properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_ADDRESS);
            properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
            properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

            // create Consumer with the desired properties
            this.consumer = new KafkaConsumer<>(properties);

            // subscribe consumer to the topic(s), note the topic can be a singleton
            consumer.subscribe(topics);
        }

        @Override
        public void run() {
            // poll for new data
            try {
                while(true) {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                    // for each new record from kafka, log the info
                    for(ConsumerRecord record : records) {
                        logger.info("Key: " + record.key() + " , Value: " + record.value());
                        logger.info("Partition: " + record.partition());
                    }
                }
            } catch(WakeupException ex) {
                logger.info("Received shutdown signal");
            } finally {
                // close the consumer
                consumer.close();
                // tell the main code that we are finished with consumption
                latch.countDown();
                try {
                    latch.await();
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
                logger.info("Application has exited");
            }
        }

        public void shutdown() {
            // interrupts the consumer.poll(), by throwing WakeUpException
            consumer.wakeup();
        }
    }
}
