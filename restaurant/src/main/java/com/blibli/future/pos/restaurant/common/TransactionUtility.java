package com.blibli.future.pos.restaurant.common;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionUtility {
//    private static final String LOG_CONNECTION = "connection";
//
//    public static Connection getConnection() {
//        return (Connection) MDC.get(LOG_CONNECTION);
//    }
//
//    public static void beginTransaction() throws SQLException {
//        MDC.put(LOG_CONNECTION, DataSource.getConnection());
//    }
//
//    public static void commitTransaction() throws SQLException {
//        Connection conn = getConnection();
//        conn.commit();
//        conn.close();
//    }
    private static Connection conn;

    public static Connection getConnection() throws SQLException {
        if(conn == null) beginTransaction();
        return conn;
    }

    public static void beginTransaction() throws SQLException {
        conn = DataSource.getConnection();
    }

    public static void commitTransaction() throws SQLException {
        conn.commit();
        conn.close();
    }
}
