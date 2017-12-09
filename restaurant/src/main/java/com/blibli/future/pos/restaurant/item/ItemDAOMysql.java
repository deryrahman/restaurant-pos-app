package com.blibli.future.pos.restaurant.item;

import com.blibli.future.pos.restaurant.common.MysqlDAO;
import com.blibli.future.pos.restaurant.common.model.Item;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class ItemDAOMysql extends MysqlDAO implements ItemDAO{

    private void setItemFromQuery(Item item, ResultSet rs) throws SQLException {
        item.setId(rs.getInt("id"));
        item.setName(rs.getString("name"));
        item.setTimestampCreated(rs.getTimestamp("timestamp_created"));
        item.setPrice(rs.getBigDecimal("price"));
        item.setDescription(rs.getString("description"));
        item.setCategoryId(rs.getInt("category_id"));
        item.setStatus(rs.getString("status"));
        item.autoSetHref();
    }

    @Override
    public boolean create(Item item) {

        try {
            String query = "INSERT INTO items(name, price, description, category_id, status)" +
                    " VALUES(?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(query);
            ps.setString(1, item.getName());
            ps.setBigDecimal(2, item.getPrice());
            ps.setString(3, item.getDescription());
            ps.setInt(4, item.getCategoryId());
            ps.setString(5, item.getStatus());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                return true;
            } else {
                message.setMessage("No affected query. No item inserting");
            }
        } catch (SQLException e) {
            message.setMessage("Something wrong on create item");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Item getById(int id) {
        Item item = new Item();
        if (!open()) {
            return item;
        }
        try {
            String query = "SELECT * FROM items WHERE id = ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            rs.next();
            setItemFromQuery(item, rs);
            return item;
        } catch (SQLException e) {
            message.setMessage("Something wrong on get item");
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public List<Item> getBulk(String filter) {
        List<Item> items = new ArrayList<>();
        if (!open()) {
            return items;
        }
        try {
            String query = "SELECT * FROM items WHERE "+filter;
            ps = conn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Item item = new Item();
                setItemFromQuery(item, rs);
                items.add(item);
            }
            return items;
        } catch (SQLException e) {
            message.setMessage("Something wrong on getBulk items");
            e.printStackTrace();
        }
        return items;
    }

    @Override
    public boolean delete(int id) {

        try {
            String query = "DELETE FROM items WHERE id = ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);

            int affected = ps.executeUpdate();
            if (affected > 0) {
                return true;
            } else {
                message.setMessage("No affected query. No item deleted");
            }
        } catch (SQLException e) {
            message.setMessage("Something wrong on delete item");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(int id, Item item) {

        try {
            String query = "UPDATE items SET " +
                    "name = ?, " +
                    "price = ?," +
                    "description = ?," +
                    "category_id = ?," +
                    "status = ? +" +
                    "WHERE id = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, item.getName());
            ps.setBigDecimal(2, item.getPrice());
            ps.setString(3, item.getDescription());
            ps.setInt(4, item.getCategoryId());
            ps.setString(5, item.getStatus());
            ps.setInt(6, id);

            int affected = ps.executeUpdate();
            if (affected > 0) {
                return true;
            } else {
                message.setMessage("No affected query. No item update");
            }
        } catch (SQLException e) {
            message.setMessage("Something wrong on update item");
            e.printStackTrace();
        }
        return false;
    }
}
