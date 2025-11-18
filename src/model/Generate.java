/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Random;

/**
 *
 * @author Sandeep
 */
public class Generate {
    
    // Static method to generate a 13-digit barcode as a string
    public static String GenerateBarcode() {
        StringBuilder barcode = new StringBuilder();
        Random random = new Random();

        // Generate the first 12 digits randomly
        for (int i = 0; i < 12; i++) {
            barcode.append(random.nextInt(10)); // Append a random digit (0-9)
        }

        // Calculate the check digit and append it to complete the 13-digit barcode
        int checkDigit = CalculateCheckDigit(barcode.toString());
        barcode.append(checkDigit);

        return barcode.toString(); // Return the 13-digit barcode as a string
    }

    // Static method to calculate the EAN-13 check digit
    private static int CalculateCheckDigit(String barcode) {
        int sum = 0;

        // Loop through the 12-digit barcode
        for (int i = 0; i < barcode.length(); i++) {
            int digit = Character.getNumericValue(barcode.charAt(i));
            if (i % 2 == 0) {
                sum += digit; // Add odd-positioned digits as they are
            } else {
                sum += digit * 3; // Multiply even-positioned digits by 3
            }
        }

        // Calculate the check digit (round up to the nearest 10)
        int remainder = sum % 10;
        return remainder == 0 ? 0 : 10 - remainder;
    }
  
    
//    public static String generateNextGrnBarcode(String lastBarcode) throws Exception {
//        // Parse the string as an integer
//        int number = Integer.parseInt(lastBarcode);
//        
//        //If Number is 0 it convert to 50000000 to first barcode generate
//        if(number==0){
//            number = 50000000;
//        }
//
//        // Increment the number
//        number++;
//
//        if (number > 50000000 && number < 100000000) {
//            // Format the incremented number to the same length as the input
//            return String.format("%0" + lastBarcode.length() + "d", number);
//
//        } else {
//            throw new Exception("Barcods are out of the valid range. Contact Developers");
//        }
//    }
//    
//     public static String generateNextInvoiceBarcode(String lastBarcode) throws Exception {
//        // Parse the string as an integer
//        int number = Integer.parseInt(lastBarcode);
//    
//        // Increment the number
//        number++;
//
//        if (number > 0 && number < 50000000) {
//            // Format the incremented number to the same length as the input
//            return String.format("%08d", number);
//        } else {
//            throw new Exception("Barcods are out of the valid range. Contact Developers");
//        }
//    }
//    
    
}
