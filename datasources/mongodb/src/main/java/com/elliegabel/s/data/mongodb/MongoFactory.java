package com.elliegabel.s.data.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.MongoClientSettings;
import io.avaje.config.Config;
import io.avaje.inject.Bean;
import io.avaje.inject.Factory;
import org.jetbrains.annotations.NotNull;

@Factory
public class MongoFactory {

    @Bean
    MongoClient mongoClient() {
        // Build connection string
        var connectionString = getConnectionString();

        // Settings
        var settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .build();

        return MongoClients.create(settings);
    }

    @NotNull
    private String getConnectionString() {
        return buildConnectionString(
                Config.get("datasource.mongodb.username"),
                Config.get("datasource.mongodb.password"),
                Config.get("datasource.mongodb.host")
        );
    }

    @NotNull
    private String buildConnectionString(String username, String password, String host) {
        return "mongodb://" + username + ":" + password + "@" + host;
    }
}