package com.blibli.future.pos.restaurant.dao.restaurant;


import com.blibli.future.pos.restaurant.common.model.Restaurant;

import java.sql.SQLException;
import java.util.List;

public interface RestaurantDAO {

    /**
     * Create restaurant
     * @param restaurant : only one restaurant will be created
     */
    public void create(Restaurant restaurant) throws SQLException;

    /**
     * Get restaurant by id
     * @param id : integer parameter, id must be valid
     * @return Restaurant object
     */
    public Restaurant findById(int id) throws SQLException;

    /**
     * Get all restaurant with specific limitation
     * @param filter : is a WHERE statemtment of mysql query
     *               To get all restaurant, just set filter to "true"
     * @return list of filtered restaurant
     */
    public List<Restaurant> find(String filter) throws SQLException;

    /**
     * Delete restaurant
     * @param id integer : only one restaurant will be deleted. Restaurant must be valid
     */
    public void delete(int id) throws SQLException;

    /**
     * Update restaurant
     * @param id, restaurant : only one restaurant will be updated. Restaurant must be valid
     */
    public void update(int id, Restaurant restaurant) throws SQLException;
//
//    /**
//     * Add relation restaurant item
//     * @param itemId id item. Id must be valid
//     */
//    public void addRelationRestaurantItem(int itemId, int restaurantId, int stock) throws SQLException;
//
//    /**
//     * Get all restaurant on specified item
//     * @param itemId item id
//     * @return list of restaurant
//     */
//    public List<Restaurant> getAllRestaurantByItemId(int itemId, String filter) throws SQLException;
//
//    /**
//     * Delete relation restaurant item
//     * @param itemId id item. Id must be valid
//     */
//    public void deleteRelationRestaurantItem(int itemId, int restaurantId) throws SQLException;
//
//    /**
//     * Update relation
//     * @param itemId id item
//     * @param restaurantId it restaurant
//     * @param stock count of stock available
//     */
//    public void updateRelationRestaurantItem(int itemId, int restaurantId, int stock) throws SQLException;
}
