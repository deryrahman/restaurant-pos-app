package com.blibli.future.pos.restaurant.dao.category;

import com.blibli.future.pos.restaurant.dao.MysqlDAO;
import com.blibli.future.pos.restaurant.model.Category;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class CategoryDAOMysql extends MysqlDAO implements CategoryDAO {

    private void setCategoryFromQuery(Category category, ResultSet rs) throws SQLException {
        category.setId(rs.getInt("id"));
        category.setTimestampCreated(rs.getTimestamp("timestamp_created"));
        category.setName(rs.getString("name"));
        category.setDescription(rs.getString("description"));
        category.autoSetHref();
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
            setCategoryFromQuery(category, rs);
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
                setCategoryFromQuery(category, rs);
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
            String query = "UPDATE categories SET name = ?, description = ? WHERE id = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            ps.setInt(3, id);

            int affected = ps.executeUpdate();
            if (affected > 0) {
                return true;
            } else {
                message.setMessage("No affected query. No category update");
            }
        } catch (SQLException e) {
            message.setMessage("Something wrong on update category");
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }
}
