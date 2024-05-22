package com.elliegabel.s.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A generic data source repository.
 *
 * @param <T> What is stored in this repository
 * @param <M> Raw result set type.
 */
public interface Repository<T, M> {

    @NotNull
    List<T> getAll() throws DatabaseException;

    @Nullable
    T mapToType(@NotNull M resultSet) throws Exception;

    void createTableIfNotExists();

    @NotNull
    String collectionName();
}