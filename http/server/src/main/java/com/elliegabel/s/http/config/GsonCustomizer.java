package com.elliegabel.s.http.config;

import com.google.gson.Gson;
import io.javalin.config.JavalinConfig;
import io.javalin.json.JsonMapper;
import io.javalin.plugin.Plugin;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.Type;
import java.util.function.Consumer;

@Singleton
public class GsonCustomizer extends Plugin<Void> {

    private final Gson gson;

    @Inject
    public GsonCustomizer(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void onInitialize(@NotNull JavalinConfig config) {
        config.jsonMapper(new JsonMapper() {
            @NotNull
            @Override
            public <T> T fromJsonString(@NotNull String json, @NotNull Type targetType) {
                return gson.fromJson(json, targetType);
            }

            @NotNull
            @Override
            public String toJsonString(@NotNull Object obj, @NotNull Type type) {
                return gson.toJson(obj, type);
            }
            @NotNull
            public <T> T fromJsonStream(@NotNull InputStream json, @NotNull Type targetType) {
                try (InputStreamReader reader = new InputStreamReader(json)) {
                    return gson.fromJson(reader, targetType);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to read JSON from InputStream", e);
                }
            }
        });
    }
}
