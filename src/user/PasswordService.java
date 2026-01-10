/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package user;

import DTO.VerificationCodeDataDTO;
import SubGUI.UserOtpDetails;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.swing.JOptionPane;
import model.MySQL;
import panels.UserManagement;

/**
 *
 * @author HP
 */
public class PasswordService {

    public static VerificationCodeDataDTO generateVerificationCode() {

        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(30);
        Timestamp expiryTimestamp = Timestamp.valueOf(expiresAt);

        SecureRandom random = new SecureRandom();
        String verificationCode = String.valueOf(100000 + random.nextInt(900000));

        return new VerificationCodeDataDTO(verificationCode, expiryTimestamp);
    }
    
    
    public static void resetUserPassword(String username, UserManagement userManagement) throws Exception {
        int result = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to reset this user's password?",
                "Confirm Password Reset",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {

            VerificationCodeDataDTO verificationCodeData = generateVerificationCode();
            MySQL.execute("UPDATE user SET is_verified = 0, "
                    + "verification_code='" + verificationCodeData.getVerificationCode() + "', "
                    + "verification_code_expiry='" + verificationCodeData.getExpiryTimestamp() + "', "
                    + "password = NULL "
                    + "WHERE username='" + username + "' ");

            if (userManagement != null) {
                userManagement.loadUserTable();
            }

            new UserOtpDetails(null, true, username);

        }
    }

}
