package com.blibli.future.pos.restaurant.common;

import com.blibli.future.pos.restaurant.common.model.Config;
import org.apache.commons.dbcp.BasicDataSource;

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
