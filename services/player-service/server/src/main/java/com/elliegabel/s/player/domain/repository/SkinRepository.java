package com.elliegabel.s.player.domain.repository;

import com.elliegabel.s.data.DatabaseException;
import com.elliegabel.s.log.Log;
import com.elliegabel.s.player.skin.PlayerSkinData;
import jakarta.inject.Singleton;
import net.elliegabel.s.data.sql.SqlDatasource;
import net.elliegabel.s.data.sql.SqlRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class SkinRepository extends SqlRepository<PlayerSkinData> {
    private static final Logger LOGGER = Log.newLogger(SkinRepository.class);

    private static final String TABLE_NAME = "player_skins";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TEXTURES = "textures";
    private static final String COLUMN_SIGNATURE = "signature";

    public SkinRepository(SqlDatasource datasource) {
        super(datasource);
    }

    // todo consider skin compression

    public Optional<PlayerSkinData> getSkinData(@NotNull UUID playerId) {
        return getByField("id", playerId);
    }

    public boolean insertSkinData(@NotNull PlayerSkinData data) {
        try (Connection connection = datasource.getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("INSERT INTO " + TABLE_NAME + " (id, textures, signature) " +
                            "VALUES (?, ?, ?) ON CONFLICT (id) DO " +
                            "UPDATE SET textures = EXCLUDED.textures, signature = EXCLUDED.signature");
            statement.setObject(1, data.playerId());
            statement.setString(2, data.texture());
            statement.setString(3, data.signature());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logError("failed to insert skin data {}", e, data);
            throw new DatabaseException();
        }
    }

    @Override
    @NotNull
    public PlayerSkinData mapToType(@NotNull ResultSet resultSet) throws SQLException {
        UUID playerId = resultSet.getObject("id", UUID.class);
        String textures = resultSet.getString("textures");
        String signature = resultSet.getString("signature");
        return new PlayerSkinData(playerId, textures, signature);
    }

    @Override
    public void createTableIfNotExists() {
        try (Connection connection = datasource.getConnection()) {
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    "id UUID PRIMARY KEY," +
                    "textures VARCHAR(432) NOT NULL," +
                    "signature VARCHAR(686) NOT NULL" +
                    ");").execute();
        } catch (SQLException e) {
            throw new RuntimeException("failed to create " + TABLE_NAME + " table", e);
        }
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    @Override
    @NotNull
    public String collectionName() {
        return TABLE_NAME;
    }
}
