package com.blibli.future.pos.restaurant.dao.restaurant;

import com.blibli.future.pos.restaurant.dao.MysqlDAO;
import com.blibli.future.pos.restaurant.common.TransactionHelper;
import com.blibli.future.pos.restaurant.common.model.Restaurant;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class RestaurantDAOMysql extends MysqlDAO<Restaurant> implements RestaurantDAO {
    private Restaurant restaurant;
    private List<Restaurant> restaurants;

    @Override
    public void mappingObject(Restaurant restaurant, ResultSet rs) throws SQLException {
        restaurant.setId(rs.getInt("id"));
        restaurant.setTimestampCreated(rs.getTimestamp("timestamp_created"));
        restaurant.setAddress(rs.getString("address"));
        restaurant.setPhone(rs.getString("phone"));
    }

    @Override
    public void create(Restaurant restaurant) throws SQLException {
        String query = "INSERT INTO restaurants(address, phone)" +
                " VALUES(?, ?)";
        ps = TransactionHelper.getConnection().prepareStatement(query);
        ps.setString(1, restaurant.getAddress());
        ps.setString(2, restaurant.getPhone());

        int affected = ps.executeUpdate();
        if (affected >= 0) {
            throw new SQLException("No affected query. No restaurant inserting");
        }
    }

    @Override
    public Restaurant findById(int id) throws SQLException {
        restaurant = new Restaurant();
        restaurants = find("id = "+id);
        if(restaurants.size()>0){
            restaurant = restaurants.get(0);
        }
        return restaurant;
    }

    @Override
    public List<Restaurant> find(String filter) throws SQLException {
        restaurants = new ArrayList<>();
        String query = "SELECT * FROM restaurants WHERE "+filter;
        ps = TransactionHelper.getConnection().prepareStatement(query);

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
        ps = TransactionHelper.getConnection().prepareStatement(query);
        ps.setInt(1, id);

        int affected = ps.executeUpdate();
        if (affected <= 0) {
            throw new SQLException("No affected query. No restaurant deleted");
        }
    }

    @Override
    public void update(int id, Restaurant restaurant) throws SQLException {
        String query = "UPDATE categories SET id = ?, address = ?, phone = ? WHERE id = ?";
        ps = TransactionHelper.getConnection().prepareStatement(query);
        ps.setString(1, restaurant.getAddress());
        ps.setString(2, restaurant.getPhone());
        ps.setInt(3, id);

        int affected = ps.executeUpdate();
        if (affected <= 0) {
            throw new SQLException("No affected query. No restaurant update");
        }
    }
}
