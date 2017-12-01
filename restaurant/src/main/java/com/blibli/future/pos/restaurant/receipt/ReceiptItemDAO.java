package com.blibli.future.pos.restaurant.receipt;


import com.blibli.future.pos.restaurant.common.model.Item;

import java.util.ArrayList;
import java.util.List;

public interface ReceiptItemDAO {

    /**
     * Add item to receipt
     * @param receiptId, receiptId must be valid
     * @return true if success to add to receipt_item, false otherwise
     */
    public boolean createBulk(int receiptId, List<Item> items);

    /**
     * Get all Item within receipt
     * @param receiptId : receiptId must be valid
     * @return list of items
     */
    public ArrayList<Item> getBulkByReceiptId(int receiptId);
}
