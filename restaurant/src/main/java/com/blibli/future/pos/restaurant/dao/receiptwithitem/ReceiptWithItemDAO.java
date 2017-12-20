package com.blibli.future.pos.restaurant.dao.receiptwithitem;

import com.blibli.future.pos.restaurant.common.model.custom.ReceiptWithItem;

import java.sql.SQLException;
import java.util.List;

public interface ReceiptWithItemDAO {

    /**
     * Create item
     */
    public void create(ReceiptWithItem receiptWithItem) throws SQLException;

    /**
     * @param receiptId restaurant Id
     * @return itemwithstock
     */
    public ReceiptWithItem findById(Integer receiptId) throws SQLException;

    /**
     * @param filter filter
     * @return Item with stock
     */
    public List<ReceiptWithItem> find(String filter) throws SQLException;

    /**
     * Delete receiptwithitem
     */
    public void delete(Integer receiptId) throws SQLException;

    /**
     * Update receiptWithItem
     */
    public void update(Integer receiptId, ReceiptWithItem receiptWithItem) throws SQLException;
}
