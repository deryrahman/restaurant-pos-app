package com.blibli.future.pos.restaurant.restaurant;


import com.blibli.future.pos.restaurant.common.model.Restaurant;

import java.util.List;

public interface RestaurantDAO {

    /**
     * Create restaurant
     * @param restaurant : only one restaurant will be created
     * @return true if restaurant is successed to build, false otherwise
     */
    public boolean create(Restaurant restaurant);

    /**
     * Get restaurant by id
     * @param id : integer parameter, id must be valid
     * @return Restaurant object
     */
    public Restaurant getById(int id);

    /**
     * Get all restaurant with specific limitation
     * @param filter : is a WHERE statemtment of mysql query
     *               To get all restaurant, just set filter to "true"
     * @return list of filtered restaurant
     */
    public List<Restaurant> getBulk(String filter);

    /**
     * Delete restaurant
     * @param id integer : only one restaurant will be deleted. Restaurant must be valid
     * @return true if success to deleted, false otherwise
     */
    public boolean delete(int id);

    /**
     * Update restaurant
     * @param id, restaurant : only one restaurant will be updated. Restaurant must be valid
     * @return true if success to update, false otherwise
     */
    public boolean update(int id, Restaurant restaurant);
}
