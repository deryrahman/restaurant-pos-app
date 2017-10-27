package com.blibli.future.pos.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCConnSample {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/<DB NAME>";

    //  Database credentials
    static final String USER = "<USER DB>";
    static final String PASS = "<USER PASS>";

    static Connection connection = null;

    public static Connection getConnection(){
        try {
            Class.forName(JDBC_DRIVER).newInstance();
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
