package model;

import exception.BadDataException;
import exception.DatabaseFailureException;
import exception.FailedCRUDOperationException;
import util.MySQLConnection;

import java.sql.*;
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
        }

        return userIdentities;
    }

    public void createUserIdentity(UserIdentity newUserIdentity) throws FailedCRUDOperationException {
        try {
            connection = MySQLConnection.getConnection();
            String query = "INSERT INTO users_identity" +
                    "(id, `username`, `password`, role) VALUES" +
                    "(?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setLong(1, newUserIdentity.getId());
            preparedStatement.setString(2, newUserIdentity.getUsername());
            preparedStatement.setString(3, newUserIdentity.getPassword());
            preparedStatement.setString(4, newUserIdentity.getRole());

            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();

            System.out.println("Successfully created UserIdentity.");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: " + e.getMessage());
            throw new BadDataException(e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            throw new DatabaseFailureException(e.getMessage());
        }
    }

    public void updateUserIdentity(UserIdentity userIdentity) throws FailedCRUDOperationException {
        try {
            connection = MySQLConnection.getConnection();
            String query = "UPDATE users_identity SET" +
                    "`username` = ?, `password` = ?, role = ?" +
                    "WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, userIdentity.getUsername());
            preparedStatement.setString(2, userIdentity.getPassword());
            preparedStatement.setString(3, userIdentity.getRole());
            preparedStatement.setLong(4, userIdentity.getId());

            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();

            System.out.println("Successfully updated UserIdentity with id = " + userIdentity.getId());
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: " + e.getMessage());
            throw new BadDataException(e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            throw new DatabaseFailureException(e.getMessage());
        }
    }
}
