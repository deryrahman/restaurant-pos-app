package com.blibli.future.pos.restaurant.dao;

import com.blibli.future.pos.restaurant.Metadata;
import com.blibli.future.pos.restaurant.model.Item;

import java.util.List;

public interface ItemDAO {

    /**
     * Create item
     * @param item : only one category will be created
     * @return true if category is successed to build, false otherwise
     */
    public boolean create(Item item);

    /**
     * Get item by id
     * @param id : integer parameter, id must be valid
     * @return Item object
     */
    public int getById(int id);

    /**
     * Get all item on database
     * @return list of item
     */
    public List<Item> getAll();

    /**
     * Get all item with specific limitation
     * @param metadata : have attributes limit and count.
     * @return list of filtered item
     */
    public List<Item> getWithMetadata(Metadata metadata);

    /**
     * Delete item
     * @param id integer : only one item will be deleted. Item must be valid
     * @return true if success to deleted, false otherwise
     */
    public boolean delete(int id);

    /**
     * Update item
     * @param id, item: only one item will be updated. Item must be valid
     * @return true if success to update, false otherwise
     */
    public boolean update(int id, Item item);
}
