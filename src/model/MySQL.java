package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MySQL {

    private static Connection conncetion;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conncetion = DriverManager.getConnection("jdbc:mysql://localhost:3306/shopmaster", "root", "Sandeep/Root/1234");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ResultSet execute(String query) throws Exception {

        Statement statement = conncetion.createStatement();

        if (query.startsWith("SELECT") | query.startsWith("select")) {
            return statement.executeQuery(query);
        } else {
            statement.executeUpdate(query);
            return null;
        }
    }
}
