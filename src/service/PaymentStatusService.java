/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author HP
 */
public class PaymentStatusService {

    public static String getPaymentStatus(BigDecimal grnTotal, BigDecimal paidAmount) {

        if (grnTotal == null || paidAmount == null) {
            return "UNPAID";
        }

        // Normalize scale
        grnTotal = grnTotal.setScale(2, RoundingMode.HALF_UP);
        paidAmount = paidAmount.setScale(2, RoundingMode.HALF_UP);

        if (paidAmount.compareTo(BigDecimal.ZERO) == 0) {
            return "UNPAID";
        }

        if (paidAmount.compareTo(grnTotal) >= 0) {
            return "PAID";
        }

        return "PARTIAL";
    }

}
