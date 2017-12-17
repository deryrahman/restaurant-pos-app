package com.blibli.future.pos.restaurant.dao.restaurant;

import com.blibli.future.pos.restaurant.common.MysqlDAO;
import com.blibli.future.pos.restaurant.common.TransactionUtility;
import com.blibli.future.pos.restaurant.common.model.Restaurant;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class RestaurantDAOMysql extends MysqlDAO<Restaurant> implements RestaurantDAO {

    @Override
    protected void mappingObject(Restaurant restaurant, ResultSet rs) throws SQLException {
        restaurant.setId(rs.getInt("id"));
        restaurant.setTimestampCreated(rs.getTimestamp("timestamp_created"));
        restaurant.setAddress(rs.getString("address"));
        restaurant.setPhone(rs.getString("phone"));
        restaurant.autoSetHref();
    }

    @Override
    public void create(Restaurant restaurant) throws SQLException {
        String query = "INSERT INTO restaurants(address, phone)" +
                " VALUES(?, ?)";
        ps = TransactionUtility.getConnection().prepareStatement(query);
        ps.setString(1, restaurant.getAddress());
        ps.setString(2, restaurant.getPhone());

        int affected = ps.executeUpdate();
        if (affected >= 0) {
            throw new SQLException("No affected query. No restaurant inserting");
        }
    }

    @Override
    public Restaurant getById(int id) throws SQLException {
        Restaurant restaurant = new Restaurant();
        String query = "SELECT * FROM restaurants WHERE id = ?";
        ps = TransactionUtility.getConnection().prepareStatement(query);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        mappingObject(restaurant, rs);
        return restaurant;
    }

    @Override
    public List<Restaurant> getBulk(String filter) throws SQLException {
        List<Restaurant> restaurants = new ArrayList<>();
        String query = "SELECT * FROM restaurants WHERE "+filter;
        ps = TransactionUtility.getConnection().prepareStatement(query);

        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            Restaurant restaurant= new Restaurant();
            mappingObject(restaurant, rs);
            restaurants.add(restaurant);
        }
        return restaurants;
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM restaurants WHERE id = ?";
        ps = TransactionUtility.getConnection().prepareStatement(query);
        ps.setInt(1, id);

        int affected = ps.executeUpdate();
        if (affected <= 0) {
            throw new SQLException("No affected query. No restaurant deleted");
        }
    }

    @Override
    public void update(int id, Restaurant restaurant) throws SQLException {
        String query = "UPDATE categories SET id = ?, address = ?, phone = ? WHERE id = ?";
        ps = TransactionUtility.getConnection().prepareStatement(query);
        ps.setString(1, restaurant.getAddress());
        ps.setString(2, restaurant.getPhone());
        ps.setInt(3, id);

        int affected = ps.executeUpdate();
        if (affected <= 0) {
            throw new SQLException("No affected query. No restaurant update");
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
    public List<Restaurant> getAllRestaurantByItemId(int itemId, String filter) throws SQLException {
        List<Restaurant> restaurants = new ArrayList<>();
        String query = "SELECT items.*, restaurants.id, restaurant_item.stock as stock " +
                "FROM restaurants JOIN restaurant_item JOIN items " +
                "WHERE items.id=restaurant_item.item_id AND items.id=" +itemId+" AND "+
                filter;
        ps = TransactionUtility.getConnection().prepareStatement(query);

        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            Restaurant restaurant = new Restaurant();
            mappingObject(restaurant, rs);
            restaurants.add(restaurant);
        }
        return restaurants;
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
