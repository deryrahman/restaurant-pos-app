package com.blibli.future.pos.dao_impl;

import com.blibli.future.pos.dao.UserDAO;
import com.blibli.future.pos.entity.User;
import com.blibli.future.pos.util.JDBCConn;
import com.blibli.future.pos.util.JDBCConnPostgre;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDAOImpl implements UserDAO {
    private Connection connection;

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try {
            connection = JDBCConn.getConnection();
            Statement stmt = connection.createStatement();
            String query = "SELECT * FROM users;";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                User newUser = resultSetToUser(rs);
                users.add(newUser);
            }

            rs.close();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return users;
    }

    public User getUserById(Long id) {
        User user = new User();
        try {
            connection = JDBCConn.getConnection();
            Statement stmt = connection.createStatement();
            String query = "SELECT * FROM users WHERE `ID` = " + id;
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                user = resultSetToUser(rs);
            }

            rs.close();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return user;
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
                            "SET restaurant_id = ?, `name` = ?, `password` = ?," +
                            "`email` = ?, `role` = ? WHERE `id` = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setLong(1, user.getRestaurant_id());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getRole());
            pstmt.setLong(6, user.getId());
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

    private User resultSetToUser(ResultSet rs) throws SQLException {
        return new User(rs.getLong("ID"),
                rs.getLong("restaurant_id"),
                rs.getString("name"),
                rs.getString("password"),
                rs.getString("email"),
                rs.getString("role"));
    }
}
