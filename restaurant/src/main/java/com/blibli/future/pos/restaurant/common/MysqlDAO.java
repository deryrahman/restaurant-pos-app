package com.blibli.future.pos.restaurant.common;

import com.blibli.future.pos.restaurant.common.model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MysqlDAO {

    protected Connection conn = null;
    protected PreparedStatement ps = null;
    protected Message message = new Message();

    /**
     * Open connection from db pool
     * @return true if connection successed, false otherwise
     */
    protected boolean open() {
        if (conn != null) return true;
        try {
            conn = MySQLUtility.getDataSource().getConnection();
            return true;
        } catch (SQLException e) {
            message.setMessage("Something wrong on opening connection");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Close connection from db pool, and close prepared statement
     */
    protected void close() {
        try {
            conn.close();
            ps.close();
        } catch (SQLException e) {
            message.setMessage("Something wrong on closing connection");
            e.printStackTrace();
        }
    }

    /**
     * Call message inside DAO, when there are another error, or messages being transfered from dao
     * @return message
     */
    public Message getMessage(){
        return message;
    }
}
