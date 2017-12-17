package com.blibli.future.pos.restaurant.dao.category;

import com.blibli.future.pos.restaurant.common.model.Category;

import java.sql.SQLException;
import java.util.List;

public interface CategoryDAO {
    /**
     * Create com.blibli.future.pos.restaurant.category
     * @param category : only one com.blibli.future.pos.restaurant.category will be created
     */
    public void create(Category category) throws SQLException;

    /**
     * Get com.blibli.future.pos.restaurant.category by id
     * @param id : integer parameter, id must be valid
     * @return Category object
     */
    public Category getById(int id) throws SQLException;

    /**
     * Get all com.blibli.future.pos.restaurant.category with specific limitation
     * @param filter : is a WHERE statemtment of mysql query
     *               To get all categories, just set filter to "true"
     * @return list of filtered com.blibli.future.pos.restaurant.category
     */
    public List<Category> getBulk(String filter) throws SQLException;

    /**
     * Delete com.blibli.future.pos.restaurant.category
     * @param id integer : only one com.blibli.future.pos.restaurant.category will be deleted. Category must be valid
     */
    public void delete(int id) throws SQLException;

    /**
     * Update com.blibli.future.pos.restaurant.category
     * @param id, com.blibli.future.pos.restaurant.category : only one com.blibli.future.pos.restaurant.category will be updated. Category must be valid
     */
    public void update(int id, Category category) throws SQLException;

}
