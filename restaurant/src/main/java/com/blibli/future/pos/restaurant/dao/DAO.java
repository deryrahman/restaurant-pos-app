package com.blibli.future.pos.restaurant.dao;

import com.blibli.future.pos.restaurant.services.Message;

public interface DAO {
    /**
     * Open connection from db pool
     * @return true if connection successed, false otherwise
     */
    public boolean open();

    /**
     * Close connection from db pool, and close prepared statement
     */
    public void close();

    /**
     * Call message inside DAO, when there are another error, or messages being transfered from dao
     * @return message
     */
    public Message getMessage();
}
