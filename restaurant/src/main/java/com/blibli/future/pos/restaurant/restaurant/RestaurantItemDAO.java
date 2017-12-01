package com.blibli.future.pos.restaurant.restaurant;


import com.blibli.future.pos.restaurant.common.model.Item;

import java.util.List;

public interface RestaurantItemDAO {

    /**
     * Add item to restaurant
     * @param itemId, restaurantId must be valid
     * @return true if success to add to restaurant_item, false otherwise
     */
    public boolean create(int itemId, int restaurantId, int stock);

    /**
     * Get all item with specific limitation
     * @param filter : is a WHERE statemtment of mysql query
     *               To get all items, just set filter to "true"
     * @return list of filtered items
     */
    public List<Item> getBulk(String filter);

    /**
     * Update stock for specific restaurant
     * @param itemId, restaurantId must be valid
     * @return true if success, false otherwise
     */
    public boolean update(int itemId, int restaurantId, int stock);
}
