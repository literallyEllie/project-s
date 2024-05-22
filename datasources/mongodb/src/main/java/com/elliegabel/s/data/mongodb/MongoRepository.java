package com.elliegabel.s.data.mongodb;

import com.elliegabel.s.data.DatabaseException;
import com.elliegabel.s.data.Repository;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import jakarta.inject.Inject;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for mongo collections.
 *
 * @param <T> Stored type.
 */
public abstract class MongoRepository<T> implements Repository<T, Document> {
    protected final MongoCollection<Document> collection;

    @Inject
    protected MongoRepository(MongoCollection<Document> collection) {
        this.collection = collection;
    }

    @Override
    @NotNull
    public List<T> getAll() throws DatabaseException {
        return getAll(null);
    }

    @NotNull
    public List<T> getAll(@Nullable Bson filter) throws DatabaseException {
        List<T> res = new ArrayList<>();

        FindIterable<Document> documents = filter != null
                ? getMongoCollection().find(filter)
                : getMongoCollection().find();

        for (Document document : documents) {
            res.add(mapToType(document));
        }

        return res;
    }

    @NotNull
    protected Optional<T> getByField(@NotNull String queryField, @NotNull Object field) throws DatabaseException {
        Document first = getMongoCollection()
                .find(new Document(queryField, field))
                .limit(1).first();

        if (first == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(mapToType(first));
    }

    protected boolean delete(@NotNull Bson filter) {
        return getMongoCollection().deleteOne(filter).getDeletedCount() > 0;
    }

    protected boolean deleteMany(@NotNull Bson filter) {
        return getMongoCollection().deleteMany(filter).getDeletedCount() > 0;
    }

    protected boolean deleteByField(@NotNull String queryField, @NotNull Object matching) {
        return delete(new Document(queryField, matching));
    }

    @Override
    @Nullable
    public abstract T mapToType(@NotNull Document resultSet) throws DatabaseException;

    @NotNull
    protected MongoCollection<Document> getMongoCollection() {
        return collection;
    }
}