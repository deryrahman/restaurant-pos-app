package com.blibli.future.pos.restaurant.dao.itemwithstock;

import com.blibli.future.pos.restaurant.common.model.custom.ItemWithStock;

import java.sql.SQLException;
import java.util.List;

public interface ItemWithStockDAO {

    /**
     * Create item
     */
    public void create(Integer restaurantId, Integer itemId, Integer stock) throws SQLException;

    /**
     * @param restaurantId restaurant Id
     * @param itemId item id
     * @return itemwithstock
     */
    public ItemWithStock findById(Integer restaurantId, Integer itemId) throws SQLException;

    /**
     * @param restaurantId id restaurant
     * @param filter filter
     * @return Item with stock
     */
    public List<ItemWithStock> findByRestaurantId(Integer restaurantId, String filter) throws SQLException;

    /**
     * @param itemId id restaurant
     * @param filter filter
     * @return Item with stock
     */
    public List<ItemWithStock> findByItemId(Integer itemId, String filter) throws SQLException;

    /**
     * @param filter filter
     * @return Item with stock
     */
    public List<ItemWithStock> find(String filter) throws SQLException;

    /**
     * Delete item
     */
    public void delete(Integer restaurantId, Integer itemId) throws SQLException;

    /**
     * Update item
     */
    public void update(Integer restaurantId, Integer itemId, Integer stock) throws SQLException;
}
