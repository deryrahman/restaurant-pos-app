package com.blibli.future.pos.dao;

import com.blibli.future.pos.entity.Category;

import java.util.List;

/**
 * Created by dery on 10/27/17.
 */
public interface CategoryDAO {
    public List<Category> getAllCategories();
    public Category getCategory(Long id);
    public boolean createCategory(Category category);
    public void updateCategory(Category category);
    public void deleteCategoryById(Long id);
}
