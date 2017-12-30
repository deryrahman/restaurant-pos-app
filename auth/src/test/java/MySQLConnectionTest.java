import util.MySQLConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLConnectionTest {

    public static void main(String[] args) {
        Connection connection = null;
        try {
            connection = MySQLConnection.getConnection();
            String query = "SELECT * FROM users_identity";
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                System.out.println(rs.getString("username"));
            }

            rs.close();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
