package net.elliegabel.s.data.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.avaje.config.Config;
import io.avaje.inject.Bean;
import io.avaje.inject.Factory;
import org.jetbrains.annotations.NotNull;

/**
 * Hikari provider.
 * Currently implementing PostgresSql.
 */
@Factory
public class SqlFactory {
    private static final String POSTGRES_DATA_SOURCE_CLASS_NAME = "org.postgresql.ds.PGSimpleDataSource";

    @Bean
    static HikariDataSource dataSource() {
        HikariConfig config = loadCredentials();
        config.setDataSourceClassName(POSTGRES_DATA_SOURCE_CLASS_NAME);
        config.setLeakDetectionThreshold(60 * 1000);
        return new HikariDataSource(config);
    }

    @NotNull
    private static HikariConfig loadCredentials() {
        var config = new HikariConfig();
        config.setJdbcUrl(Config.get("datasource.sql.url", "jdbc:postgresql://localhost:5432/project_s"));
        config.setUsername(Config.get("datasource.sql.username", ""));
        config.setPassword(Config.get("datasource.sql.password", ""));
        return new HikariConfig();
    }
}