package net.elliegabel.s.data.sql;

import com.elliegabel.s.data.Datasource;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Wrapper for postgresql datasource.
 */
@Singleton
public class SqlDatasource implements Datasource {

    private final HikariDataSource dataSource;

    @Inject
    public SqlDatasource(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @NotNull
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void closeConnection() {
        if (!isConnected()) {
            return;
        }

        dataSource.close();
    }

    public boolean isConnected() {
        return !dataSource.isClosed();
    }
}