package com.blibli.future.pos.restaurant.dao.item;

import com.blibli.future.pos.restaurant.common.MysqlDAO;
import com.blibli.future.pos.restaurant.common.TransactionUtility;
import com.blibli.future.pos.restaurant.common.model.Item;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class ItemDAOMysql extends MysqlDAO<Item> implements ItemDAO{
    
    @Override
    protected void mappingObject(Item item, ResultSet rs) throws SQLException {
        item.setId(rs.getInt("id"));
        item.setName(rs.getString("name"));
        item.setTimestampCreated(rs.getTimestamp("timestamp_created"));
        item.setPrice(rs.getBigDecimal("price"));
        item.setDescription(rs.getString("description"));
        item.setCategoryId(rs.getInt("category_id"));
        item.setStatus(rs.getString("status"));
//        item.autoSetHref();
    }

    @Override
    public void create(Item item) throws SQLException {
        String query = "INSERT INTO items(name, price, description, category_id, status)" +
                " VALUES(?, ?, ?, ?, ?)";
        ps = TransactionUtility.getConnection().prepareStatement(query);
        ps.setString(1, item.getName());
        ps.setBigDecimal(2, item.getPrice());
        ps.setString(3, item.getDescription());
        ps.setInt(4, item.getCategoryId());
        ps.setString(5, item.getStatus());

        int affected = ps.executeUpdate();
        if (affected > 0) {
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                item.setId(rs.getInt(1));
            }
        } else {
            throw new SQLException("No affected query. No com.blibli.future.pos.restaurant.item inserting");
        }
    }

    @Override
    public Item getById(int id) throws SQLException {
        Item item = new Item();
        String query = "SELECT * FROM items WHERE id = ?";
        ps = TransactionUtility.getConnection().prepareStatement(query);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        mappingObject(item, rs);
        return item;
    }

    @Override
    public List<Item> getBulk(String filter) throws SQLException {
        List<Item> items = new ArrayList<>();
        String query = "SELECT * FROM items WHERE "+filter;
        ps = TransactionUtility.getConnection().prepareStatement(query);

        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            Item item = new Item();
            mappingObject(item, rs);
            items.add(item);
        }
        return items;
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM items WHERE id = ?";
        ps = TransactionUtility.getConnection().prepareStatement(query);
        ps.setInt(1, id);

        int affected = ps.executeUpdate();
        if (affected <= 0) {
            throw new SQLException("No affected query. No com.blibli.future.pos.restaurant.item deleted");
        }
    }

    @Override
    public void update(int id, Item item) throws SQLException {
        String query = "UPDATE items SET " +
                "name = ?, " +
                "price = ?," +
                "description = ?," +
                "category_id = ?," +
                "status = ? +" +
                "WHERE id = ?";
        ps = TransactionUtility.getConnection().prepareStatement(query);
        ps.setString(1, item.getName());
        ps.setBigDecimal(2, item.getPrice());
        ps.setString(3, item.getDescription());
        ps.setInt(4, item.getCategoryId());
        ps.setString(5, item.getStatus());
        ps.setInt(6, id);

        int affected = ps.executeUpdate();
        if (affected <= 0) {
            throw new SQLException("No affected query. No com.blibli.future.pos.restaurant.item update");
        }
    }

    @Override
    public void addRelationRestaurantItem(int itemId, int restaurantId, int stock) throws SQLException {
        String query = "INSERT INTO restaurant_item(restaurant_id, item_id, stock)" +
                " VALUES(?, ?, ?)";
        ps = TransactionUtility.getConnection().prepareStatement(query);
        ps.setInt(1, restaurantId);
        ps.setInt(2, itemId);
        ps.setInt(3, stock);

        int affected = ps.executeUpdate();
        if (affected >= 0) {
            throw new SQLException("No affected query. No com.blibli.future.pos.restaurant.item inserting");
        }
    }

    @Override
    public List<Item> getAllItemByRestaurantId(int restaurantId, String filter) throws SQLException {
        List<Item> items = new ArrayList<>();
        String query = "SELECT items.*, restaurants.id, restaurant_item.stock as stock " +
                "FROM restaurants JOIN restaurant_item JOIN items " +
                "WHERE restaurants.id=restaurant_item.restaurant_id AND restaurants.id=" +restaurantId+" AND "+
                filter;
        ps = TransactionUtility.getConnection().prepareStatement(query);

        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            Item restaurant = new Item();
            mappingObject(restaurant, rs);
            items.add(restaurant);
        }
        return items;
    }

    @Override
    public void deleteRelationRestaurantItem(int itemId, int restaurantId) throws SQLException {
        String query = "DELETE FROM restaurant_item WHERE restaurant_id = ? AND item_id = ?";
        ps = TransactionUtility.getConnection().prepareStatement(query);
        ps.setInt(1, restaurantId);
        ps.setInt(2, itemId);

        int affected = ps.executeUpdate();
        if (affected <= 0) {
            throw new SQLException("No affected query. No com.blibli.future.pos.restaurant.item deleted");
        }
    }

    @Override
    public void updateRelationRestaurantItem(int itemId, int restaurantId, int stock) throws SQLException {
        String query = "UPDATE restaurant_item SET stock = ? WHERE restaurant_id = ? AND item_id = ?";
        ps = TransactionUtility.getConnection().prepareStatement(query);
        ps.setInt(1, stock);
        ps.setInt(2, restaurantId);
        ps.setInt(3, itemId);

        int affected = ps.executeUpdate();
        if (affected <= 0) {
            throw new SQLException("No affected query. No com.blibli.future.pos.restaurant.item stock update");
        }
    }
}
