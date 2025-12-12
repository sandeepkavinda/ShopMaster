/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import DTO.VerificationCodeData;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import model.MySQL;

/**
 *
 * @author HP
 */
public class PasswordUtils {

    public static VerificationCodeData generateVerificationCode() {

        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(30);
        Timestamp expiryTimestamp = Timestamp.valueOf(expiresAt);

        SecureRandom random = new SecureRandom();
        String verificationCode = String.valueOf(100000 + random.nextInt(900000));

        return new VerificationCodeData(verificationCode, expiryTimestamp);
    }

    public static void resetPassword(String username) throws Exception {

        VerificationCodeData verificationCodeData = generateVerificationCode();
        MySQL.execute("UPDATE user SET is_verified = 0, "
                + "verification_code='" + verificationCodeData.getVerificationCode() + "', "
                + "verification_code_expiry='" + verificationCodeData.getExpiryTimestamp() + "', "
                + "password = NULL "
                + "WHERE username='" + username + "' ");

    }

}
