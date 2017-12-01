package com.blibli.future.pos.restaurant.item;

import com.blibli.future.pos.restaurant.common.model.Item;

import java.util.List;

public interface ItemDAO {

    /**
     * Create item
     * @param item : only one item will be created
     * @return true if category is successed to build, false otherwise
     */
    public boolean create(Item item);

    /**
     * Get item by id
     * @param id : integer parameter, id must be valid
     * @return Item object
     */
    public Item getById(int id);

    /**
     * Get all item with specific limitation
     * @param filter : is a WHERE statemtment of mysql query
     *               To get all items, just set filter to "true"
     * @return list of filtered items
     */
    public List<Item> getBulk(String filter);

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
