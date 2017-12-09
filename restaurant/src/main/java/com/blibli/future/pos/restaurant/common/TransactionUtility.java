package com.blibli.future.pos.restaurant.common;

import org.apache.log4j.MDC;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionUtility {
    public static final String connKey = "thread";

    public static void createTransaction() throws SQLException {
        Connection conn = MySQLUtility.getDataSource().getConnection();
        conn.setAutoCommit(false);
        MDC.put(connKey, conn);
    }

    public static Connection getConnection(){
        return (Connection) MDC.get(connKey);
    }

    public static void commitTransaction() throws SQLException {
        Connection conn = getConnection();
        conn.commit();
        conn.close();
    }

}
