package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySQLConnection {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = MySQLConfiguration.JDBC_DRIVER;
    static final String DB_URL = MySQLConfiguration.DB_URL;

    //  Database credentials
    static final String USER = MySQLConfiguration.USER;
    static final String PASS = MySQLConfiguration.PASS;

    static Connection connection = null;

    public static Connection getConnection(){
        try {
            Class.forName(JDBC_DRIVER).newInstance();
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
