package com.blibli.future.pos.dao_impl;

import com.blibli.future.pos.dao.CategoryDAO;
import com.blibli.future.pos.entity.Category;
import com.blibli.future.pos.util.JDBCConn;

import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by dery on 10/27/17.
 */
public class CategoryDAOImpl implements CategoryDAO {
    private List<Category> categories;
    Hashtable<Long, Category> hashCategories;
    private int size;
    private Connection conn;
    Statement stmt;
    PreparedStatement pstmt;

    public CategoryDAOImpl(){
        hashCategories = new Hashtable<Long, Category>();
        conn = JDBCConn.getConnection();
        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM categories";
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                //Retrieve by column name
                Long ID  = Long.valueOf(rs.getInt("ID"));
                String name = rs.getString("name");
                String description = rs.getString("description");

                // Create category
                Category category = new Category(ID);
                category.setName(name);
                category.setDescription(description);

                // Add to hash
                hashCategories.put(ID,category);
            }
            categories = new ArrayList<Category>(hashCategories.values());
            size = categories.size();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Category> getAllCategories() {
        return categories;
    }

    public Category getCategory(Long id) {
        if (id > size) return null;
        return hashCategories.get(id);
    }

    public boolean createCategory(Category category) {
        conn = null;
        pstmt = null;
        String query = "INSERT INTO categories (ID, name, description) VALUES (NULL, ?, ?);";

        String name = category.getName();
        String description = category.getDescription();
        try {
            conn = JDBCConn.getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            if(description != null) pstmt.setString(2, description);
            else pstmt.setNull(2, Types.INTEGER);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public void updateCategory(Category category) {

    }

    public void deleteCategoryById(Long id) {

    }
}
