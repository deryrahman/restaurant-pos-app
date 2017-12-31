package com.blibli.future.pos.restaurant.common;

import com.blibli.future.pos.restaurant.common.model.Config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.servlet.ServletContext;
import java.sql.Connection;
import java.sql.SQLException;

@SuppressWarnings("ALL")
public class DataSource {
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    static {
        ServletContext ctx = ApplicationContex.getServletContext();
        Config cfg = (Config) ctx.getAttribute("restaurantConfig");
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setJdbcUrl(cfg.getJdbc().getUrl());
        config.setUsername(cfg.getJdbc().getUsername());
        config.setPassword(cfg.getJdbc().getPassword());
        config.setAutoCommit(false);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);
    }

    private DataSource(){}

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
