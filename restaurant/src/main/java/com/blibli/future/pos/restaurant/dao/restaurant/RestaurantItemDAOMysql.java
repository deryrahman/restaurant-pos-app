package com.blibli.future.pos.restaurant.dao.restaurant;

import com.blibli.future.pos.restaurant.common.MysqlDAO;
import com.blibli.future.pos.restaurant.common.model.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class RestaurantItemDAOMysql extends MysqlDAO<Item> implements RestaurantItemDAO{

    @Override
    protected void mappingObject(Item item, ResultSet rs) throws SQLException {
        item.setId(rs.getInt("items.id"));
        item.setName(rs.getString("name"));
        item.setTimestampCreated(rs.getTimestamp("items.timestamp_created"));
        item.setPrice(rs.getBigDecimal("price"));
        item.setDescription(rs.getString("description"));
        item.setCategoryId(rs.getInt("category_id"));
        item.setStatus(rs.getString("status"));
//        item.setRestaurantId(rs.getInt("restaurants.id"));
//        item.setStock(rs.getInt("stock"));
//        item.autoSetHref();
    }
    @Override
    public void create(int itemId, int restaurantId, int stock) throws SQLException {
        String query = "INSERT INTO restaurant_item(restaurant_id, item_id, stock)" +
                " VALUES(?, ?, ?)";
        ps = conn.prepareStatement(query);
        ps.setInt(1, restaurantId);
        ps.setInt(2, itemId);
        ps.setInt(3, stock);

        int affected = ps.executeUpdate();
        if (affected <= 0) {
            throw new SQLException("No affected query. No restaurantItem inserting");
        }
    }

    @Override
    public List<Item> getBulk(String filter) throws SQLException {
        List<Item> items = new ArrayList<>();
        String query = "SELECT items.*, restaurants.id, restaurant_item.stock as stock " +
                "FROM restaurants JOIN restaurant_item JOIN items " +
                "WHERE restaurants.id=restaurant_item.restaurant_id AND items.id=restaurant_item.item_id AND " +
                filter;
        ps = conn.prepareStatement(query);

        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            Item item = new Item();
            mappingObject(item, rs);
            items.add(item);
        }
        return items;
    }

    @Override
    public void update(int itemId, int restaurantId, int stock) throws SQLException {
        String query = "UPDATE restaurant_item SET " +
                "stock = ? +" +
                "WHERE restaurant_id = ?" +
                "AND item_id = ?";
        ps = conn.prepareStatement(query);
        ps.setInt(1, stock);
        ps.setInt(2, restaurantId);
        ps.setInt(3, itemId);

        int affected = ps.executeUpdate();
        if (affected <= 0) {
            throw new SQLException("No affected query. No restaurant com.blibli.future.pos.restaurant.item update");
        }
    }
}
