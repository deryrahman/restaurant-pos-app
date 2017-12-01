package com.blibli.future.pos.restaurant.dao;

import com.blibli.future.pos.restaurant.Metadata;
import com.blibli.future.pos.restaurant.MySQLUtility;
import com.blibli.future.pos.restaurant.model.Category;
import com.blibli.future.pos.restaurant.services.Message;

import javax.ws.rs.QueryParam;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class CategoryDAOMysql implements CategoryDAO, DAO {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private Message message = new Message();

    @Override
    public boolean open() {
        if (conn != null) return true;
        try {
            conn = MySQLUtility.getDataSource().getConnection();
            return true;
        } catch (SQLException e) {
            message.setMessage("Something wrong on opening connection");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void close() {
        try {
            conn.close();
            ps.close();
        } catch (SQLException e) {
            message.setMessage("Something wrong on closing connection");
            e.printStackTrace();
        }
    }

    @Override
    public Message getMessage(){
        return message;
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
                message.setMessage("No affected query. No category inserting");
            }
        } catch (SQLException e) {
            message.setMessage("Something wrong on create category");
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
            message.setMessage("Something wrong on get category");
            e.printStackTrace();
        } finally {
            close();
        }
        return category;
    }

    @Override
    public List<Category> getBulk(String filter) {
        List<Category> categories = new ArrayList<>();
        if (!open()) {
            return categories;
        }
        try {
            String query = "SELECT * FROM categories WHERE "+filter;
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
            message.setMessage("Something wrong on getBulk categories");
            e.printStackTrace();
        } finally {
            close();
        }
        return categories;
    }

    @Override
    public boolean delete(int id) {
        if (!open()) {
            return false;
        }
        try {
            String query = "DELETE FROM categories WHERE id = ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);

            int affected = ps.executeUpdate();
            if (affected > 0) {
                return true;
            } else {
                message.setMessage("No affected query. No category deleted");
            }
        } catch (SQLException e) {
            message.setMessage("Something wrong on delete category");
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }

    @Override
    public boolean update(int id, Category category) {
        if (!open()) {
            return false;
        }
        try {
            String query = "UPDATE categories SET id = ?, name = ?, description = ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);

            int affected = ps.executeUpdate();
            if (affected > 0) {
                return true;
            } else {
                message.setMessage("No affected query. No category deleted");
            }
        } catch (SQLException e) {
            message.setMessage("Something wrong on delete category");
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }
}
