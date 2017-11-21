package com.blibli.future.pos.restaurant;

import org.apache.commons.dbcp.BasicDataSource;

import javax.servlet.ServletContext;

public class MySQLUtility {
    private static BasicDataSource dataSource;

    public static BasicDataSource getDataSource() {
        return dataSource;
    }

    public static void setDataSource(Config config){
        BasicDataSource ds = new BasicDataSource();

        String url = config.getJdbc().getUrl();
        String username = config.getJdbc().getUsername();
        String password = config.getJdbc().getPassword();

        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);

        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxOpenPreparedStatements(100);

        dataSource = ds;
    }
}
