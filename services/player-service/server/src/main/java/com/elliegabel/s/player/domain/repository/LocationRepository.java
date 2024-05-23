package com.elliegabel.s.player.domain.repository;

import com.elliegabel.s.data.DatabaseException;
import com.elliegabel.s.log.Log;
import com.elliegabel.s.player.location.PlayerLocation;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import net.elliegabel.s.data.sql.SqlDatasource;
import net.elliegabel.s.data.sql.SqlRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class LocationRepository extends SqlRepository<PlayerLocation> {
    private static final Logger LOGGER = Log.newLogger(LocationRepository.class);
    private static final String TABLE_NAME = "player_locations";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PROXY_ID = "proxy_id";
    private static final String COLUMN_SERVER_ID = "server_id";
    private static final String COLUMN_UPDATED = "updated";

    @Inject
    public LocationRepository(SqlDatasource datasource) {
        super(datasource);
    }

    public boolean setFullLocation(@NotNull UUID playerId, @NotNull String proxyId, @NotNull String serverId) {
        try (Connection connection = datasource.getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("INSERT INTO " + TABLE_NAME + " (id, proxy_id, server_id) " +
                            "VALUES (?, ?, ?) ON CONFLICT (id) DO " +
                            "UPDATE SET proxy_id = EXCLUDED.proxy_id, server_id = EXCLUDED.server_id, updated = NOW()");
            statement.setObject(1, playerId);
            statement.setString(2, proxyId);
            statement.setString(3, serverId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logError("failed to set location {}", e, playerId);
            throw new DatabaseException();
        }
    }

    public boolean setLocation(@NotNull UUID playerId, @NotNull String serverId) {
        return updateFieldById(playerId, COLUMN_SERVER_ID, serverId);
    }

    public boolean deleteLocation(@NotNull UUID playerId) {
        return deleteDataById(playerId);
    }

    @NotNull
    public Optional<String> getProxyId(@NotNull UUID playerId) throws DatabaseException {
        try (Connection connection = datasource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT proxy_id FROM " + TABLE_NAME + " WHERE id = ?");
            preparedStatement.setObject(1, playerId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(resultSet.getString(COLUMN_PROXY_ID));
            }

            return Optional.empty();
        } catch (SQLException e) {
            logError("failed to get {} proxyId", e, playerId);
            throw new DatabaseException();
        }
    }

    @NotNull
    public Optional<String> getServerId(@NotNull UUID playerId) throws DatabaseException {
        try (Connection connection = datasource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT server_id FROM " + TABLE_NAME + " WHERE id = ?");
            preparedStatement.setObject(1, playerId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(resultSet.getString(COLUMN_SERVER_ID));
            }

            return Optional.empty();
        } catch (SQLException e) {
            logError("failed to get {} serverId", e, playerId);
            throw new DatabaseException();
        }
    }

    public List<UUID> getPlayersOnProxy(@NotNull String proxyId) {
        try (Connection connection = datasource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM " + TABLE_NAME + " WHERE proxy_id = ?");
            preparedStatement.setString(1, proxyId);

            ResultSet resultSet = preparedStatement.executeQuery();
            List<UUID> players = new ArrayList<>();
            while (resultSet.next()) {
                players.add(UUID.fromString(resultSet.getString(COLUMN_ID)));
            }

            return players;
        } catch (SQLException e) {
            logError("failed to get players on proxy: {}", e, proxyId);
            throw new DatabaseException();
        }
    }

    public List<UUID> getPlayersOnServer(@NotNull String serverId) {
        try (Connection connection = datasource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM " + TABLE_NAME + " WHERE server_id = ?");
            preparedStatement.setString(1, serverId);

            ResultSet resultSet = preparedStatement.executeQuery();
            List<UUID> players = new ArrayList<>();
            while (resultSet.next()) {
                players.add(UUID.fromString(resultSet.getString(COLUMN_ID)));
            }

            return players;
        } catch (SQLException e) {
            logError("failed to get players on server: {}", e, serverId);
            throw new DatabaseException();
        }
    }

    @Override
    @NotNull
    public PlayerLocation mapToType(@NotNull ResultSet resultSet) throws SQLException {
        String proxyId = resultSet.getString(COLUMN_PROXY_ID);
        String serverId = resultSet.getString(COLUMN_SERVER_ID);
        return new PlayerLocation(proxyId, serverId);
    }

    @Override
    public void createTableIfNotExists() {
        try (Connection connection = datasource.getConnection()) {
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    "id UUID PRIMARY KEY," +
                    "proxy_id VARCHAR(32) NOT NULL," +
                    "server_id VARCHAR(32) NOT NULL," +
                    "updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP" +
                    ");").execute();
        } catch (SQLException e) {
            throw new RuntimeException("failed to create " + TABLE_NAME + " table", e);
        }
    }

    @Override
    @NotNull
    public String collectionName() {
        return TABLE_NAME;
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}
