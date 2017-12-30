package model.uid;

import util.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserIdentityDAO {
    private Connection connection;

    public List<UserIdentity> getAll() throws SQLException {
        List<UserIdentity> userIdentities = new ArrayList<>();

        connection = DataSource.getConnection();
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

        return userIdentities;
    }

    public List<UserIdentity> find(String filter) throws SQLException {
        List<UserIdentity> userIdentities = new ArrayList<>();
        connection = DataSource.getConnection();
        String query = "SELECT * FROM users_identity WHERE " + filter;
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            UserIdentity user = new UserIdentity(rs.getLong("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role"));
            userIdentities.add(user);
        }

        rs.close();
        statement.close();
        connection.close();

        return userIdentities;
    }

    public long create(UserIdentity newUserIdentity) throws SQLException {
        connection = DataSource.getConnection();
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

        long newId = newUserIdentity.getId();
        System.out.println("Successfully created UserIdentity with id = " + newId);
        return newId;
    }

    public void update(UserIdentity userIdentity) throws SQLException {
        connection = DataSource.getConnection();
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
    }

    public void delete(long id) throws SQLException {
        connection = DataSource.getConnection();
        String query = "DELETE FROM users_identity WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setLong(1, id);

        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();

        System.out.println("Successfully deleted UserIdentity with id = " + id);
    }
}
