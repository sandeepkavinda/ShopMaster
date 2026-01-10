package model;

import java.math.BigDecimal;
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

    // 🔹 Prepare statement (Safe for all queries)
    public static PreparedStatement prepare(String sql) throws SQLException {
        return connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    }

    // 🔹 SELECT queries
    public static ResultSet executeQuery(String sql, Object... params) throws SQLException {
        PreparedStatement ps = prepare(sql);
        setParameters(ps, params);
        return ps.executeQuery();
    }

    // 🔹 INSERT / UPDATE / DELETE
    public static int executeUpdate(String sql, Object... params) throws SQLException {
        PreparedStatement ps = prepare(sql);
        setParameters(ps, params);
        return ps.executeUpdate();
    }

    // 🔹 Transaction control
    public static void beginTransaction() throws SQLException {
        connection.setAutoCommit(false);
    }

    public static void commit() throws SQLException {
        connection.commit();
        connection.setAutoCommit(true);
    }

    public static void rollback() {
        try {
            connection.rollback();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔹 Parameter binder (Prevents SQL Injection)
    public static void setParameters(PreparedStatement ps, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];

            if (param == null) {
                ps.setNull(i + 1, Types.NULL);
            } else if (param instanceof Integer) {
                ps.setInt(i + 1, (Integer) param);
            } else if (param instanceof Long) {
                ps.setLong(i + 1, (Long) param);
            } else if (param instanceof String) {
                ps.setString(i + 1, (String) param);
            } else if (param instanceof BigDecimal) {
                ps.setBigDecimal(i + 1, (BigDecimal) param);
            } else if (param instanceof Timestamp) {
                ps.setTimestamp(i + 1, (Timestamp) param);
            } else if (param instanceof java.util.Date) {
                ps.setTimestamp(i + 1, new Timestamp(((java.util.Date) param).getTime()));
            } else {
                ps.setObject(i + 1, param);
            }
        }
    }

}
