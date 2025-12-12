package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.*;

public class MySQL {

    private static Connection connection;

    static {
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/shopmaster", 
                    "root", 
                    "Sandeep/Root/1234"
            );
            connection.setAutoCommit(true);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public static Connection getConnection() {
        return connection;
    }

    public static ResultSet execute(String query) throws Exception {

        Statement statement = connection.createStatement();

        if (query.trim().toUpperCase().startsWith("SELECT")) {
            return statement.executeQuery(query);
        } else {
            statement.executeUpdate(query);
            return null;
        }
    }
}
