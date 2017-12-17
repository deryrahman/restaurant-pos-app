package com.blibli.future.pos.restaurant.dao.receipt;


import com.blibli.future.pos.restaurant.common.model.Receipt;

import java.sql.SQLException;
import java.util.List;

public interface ReceiptDAO {

    /**
     * Create receipt
     * @param receipt : only one receipt will be created
     */
    public void create(Receipt receipt) throws SQLException;

    /**
     * Get receipt by id
     * @param id : integer parameter, id must be valid
     * @return Receipt object
     */
    public Receipt getById(int id) throws SQLException;

    /**
     * Get all receipt with specific limitation
     * @param filter : is a WHERE statemtment of mysql query
     *               To get all receipts, just set filter to "true"
     * @return list of filtered receipts
     */
    public List<Receipt> getBulk(String filter) throws SQLException;

    /**
     * Delete receipt
     * @param id integer : only one receipt will be deleted. Receipt must be valid
     */
    public void delete(int id) throws SQLException;

    /**
     * Update receipt
     * @param id, receipt: only one receipt will be updated. Receipt must be valid
     */
    public void update(int id, Receipt receipt) throws SQLException;
}
