package ru.smyvin.exchangedemo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.smyvin.exchangedemo.service.AccountStorage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileWriter {

    @Value("${data.result}")
    private String resultPath;

    @Autowired
    private AccountStorage accountStorage;

    public void write() throws IOException {
        Files.write(getPath(), accountStorage.toFile(), StandardCharsets.UTF_8);
    }

    private Path getPath() throws IOException {
        Path resultFile = Paths.get(resultPath);
        try {
            Files.createFile(resultFile);
        } catch (FileAlreadyExistsException e) {
            //skip
        }
        return resultFile;
    }
}
