/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import model.MySQL;
import java.sql.ResultSet;

/**
 *
 * @author HP
 */
public class IdGenerater {

    private static final int TOTAL_LENGTH = 10;

    public static String generateId(String table, String columnName, String prefix) {
        String query = "SELECT " + columnName + " FROM " + table + " ORDER BY " + columnName + " DESC LIMIT 1";

        try {
            ResultSet rs = MySQL.execute(query);

            if (!rs.next()) {
                int numLength = TOTAL_LENGTH - prefix.length();
                return prefix + String.format("%0" + numLength + "d", 1);
            } else {
                String lastId = rs.getString(columnName);
                int numericPart = Integer.parseInt(lastId.substring(prefix.length()));
                int newNumericPart = numericPart + 1;

                int numLength = TOTAL_LENGTH - prefix.length();
                return prefix + String.format("%0" + numLength + "d", newNumericPart);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
