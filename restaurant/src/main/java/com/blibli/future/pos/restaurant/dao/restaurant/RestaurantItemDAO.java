package com.blibli.future.pos.restaurant.dao.restaurant;


import com.blibli.future.pos.restaurant.common.model.Item;

import java.sql.SQLException;
import java.util.List;

public interface RestaurantItemDAO {

    /**
     * Add com.blibli.future.pos.restaurant.item to restaurant
     * @param itemId, restaurantId must be valid
     */
    public void create(int itemId, int restaurantId, int stock) throws SQLException;

    /**
     * Get all com.blibli.future.pos.restaurant.item with specific limitation
     * @param filter : is a WHERE statemtment of mysql query
     *               To get all items, just set filter to "true"
     * @return list of filtered items
     */
    public List<Item> getBulk(String filter) throws SQLException;

    /**
     * Update stock for specific restaurant
     * @param itemId, restaurantId must be valid
     */
    public void update(int itemId, int restaurantId, int stock) throws SQLException;
}
