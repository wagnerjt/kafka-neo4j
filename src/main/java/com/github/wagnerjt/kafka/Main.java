package com.github.wagnerjt.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wagnerjt.kafka.com.github.wagnerjt.kafka.models.Data;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String filePath = "fake/data.json";

        Data fakedData = parse(filePath);

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
}
