package com.blibli.future.pos.restaurant.receipt;


import com.blibli.future.pos.restaurant.common.model.Receipt;

import java.util.List;

public interface ReceiptDAO {

    /**
     * Create receipt
     * @param receipt : only one receipt will be created
     * @return lastId if receipt is successed to build, 0 otherwise
     */
    public int create(Receipt receipt);

    /**
     * Get receipt by id
     * @param id : integer parameter, id must be valid
     * @return Receipt object
     */
    public Receipt getById(int id);

    /**
     * Get all receipt with specific limitation
     * @param filter : is a WHERE statemtment of mysql query
     *               To get all receipts, just set filter to "true"
     * @return list of filtered receipts
     */
    public List<Receipt> getBulk(String filter);

    /**
     * Delete receipt
     * @param id integer : only one receipt will be deleted. Receipt must be valid
     * @return true if success to deleted, false otherwise
     */
    public boolean delete(int id);

    /**
     * Update receipt
     * @param id, receipt: only one receipt will be updated. Receipt must be valid
     * @return true if success to update, false otherwise
     */
    public boolean update(int id, Receipt receipt);
}
