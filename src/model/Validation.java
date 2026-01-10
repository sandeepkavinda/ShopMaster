package model;

import java.math.BigDecimal;
import javax.swing.JOptionPane;

public class Validation {

    public static boolean isEmailVaild(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    public static boolean isPasswordVaild(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$");

    }

    public static boolean isDouble(String text) {
        return text.matches("^\\d+(\\.\\d{2})?$");
    }

    public static boolean isInteger(String text) {
        return text.matches("^\\d+$");
    }

    public static boolean isValidMobile(String mobile) {
        return mobile.matches("^07[01245678]{1}[0-9]{7}$");
    }

    public static boolean isValidMobileBasic(String input) {
        if (input.equals("-")) {
            return true;
        } else {
            // Check if the string is not null, is numeric, and has a length == 10
            return input != null && input.matches("\\d+") && input.length() == 10;
        }

    }

    public static boolean isValidQuantity(String value) {
        // Regular expression for an integer or double (positive or negative)
        String numberRegex = "^[+-]?\\d+(\\.\\d+)?$";

        // Check if the value matches the regex pattern
        return value.matches(numberRegex);
    }

    public static boolean isValidPrice(String price) {
        if (price == null || price.isEmpty()) {
            return false; // Null or empty strings are invalid
        }

        // Regular expression to match valid price formats
        String pricePattern = "^\\d+(\\.\\d{1,2})?$";
        return price.matches(pricePattern);
    }

    public static boolean isValidBigDecimal(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        try {
            new BigDecimal(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
