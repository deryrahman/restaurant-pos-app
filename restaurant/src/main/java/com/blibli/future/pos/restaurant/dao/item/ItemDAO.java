package com.blibli.future.pos.restaurant.dao.item;

import com.blibli.future.pos.restaurant.common.model.Item;

import java.sql.SQLException;
import java.util.List;

public interface ItemDAO {

    /**
     * Create item
     * @param item : only one item will be created
     */
    public void create(Item item) throws SQLException;

    /**
     * Get item by id
     * @param id : integer parameter, id must be valid
     * @return Item object
     */
    public Item findById(int id) throws SQLException;

    /**
     * Get all item with specific limitation
     * @param filter : is a WHERE statemtment of mysql query
     *               To get all items, just set filter to "true"
     * @return list of filtered items
     */
    public List<Item> find(String filter) throws SQLException;

    /**
     * Delete item
     * @param id integer : only one item will be deleted. Item must be valid
     */
    public void delete(int id) throws SQLException;

    /**
     * Update item
     * @param id, item: only one item will be updated. Item must be valid
     */
    public void update(int id, Item item) throws SQLException;
//
//    /**
//     * Add relation restaurant item
//     * @param itemId id item. Id must be valid
//     */
//    public void addToRestaurant(int itemId, int restaurantId, int stock) throws SQLException;
//
//    /**
//     * Get all item on specified retaurant
//     * @param restaurantId restaurant id
//     * @return list of item
//     */
//    public List<Item> getAllItemByRestaurantId(int restaurantId) throws SQLException;
//
//    /**
//     * Delete relation restaurant item
//     * @param itemId id item. Id must be valid
//     */
//    public void deleteRestaurant(int itemId, int restaurantId) throws SQLException;
//
//    /**
//     * Update relation
//     * @param itemId id item
//     * @param restaurantId it restaurant
//     * @param stock count of stock available
//     */
//    public void updateRelationRestaurantItem(int itemId, int restaurantId, int stock) throws SQLException;
}
