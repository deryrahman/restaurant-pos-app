package com.blibli.future.pos.restaurant;

import org.apache.commons.dbcp.BasicDataSource;

public class MySQLUtilily {
    private static BasicDataSource dataSource;

    private MySQLUtilily(){
    }

    private static BasicDataSource getDataSource(Config config) {
        if (dataSource == null) {
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
        return dataSource;
    }
}
