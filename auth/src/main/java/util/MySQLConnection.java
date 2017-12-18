package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySQLConnection {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://geekstudio.id:3306/peregrine_identity?useSSL=false";

    //  Database credentials
    static final String USER = "peregrine";
    static final String PASS = "pausterbang";

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
