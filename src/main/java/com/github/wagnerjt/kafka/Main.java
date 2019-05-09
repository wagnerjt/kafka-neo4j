package com.github.wagnerjt.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wagnerjt.kafka.com.github.wagnerjt.kafka.models.Data;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        String filePath = "fake/data.json";

        Data fakedData = parse(filePath);


        // if you want to send some fake data
        produce(fakedData);
    }

    private static Data parse(String path) {
        ObjectMapper mapper = new ObjectMapper();

        ClassLoader classLoader = new Main().getClass().getClassLoader();
        File file = new File(classLoader.getResource(path).getFile());

        Data fakeData = null;
        try {
            fakeData = mapper.readValue(file, Data.class);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return fakeData;
    }

    private static void produce(Data fakeData) {
        // create Producer configuration
        final String KAFKA_ADDRESS = "127.0.0.1:9092";

        Properties configuration = new Properties();
        configuration.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_ADDRESS);
        configuration.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configuration.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        Producer producer = Producer.create(fakeData, configuration);
        producer.pubAuthors();
        producer.pubPosts();
        producer.close();
    }
}
