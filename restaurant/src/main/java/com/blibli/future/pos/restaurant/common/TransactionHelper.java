package com.blibli.future.pos.restaurant.common;

import org.apache.log4j.MDC;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionHelper {
    private static final String LOG_CONNECTION = "connection";

    public static void init() throws SQLException {
        MDC.put(LOG_CONNECTION, DataSource.getConnection());
    }

    public static Connection getConnection() {
        return (Connection) MDC.get(LOG_CONNECTION);
    }

    public static void commit() throws SQLException {
        getConnection().commit();
    }

    public static void close() throws SQLException {
        getConnection().close();
    }

    public static void rollback() throws SQLException {
        getConnection().rollback();
    }
}
