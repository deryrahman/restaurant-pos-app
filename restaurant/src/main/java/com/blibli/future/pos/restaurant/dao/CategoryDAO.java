package com.blibli.future.pos.restaurant.dao;

import com.blibli.future.pos.restaurant.Metadata;
import com.blibli.future.pos.restaurant.model.Category;

import java.util.List;

public interface CategoryDAO {
    /**
     * Create category
     * @param category : only one category will be created
     * @return true if category is successed to build, false otherwise
     */
    public boolean create(Category category);

    /**
     * Get category by id
     * @param id : integer parameter, id must be valid
     * @return Category object
     */
    public Category getById(int id);

    /**
     * Get all category on database
     * @return list of category
     */
    public List<Category> getAll();

    /**
     * Get all category with specific limitation
     * @param metadata : have attributes limit and count.
     * @return list of filtered category
     */
    public List<Category> getWithMetadata(Metadata metadata);

    /**
     * Delete category
     * @param id integer : only one category will be deleted. Category must be valid
     * @return true if success to deleted, false otherwise
     */
    public boolean delete(int id);

    /**
     * Update category
     * @param id, category : only one category will be updated. Category must be valid
     * @return true if success to update, false otherwise
     */
    public boolean update(int id, Category category);
}
