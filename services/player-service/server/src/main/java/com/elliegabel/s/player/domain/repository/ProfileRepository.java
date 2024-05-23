package com.elliegabel.s.player.domain.repository;

import com.elliegabel.s.data.DatabaseException;
import com.elliegabel.s.log.Log;
import com.elliegabel.s.player.dto.lookup.PlayerId;
import com.elliegabel.s.player.profile.PlayerProfile;
import com.elliegabel.s.player.profile.PlayerRank;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import net.elliegabel.s.data.sql.SqlDatasource;
import net.elliegabel.s.data.sql.SqlRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

/**
 * Defines player meta.
 * </br>
 * Structure:
 * Each PlayerId has a list of meta values in.
 */
@Singleton
public class ProfileRepository extends SqlRepository<PlayerProfile> {
    private static final Logger LOGGER = Log.newLogger(ProfileRepository.class);
    private static final String TABLE_NAME = "player_profiles";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_LAST_IP = "last_ip";
    private static final String COLUMN_FIRST_SEEN = "first_seen";
    private static final String COLUMN_LAST_SEEN = "last_seen";
    private static final String COLUMN_RANK = "rank";

    @Inject
    public ProfileRepository(SqlDatasource datasource) {
        super(datasource);
    }

    public Optional<PlayerProfile> getProfile(@NotNull UUID playerId) {
        return getByField(COLUMN_ID, playerId);
    }

    public void upsertProfile(@NotNull UUID playerId, @NotNull String username, @NotNull String ip) throws DatabaseException {
        try (Connection connection = datasource.getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("INSERT INTO " + TABLE_NAME + " (id, username, last_ip) " +
                            "VALUES (?, ?, ?) ON CONFLICT (id) DO " +
                            "UPDATE SET username = EXCLUDED.username, last_ip = EXCLUDED.last_ip, last_seen = CURRENT_TIMESTAMP");
            statement.setObject(1, playerId);
            statement.setString(2, username);
            statement.setString(3, ip);
            statement.executeUpdate();
        } catch (SQLException e) {
            logError("failed to upsert profile for {}", e, playerId);
            throw new DatabaseException();
        }
    }

    public Optional<PlayerProfile> getProfileByName(@NotNull String playerName) {
        return getByField("username", playerName);
    }

    @NotNull
    public Optional<String> getUsernameById(@NotNull UUID playerId) {
        try (Connection connection = datasource.getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("SELECT username FROM " + TABLE_NAME + " WHERE id = ?");
            statement.setObject(1, playerId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(resultSet.getString(COLUMN_USERNAME));
            }
        } catch (SQLException e) {
            logError("failed to get username by id for {}", e, playerId);
            throw new DatabaseException();
        }

        return Optional.empty();
    }

    @NotNull
    public Optional<PlayerId> getIdByUsername(@NotNull String playerName) {
        try (Connection connection = datasource.getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("SELECT id, username FROM " + TABLE_NAME + " WHERE username = ?");
            statement.setString(1, playerName);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new PlayerId(
                        resultSet.getObject(COLUMN_ID, UUID.class),
                        resultSet.getString(COLUMN_USERNAME)
                ));
            }
        } catch (SQLException e) {
            getLogger().error("failed to get id by username for {}", playerName);
            throw new DatabaseException();
        }

        return Optional.empty();
    }

    public boolean updateUsername(@NotNull UUID playerId, @NotNull String username) {
        return updateField(COLUMN_ID, playerId, COLUMN_USERNAME, username);
    }

    public boolean updateRank(@NotNull UUID playerId, @Nullable PlayerRank rank) {
        return updateField(COLUMN_ID, playerId, COLUMN_RANK, (rank != null ? rank.name() : null));
    }

    public boolean updateLastSeen(@NotNull UUID playerId) {
        return updateField(COLUMN_ID, playerId, COLUMN_LAST_SEEN, new Timestamp(System.currentTimeMillis()));
    }

    @Override
    @NotNull
    public PlayerProfile mapToType(@NotNull ResultSet resultSet) throws SQLException {
        UUID id = resultSet.getObject(COLUMN_ID, UUID.class);
        String username = resultSet.getString(COLUMN_USERNAME);
        String lastIp = resultSet.getString(COLUMN_LAST_IP);
        Timestamp firstSeen = resultSet.getTimestamp(COLUMN_FIRST_SEEN);
        Timestamp lastSeen = resultSet.getTimestamp(COLUMN_LAST_SEEN);

        String rawRank = resultSet.getString(COLUMN_RANK);
        PlayerRank rank = PlayerRank.PLAYER;
        if (rawRank != null) {
            rank = PlayerRank.valueOf(rawRank.toUpperCase());
        }

        return new PlayerProfile(
                id, username, lastIp, firstSeen.getTime(), lastSeen.getTime(), rank
        );
    }

    @Override
    public void createTableIfNotExists() {
        try (Connection connection = datasource.getConnection()) {
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    "id UUID PRIMARY KEY," +
                    "username VARCHAR(16) NOT NULL," +
                    "last_ip VARCHAR(32) NOT NULL," +
                    "first_seen TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "last_seen TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "rank VARCHAR(16) DEFAULT NULL" +
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
    public Logger getLogger() {
        return LOGGER;
    }
}
