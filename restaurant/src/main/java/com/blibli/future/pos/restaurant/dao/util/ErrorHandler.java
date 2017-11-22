package com.blibli.future.pos.restaurant.dao.util;

import com.blibli.future.pos.restaurant.MySQLUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryMysql {
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public void open() throws SQLException {
        if (!isOpened()) {
            conn = MySQLUtility.getDataSource().getConnection();
        }
    }

    public void close() throws SQLException {
        if (isOpened()) {
            if (ps != null) ps.close();
            conn.close();
            conn = null;
        }
    }

    private boolean isOpened() {
        return conn != null;
    }

}