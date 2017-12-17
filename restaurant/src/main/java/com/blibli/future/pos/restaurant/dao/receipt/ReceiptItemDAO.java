package com.blibli.future.pos.restaurant.dao.receipt;


import com.blibli.future.pos.restaurant.common.model.Item;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface ReceiptItemDAO {

    /**
     * Add com.blibli.future.pos.restaurant.item to receipt
     * @param receiptId, receiptId must be valid
     */
    public void createBulk(int receiptId, List<Item> items) throws SQLException;

    /**
     * Get all Item within receipt
     * @param receiptId : receiptId must be valid
     * @return list of items
     */
    public ArrayList<Item> getBulkByReceiptId(int receiptId) throws SQLException;
}
