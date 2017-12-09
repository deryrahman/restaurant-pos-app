package com.blibli.future.pos.restaurant.common;

import com.blibli.future.pos.restaurant.common.model.Message;
import org.apache.log4j.MDC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MysqlDAO {

    protected Connection conn = TransactionUtility.getConnection();
    protected PreparedStatement ps = null;
    protected Message message = new Message();

    /**
     * Open connection from db pool
     * @return true if connection successed, false otherwise
     */
    protected boolean open() {
        if (conn != null) return true;
        conn = TransactionUtility.getConnection();
        return true;
    }

    /**
     * Close connection from db pool, and close prepared statement
     */
    protected void close() throws SQLException {
        TransactionUtility.commitTransaction();
    }

    /**
     * Call message inside DAO, when there are another error, or messages being transfered from dao
     * @return message
     */
    public Message getMessage(){
        return message;
    }
}
