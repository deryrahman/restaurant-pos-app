package com.blibli.future.pos.restaurant.dao;


import com.blibli.future.pos.restaurant.Metadata;
import com.blibli.future.pos.restaurant.model.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class ItemDAOMysql implements ItemDAO{
    private Connection conn = null;
    private PreparedStatement ps = null;

    @Override
    public boolean create(Item item) {
        return false;
    }

    @Override
    public int getById(int id) {
        return 0;
    }

    @Override
    public List<Item> getAll() {
        return null;
    }

    @Override
    public List<Item> getWithMetadata(Metadata metadata) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public boolean update(int id, Item item) {
        return false;
    }
}
