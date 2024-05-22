package com.elliegabel.s.properties;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Loads properties from file.
 */
public final class PropertiesLoader {

    @NotNull
    public static Properties loadPropertiesFromFile(@NotNull String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Properties properties = new Properties();

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            properties.load(reader);
        }

        return properties;
    }
}