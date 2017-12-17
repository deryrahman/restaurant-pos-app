package com.blibli.future.pos.restaurant.dao.item;

import com.blibli.future.pos.restaurant.common.model.Item;

import java.sql.SQLException;
import java.util.List;

public interface ItemDAO {

    /**
     * Create com.blibli.future.pos.restaurant.item
     * @param item : only one com.blibli.future.pos.restaurant.item will be created
     */
    public void create(Item item) throws SQLException;

    /**
     * Get com.blibli.future.pos.restaurant.item by id
     * @param id : integer parameter, id must be valid
     * @return Item object
     */
    public Item getById(int id) throws SQLException;

    /**
     * Get all com.blibli.future.pos.restaurant.item with specific limitation
     * @param filter : is a WHERE statemtment of mysql query
     *               To get all items, just set filter to "true"
     * @return list of filtered items
     */
    public List<Item> getBulk(String filter) throws SQLException;

    /**
     * Delete com.blibli.future.pos.restaurant.item
     * @param id integer : only one com.blibli.future.pos.restaurant.item will be deleted. Item must be valid
     */
    public void delete(int id) throws SQLException;

    /**
     * Update com.blibli.future.pos.restaurant.item
     * @param id, com.blibli.future.pos.restaurant.item: only one com.blibli.future.pos.restaurant.item will be updated. Item must be valid
     */
    public void update(int id, Item item) throws SQLException;

    /**
     * Add relation restaurant com.blibli.future.pos.restaurant.item
     * @param itemId id com.blibli.future.pos.restaurant.item. Id must be valid
     */
    public void addRelationRestaurantItem(int itemId, int restaurantId, int stock) throws SQLException;

    /**
     * Get all com.blibli.future.pos.restaurant.item on specified retaurant
     * @param restaurantId restaurant id
     * @return list of com.blibli.future.pos.restaurant.item
     */
    public List<Item> getAllItemByRestaurantId(int restaurantId, String filter) throws SQLException;

    /**
     * Delete relation restaurant com.blibli.future.pos.restaurant.item
     * @param itemId id com.blibli.future.pos.restaurant.item. Id must be valid
     */
    public void deleteRelationRestaurantItem(int itemId, int restaurantId) throws SQLException;

    /**
     * Update relation
     * @param itemId id com.blibli.future.pos.restaurant.item
     * @param restaurantId it restaurant
     * @param stock count of stock available
     */
    public void updateRelationRestaurantItem(int itemId, int restaurantId, int stock) throws SQLException;
}
