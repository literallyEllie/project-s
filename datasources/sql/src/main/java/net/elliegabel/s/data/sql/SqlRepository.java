package net.elliegabel.s.data.sql;

import com.elliegabel.s.data.DatabaseException;
import com.elliegabel.s.data.Repository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class SqlRepository<T> implements Repository<T, ResultSet> {
    protected final SqlDatasource datasource;

    public SqlRepository(@NotNull SqlDatasource datasource) {
        this.datasource = datasource;

        createTableIfNotExists();
    }

    /**
     * @return Get all values in the repository.
     */
    @NotNull
    @Override
    public List<T> getAll() throws DatabaseException {
        List<T> elements = new ArrayList<>();

        try (Connection connection = datasource.getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("SELECT * FROM " + collectionName());

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                elements.add(mapToType(resultSet));
            }

        } catch (SQLException e) {
            logError("failed to get repository elements", e);
        }

        return elements;
    }

    /**
     * Get all elements in a repository with a filter.
     *
     * @param filterField Filter field.
     * @param filter      Filter value.
     * @return Matching values.
     */
    @NotNull
    protected List<T> getAllWithFilter(@NotNull String filterField, Object filter) {
        List<T> res = new ArrayList<>();

        try (Connection connection = datasource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM " + collectionName() + " WHERE " + filterField + " = ?"
            );
            statement.setObject(1, filter);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                res.add(mapToType(resultSet));
            }
        } catch (SQLException e) {
            logError("failed to get elements where {}={}", e, filterField, filter);
        }

        return res;
    }

    /**
     * Get a value with a query.
     *
     * @param queryField Field to check.
     * @param query      Value to compare.
     * @return The value, or empty if doesn't exist.
     */
    @NotNull
    protected Optional<T> getByField(@NotNull String queryField, @Nullable Object query) {
        try (Connection connection = datasource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM " + collectionName() + " WHERE " + queryField + " = ?"
            );
            if (query instanceof UUID) {
                statement.setObject(1, query, Types.OTHER);
            } else {
                statement.setObject(1, query);
            }


            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapToType(resultSet));
            }

        } catch (SQLException e) {
            logError("failed to get element where {}={}", e, query, query);
            throw new DatabaseException(e);
        }

        return Optional.empty();
    }

    /**
     * Get a value by its databased assigned id.
     *
     * @param id Id.
     * @return The value, or empty if doesn't exist.
     */
    @NotNull
    public Optional<T> getById(int id) {
        return getByField("id", id);
    }

    /**
     * Get a value by its databased assigned id.
     *
     * @param id Id.
     * @return The value, or empty if doesn't exist.
     */
    @NotNull
    public Optional<T> getById(UUID id) {
        return getByField("id", id);
    }

    /**
     * Check if a field with a value exists.
     *
     * @param field Field to check.
     * @param query Value to compare.
     * @return If this value is already set in the table.
     */
    protected boolean existsByField(@NotNull String field, @NotNull String query) {
        try (Connection connection = datasource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT " + field + " FROM " + collectionName() + " WHERE " + field + " = ?");
            statement.setString(1, query);

            return statement.executeQuery().next();
        } catch (SQLException e) {
            logError("failed to check if field {} exists by query {}", e, field, query);
            throw new DatabaseException(e);
        }
    }

    /**
     * Update a field.
     *
     * @param queryField  Lookup field.
     * @param query       Comparison.
     * @param updateField Field to update.
     * @param newValue    New value to use.
     * @return If was updated successfully.
     */
    protected boolean updateField(@NotNull String queryField, @NotNull Object query, @NotNull String updateField, @Nullable Object newValue) {
        try (Connection connection = datasource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE " + collectionName() + " SET " + updateField + " = ? WHERE " + queryField + " = ?");
            statement.setObject(1, newValue);
            statement.setObject(2, query);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logError("failed to update field for {}={}: {} -> {}", e, queryField, query, updateField, newValue);
            throw new DatabaseException(e);
        }
    }

    /**
     * Update a field by its database assigned ID.
     *
     * @param id          Id to update.
     * @param updateField The field to update.
     * @param newValue    The new value to replace with.
     * @return If the update was successfully.
     * @see SqlRepository#updateField(String, Object, String, Object)
     */
    protected boolean updateFieldById(int id, @NotNull String updateField, @Nullable Object newValue) {
        return updateField("id", id, updateField, newValue);
    }

    protected boolean updateFieldById(UUID id, @NotNull String updateField, @Nullable Object newValue) {
        return updateField("id", id, updateField, newValue);
    }

    /**
     * Delete an element by a query.
     *
     * @param queryField Lookup field.
     * @param query      Comparison.
     * @return If deleted successfully.
     */
    protected boolean deleteByField(@NotNull String queryField, @NotNull Object query) {
        try (Connection connection = datasource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM " + collectionName() + " WHERE " + queryField + " = ?"
            );
            statement.setObject(1, query);
            int deleted = statement.executeUpdate();

            return deleted > 0;
        } catch (SQLException e) {
            logError("failed to delete table element where {}={}", e, queryField, query);
            throw new DatabaseException(e);
        }
    }

    /**
     * Delete an element by its id.
     *
     * @param id Id to delete by.
     * @return If deleted successfully.
     */
    protected boolean deleteDataById(int id) {
        return deleteByField("id", id);
    }

    protected boolean deleteDataById(@NotNull UUID playerId) {
        return deleteByField("id", playerId);
    }

    @NotNull
    public abstract T mapToType(@NotNull ResultSet resultSet) throws SQLException;

    protected void logError(String err, Exception e, Object... params) {
        getLogger().error(err.formatted(params), e);
    }

    protected abstract Logger getLogger();
}