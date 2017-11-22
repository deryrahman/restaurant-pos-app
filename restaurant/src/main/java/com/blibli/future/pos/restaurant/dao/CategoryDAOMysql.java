package com.blibli.future.pos.restaurant.dao;

import com.blibli.future.pos.restaurant.Metadata;
import com.blibli.future.pos.restaurant.MySQLUtility;
import com.blibli.future.pos.restaurant.dao.util.ErrorHandler;
import com.blibli.future.pos.restaurant.model.Category;
import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAOMysql implements CategoryDAO {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ErrorHandler errorHandler = new ErrorHandler();

    private boolean open() {
        if (conn != null) return true;
        try {
            conn = MySQLUtility.getDataSource().getConnection();
            return true;
        } catch (SQLException e) {
//            errorHandler.setMsg("Something wrong on opening connection");
            e.printStackTrace();
        }
        return false;
    }

    private boolean close() {
        if (conn == null) return true;
        try {
            conn.close();
            return true;
        } catch (SQLException e) {
//            errorHandler.setMsg("Something wrong on closing connection");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean create(Category category) {
        if (!open()) {
            return false;
        }
        try {
            String query = "INSERT INTO categories(name, description)" +
                    " VALUES(?, ?)";
            ps = conn.prepareStatement(query);
            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                return true;
            } else {
//                errorHandler.setMsg("No affected query. No category inserting");
            }
        } catch (SQLException e) {
//            errorHandler.setMsg("Something wrong on create category");
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }

    @Override
    public Category getById(int id) {
        Category category = new Category();
        if (!open()) {
            return category;
        }
        try {
            String query = "SELECT * FROM categories WHERE id = ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            rs.next();
            category.setId(rs.getInt("ID"));
            category.setName(rs.getString("name"));
            category.setDescription(rs.getString("description"));
            return category;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return category;
    }

    @Override
    public List<Category> getAll() {
        List<Category> categories = new ArrayList<Category>();
        if (!open()) {
            return categories;
        }
        try {
            String query = "SELECT * FROM categories";
            ps = conn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Category category = new Category();
                category.setId(rs.getInt("ID"));
                category.setName(rs.getString("name"));
                category.setDescription(rs.getString("description"));
                categories.add(category);
            }
            return categories;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return categories;
    }

    @Override
    public List<Category> getWithMetadata(Metadata metadata) {
        return new ArrayList<Category>();
    }

    @Override
    public boolean delete(int id) {
        return true;
    }

    @Override
    public boolean update(int id, Category category) {
        return true;
    }
}
