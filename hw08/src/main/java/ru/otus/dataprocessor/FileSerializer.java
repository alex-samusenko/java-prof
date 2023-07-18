package ru.otus.dataprocessor;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class FileSerializer implements Serializer {
    private final String fileName;
    private final Gson gson;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
        gson = new Gson();
    }

    @Override
    public void serialize(Map<String, Double> data) {
        writeToFile(fileName, gson.toJson(new TreeMap<>(data)));
    }

    private void writeToFile(String fileName, String data) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
            bufferedWriter.write(data);
        } catch (IOException e) {
            throw new FileProcessException("Error write file");
        }
    }
}
