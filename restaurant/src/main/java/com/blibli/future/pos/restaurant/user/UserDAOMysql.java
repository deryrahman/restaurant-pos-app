package com.blibli.future.pos.restaurant.user;


import com.blibli.future.pos.restaurant.common.MysqlDAO;
import com.blibli.future.pos.restaurant.common.TransactionUtility;
import com.blibli.future.pos.restaurant.common.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAOMysql extends MysqlDAO implements UserDAO{

    private void setItemFromQuery(User user, ResultSet rs) throws SQLException {
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setTimestampCreated(rs.getTimestamp("timestamp_created"));
        user.setRestaurantId(rs.getInt("restaurant_id"));
        user.setEmail(rs.getString("email"));
        user.setRole(rs.getString("role"));
        user.autoSetHref();
    }

    @Override
    public boolean create(User user) {
        try {
            String query = "INSERT INTO users(name, restaurant_id, email, role)" +
                    " VALUES(?, ?, ?, ?)";
            ps = TransactionUtility.getConnection().prepareStatement(query);
            ps.setString(1, user.getName());
            ps.setInt(2, user.getRestaurantId());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getRole());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                return true;
            } else {
                message.setMessage("No affected query. No user inserting");
            }
        } catch (SQLException e) {
            message.setMessage("Something wrong on create user");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public User getById(int id) {
        User user = new User();
        try {
            String query = "SELECT * FROM users WHERE id = ?";
            ps = TransactionUtility.getConnection().prepareStatement(query);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            rs.next();
            setItemFromQuery(user, rs);
            return user;
        } catch (SQLException e) {
            message.setMessage("Something wrong on get user");
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public List<User> getBulk(String filter) {
        List<User> users = new ArrayList<>();
        try {
            String query = "SELECT * FROM users WHERE "+filter;
            ps = TransactionUtility.getConnection().prepareStatement(query);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                User user = new User();
                setItemFromQuery(user, rs);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            message.setMessage("Something wrong on getBulk users");
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public boolean delete(int id) {
        try {
            String query = "DELETE FROM users WHERE id = ?";
            ps = TransactionUtility.getConnection().prepareStatement(query);
            ps.setInt(1, id);

            int affected = ps.executeUpdate();
            if (affected > 0) {
                return true;
            } else {
                message.setMessage("No affected query. No user deleted");
            }
        } catch (SQLException e) {
            message.setMessage("Something wrong on delete user");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(int id, User user) {
        try {
            String query = "UPDATE users SET " +
                    "name = ?, " +
                    "restaurant_id = ?," +
                    "email = ?," +
                    "role = ?" +
                    "WHERE id = ?";
            ps = TransactionUtility.getConnection().prepareStatement(query);
            ps.setString(1, user.getName());
            ps.setInt(2, user.getRestaurantId());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getRole());
            ps.setInt(5, id);

            int affected = ps.executeUpdate();
            if (affected > 0) {
                return true;
            } else {
                message.setMessage("No affected query. No user update");
            }
        } catch (SQLException e) {
            message.setMessage("Something wrong on update user");
            e.printStackTrace();
        }
        return false;
    }
}
