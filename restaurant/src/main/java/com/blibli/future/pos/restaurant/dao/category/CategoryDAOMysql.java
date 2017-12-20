package com.blibli.future.pos.restaurant.dao.category;

import com.blibli.future.pos.restaurant.common.TransactionHelper;
import com.blibli.future.pos.restaurant.dao.MysqlDAO;
import com.blibli.future.pos.restaurant.common.model.Category;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class CategoryDAOMysql extends MysqlDAO<Category> implements CategoryDAO {
    private Category category;
    private List<Category> categories;

    @Override
    protected void mappingObject(Category category, ResultSet rs) throws SQLException {
        category.setId(rs.getInt("id"));
        category.setTimestampCreated(rs.getTimestamp("timestamp_created"));
        category.setName(rs.getString("name"));
        category.setDescription(rs.getString("description"));
    }

    @Override
    public void create(Category category) throws SQLException {
        String query = "INSERT INTO categories(name, description)" +
                " VALUES(?, ?)";
        ps = TransactionHelper.getConnection().prepareStatement(query);
        ps.setString(1, category.getName());
        ps.setString(2, category.getDescription());

        int affected = ps.executeUpdate();
        if (affected <= 0) {
            throw new SQLException("No affected query. No com.blibli.future.pos.restaurant.category inserting");
        }
    }

    @Override
    public Category findById(int id) throws SQLException {
        category  = new Category();

        categories = find("id = "+ id);
        if(categories.size()>0){
            category = categories.get(0);
        }
        return category;
    }

    @Override
    public List<Category> find(String filter) throws SQLException {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM categories WHERE "+filter;
        ps = TransactionHelper.getConnection().prepareStatement(query);

        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            Category category = new Category();
            mappingObject(category, rs);
            categories.add(category);
        }
        return categories;
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM categories WHERE id = ?";
        ps = TransactionHelper.getConnection().prepareStatement(query);
        ps.setInt(1, id);

        int affected = ps.executeUpdate();
        if (affected <= 0) {
            throw new SQLException("No affected query. No com.blibli.future.pos.restaurant.category deleted");
        }
    }

    @Override
    public void update(int id, Category category) throws SQLException {
        String query = "UPDATE categories SET name = ?, description = ? WHERE id = ?";
        ps = TransactionHelper.getConnection().prepareStatement(query);
        ps.setString(1, category.getName());
        ps.setString(2, category.getDescription());
        ps.setInt(3, id);

        int affected = ps.executeUpdate();
        if (affected <= 0) {
            throw new SQLException("No affected query. No com.blibli.future.pos.restaurant.category update");
        }
    }
}
