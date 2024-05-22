package com.elliegabel.s.data.mongodb;

import com.elliegabel.s.data.Datasource;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

/**
 * Wrapper for the mongo client.
 */
@Singleton
public class MongoDBDatasource implements Datasource {

    private final MongoClient mongoClient;

    @Inject
    public MongoDBDatasource(MongoClient client) {
        this.mongoClient = client;
    }

    @NotNull
    public MongoDatabase getDatabase(@NotNull String database) {
        return mongoClient.getDatabase(database);
    }

    @NotNull
    public MongoCollection<Document> getCollection(@NotNull String database, @NotNull String collection) {
        return mongoClient.getDatabase(database).getCollection(collection);
    }

    @NotNull
    public MongoClient getMongoClient() {
        return mongoClient;
    }
}