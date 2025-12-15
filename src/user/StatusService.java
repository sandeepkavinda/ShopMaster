/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package user;

import constants.UserStatusConstants;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import model.MySQL;
import panels.UserManagement;

/**
 *
 * @author HP
 */
public class StatusService {

    public static void deactivateUser(String username, UserManagement userManagement) throws Exception {

        int result = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to Deactivate This User?",
                "Confirm Password Reset",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            MySQL.execute("UPDATE user SET user_status_id='" + UserStatusConstants.DEACTIVE + "' WHERE username = '" + username + "'");

            if (userManagement != null) {
                userManagement.loadUserTable();
            }
        }

    }

    public static void activateUser(String username, UserManagement userManagement) throws Exception {

        int result = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to Activate This User?",
                "Confirm Password Reset",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            MySQL.execute("UPDATE user SET user_status_id='" + UserStatusConstants.ACTIVE + "' WHERE username = '" + username + "'");

            if (userManagement != null) {
                userManagement.loadUserTable();
            }
        }

    }
}
