package ru.otus.dataprocessor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.otus.model.Measurement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class ResourcesFileLoader implements Loader {

    private final String fileName;
    private final Gson gson;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
        this.gson = new Gson();
    }

    @Override
    public List<Measurement> load() {
        try {
            return gson.fromJson(readFile(fileName), new TypeToken<List<Measurement>>() {
            }.getType());
        } catch (Exception e) {
            throw new FileProcessException("Read file exception");
        }
    }

    private String readFile(String fileName) throws IOException {
        try (var reader = new BufferedReader(new InputStreamReader(readResourceFile(fileName)))) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }

    private InputStream readResourceFile(String fileName) {
        return ResourcesFileLoader.class.getClassLoader().getResourceAsStream(fileName);
    }
}
