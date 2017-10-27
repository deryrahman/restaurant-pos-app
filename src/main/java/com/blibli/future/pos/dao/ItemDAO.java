package com.blibli.future.pos.dao;

import com.blibli.future.pos.entity.Item;

import java.util.List;

public interface ItemDAO {
    public List<Item> getAllItems();
    public Item getItem(Long id);
    public boolean createItem(Item item);
    public void updateItem(Item item);
    public void deleteItemById(Long id);
}
