package com.blibli.future.pos.restaurant.dao;

import com.blibli.future.pos.restaurant.Metadata;
import com.blibli.future.pos.restaurant.model.Category;

public interface CategoryDAO {
    public boolean create(Category category);
    public Category getById(int id);
    public Category[] getAll();
    public Category[] getWithMetadata(Metadata metadata);
    public boolean delete(Category category);
    public boolean update(Category category);
}
