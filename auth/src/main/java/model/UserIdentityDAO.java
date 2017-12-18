package model;

import util.MySQLConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserIdentityDAO {
    private Connection connection;

    public List<UserIdentity> getAllUserIdentity() {
        List<UserIdentity> userIdentities = new ArrayList<>();

        try {
            connection = MySQLConnection.getConnection();
            String query = "SELECT * FROM users_identity";
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                UserIdentity user = new UserIdentity(rs.getLong("id"),
                                                     rs.getString("username"),
                                                     rs.getString("password"),
                                                     rs.getString("role"));
                userIdentities.add(user);
            }

            rs.close();
            stmt.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return userIdentities;
    }
}
