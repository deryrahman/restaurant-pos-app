package util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource dataSource;

    static {
        config.setJdbcUrl(MySQLConfiguration.DB_URL);
        config.setDriverClassName(MySQLConfiguration.JDBC_DRIVER);
        config.setUsername(MySQLConfiguration.USER);
        config.setPassword(MySQLConfiguration.PASS);
        dataSource = new HikariDataSource(config);
    }

    private DataSource() {}

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
