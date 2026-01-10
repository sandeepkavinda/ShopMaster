/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 *
 * @author HP
 */
public class BigDecimalFormatter {

    public static String formatQuantity(BigDecimal quantity) {
        if (quantity == null) {
            return "0";
        }

        // Normalize value
        BigDecimal value = quantity.setScale(3, RoundingMode.HALF_UP).stripTrailingZeros();

        DecimalFormat df;

        if (value.scale() <= 0) {
            // Whole number
            df = new DecimalFormat("#,##0");
        } else {
            // Up to 3 decimals
            df = new DecimalFormat("#,##0.###");
        }

        return df.format(value);
    }

    public static String formatPrice(BigDecimal price) {
        if (price == null) {
            return "0.00";
        }

        DecimalFormat df = new DecimalFormat("#,##0.00");
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);

        // Ensure price is scaled to 2 decimals
        price = price.setScale(2, RoundingMode.HALF_UP);

        return df.format(price);
    }

}
