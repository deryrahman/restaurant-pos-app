package com.blibli.future.pos.restaurant.dao.user;


import com.blibli.future.pos.restaurant.dao.MysqlDAO;
import com.blibli.future.pos.restaurant.common.TransactionHelper;
import com.blibli.future.pos.restaurant.common.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAOMysql extends MysqlDAO<User> implements UserDAO {
    private List<User> users;
    private User user;

    @Override
    protected void mappingObject(User user, ResultSet rs) throws SQLException {
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setTimestampCreated(rs.getTimestamp("timestamp_created"));
        user.setRestaurantId(rs.getInt("restaurant_id"));
        user.setEmail(rs.getString("email"));
        user.setRole(rs.getString("role"));
    }

    @Override
    public void create(User user) throws SQLException {
        String query = "INSERT INTO users(name, restaurant_id, email, role)" +
                " VALUES(?, ?, ?, ?)";
        ps = TransactionHelper.getConnection().prepareStatement(query);
        ps.setString(1, user.getName());
        ps.setInt(2, user.getRestaurantId());
        ps.setString(3, user.getEmail());
        ps.setString(4, user.getRole());

        int affected = ps.executeUpdate();
        if (affected > 0) {
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                user.setId(rs.getInt(1));
            }
        } else {
            throw new SQLException("No affected query. No user inserting");
        }
    }

    @Override
    public User findById(int id) throws SQLException  {
        user = new User();
        users = find("id = "+id);
        if(users.size()>0){
            user = users.get(0);
        }
        return user;
    }

    @Override
    public User findById(String id) throws SQLException {
        user = new User();
        users = find("email = '"+ id + "'");
        if(users.size()>0){
            user = users.get(0);
        }
        return user;
    }

    @Override
    public List<User> find(String filter) throws SQLException  {
        users = new ArrayList<>();
        String query = "SELECT * FROM users WHERE "+filter;
        ps = TransactionHelper.getConnection().prepareStatement(query);

        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            User user = new User();
            mappingObject(user, rs);
            users.add(user);
        }
        return users;
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM users WHERE id = ?";
        ps = TransactionHelper.getConnection().prepareStatement(query);
        ps.setInt(1, id);

        int affected = ps.executeUpdate();
        if (affected <= 0) {
            throw new SQLException("No affected query. No user deleted");
        }
    }

    @Override
    public void update(int id, User user) throws SQLException {
        String query = "UPDATE users SET " +
                "name = ?, " +
                "restaurant_id = ?," +
                "email = ?," +
                "role = ? " +
                "WHERE id = ?";
        ps = TransactionHelper.getConnection().prepareStatement(query);
        ps.setString(1, user.getName());
        ps.setInt(2, user.getRestaurantId());
        ps.setString(3, user.getEmail());
        ps.setString(4, user.getRole());
        ps.setInt(5, id);

        int affected = ps.executeUpdate();
        if (affected <= 0) {
            throw new SQLException("No affected query. No user update");
        }
    }
}
