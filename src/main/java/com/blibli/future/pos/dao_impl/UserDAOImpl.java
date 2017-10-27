package com.blibli.future.pos.dao_impl;

import com.blibli.future.pos.dao.UserDAO;
import com.blibli.future.pos.entity.User;
import com.blibli.future.pos.util.JDBCConn;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDAOImpl implements UserDAO {
    private Map<Long, User> users = new HashMap<Long, User>();
    private Connection connection;

    public List<User> getAllUsers() {
        try {
            connection = JDBCConn.getConnection();
            Statement stmt = connection.createStatement();
            String query = "SELECT * FROM users;";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                User newUser = new User(rs.getLong("ID"),
                                        rs.getLong("restaurant_id"),
                                        rs.getString("name"),
                                        rs.getString("password"),
                                        rs.getString("email"),
                                        rs.getString("role"));
                users.put(newUser.getId(), newUser);
            }

            rs.close();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return new ArrayList<User>(users.values());
    }

    public User getUser(Long id) {
        return users.get(id);
    }

    public boolean createUser(User user) {
        try {
            connection = JDBCConn.getConnection();
            String query = "INSERT INTO users (restaurant_id, name, password, email, role)" +
                            "VALUES (?, ?, ?, ?, ?);";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setLong(1, user.getRestaurant_id());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getRole());
            pstmt.executeUpdate();
            pstmt.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void updateUser(User user) {
        try {
            connection = JDBCConn.getConnection();
            String query = "UPDATE users" +
                            "SET restaurant_id = ? name = ?, password = ?," +
                            "email = ?, role = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setLong(1, user.getRestaurant_id());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getRole());
            pstmt.executeUpdate();
            pstmt.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteUserById(Long id) {

    }
}
