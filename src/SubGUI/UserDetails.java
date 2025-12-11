/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package SubGUI;

import DTO.UserEditableData;
import java.awt.Color;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import model.MySQL;
import panels.UserManagement;

/**
 *
 * @author Sandeep
 */
public class UserDetails extends javax.swing.JDialog {

    private UserManagement userManagement;
    private String username;

    private UserEditableData userEditableData = new UserEditableData();

    private String userStatusId;

    HashMap<String, String> userTypeMap = new HashMap<>();

    // Database Status Ids
    private String ACTIVE_STATUS_ID = "1";
    private String DEACTIVE_STATUS_ID = "2";

    public UserDetails(java.awt.Frame parent, boolean modal, String username, UserManagement userManagement) {
        super(parent, modal);
        this.username = username;
        this.userManagement = userManagement;
        initComponents();
        loadUserTypes();
        loadData();
    }

    private void loadUserTypes() {
        try {
            ResultSet result = MySQL.execute("SELECT * FROM user_type ");
            Vector v = new Vector();
            while (result.next()) {
                v.add(result.getString("name"));
                userTypeMap.put(result.getString("name"), result.getString("id"));
            }
            DefaultComboBoxModel model = new DefaultComboBoxModel(v);
            usertypeComboBox.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        try {

            ResultSet result = MySQL.execute("SELECT * FROM user u "
                    + "INNER JOIN user_type ut ON u.user_type_id = ut.id "
                    + "INNER JOIN user_status us ON u.user_status_id = us.id "
                    + "WHERE u.username = '" + username + "'");

            if (result.next()) {
                titleLable.setText(result.getString("u.full_name"));
                userTypeLabel.setText(result.getString("ut.name"));

                fullNameTextField.setText(result.getString("u.full_name"));
                emailTextField.setText(result.getString("u.email"));
                usernameTextField.setText(result.getString("u.username"));
                usertypeComboBox.setSelectedItem(result.getString("ut.name"));
                userStatusTextField.setText(result.getString("us.status"));
                verificationStatusTextField.setText(result.getBoolean("u.is_verified") ? "Verified" : "Not Verified");
                regDateTimeTextField.setText(result.getString("u.registered_date_time"));

                String userStatusId = result.getString("us.id");
                this.userStatusId = userStatusId;

                userEditableData.setFullName(result.getString("u.full_name"));
                userEditableData.setEmail(result.getString("u.email"));
                userEditableData.setUserTypeId(result.getString("ut.id"));

                if (userStatusId.equals(ACTIVE_STATUS_ID)) {
                    changeUserStatusButton.setText("Deactivate User");
                } else if (userStatusId.equals(DEACTIVE_STATUS_ID)) {
                    changeUserStatusButton.setText("Activate User");
                }

            } else {
                JOptionPane.showMessageDialog(this, "An unexpected error has occurred. Please try again later or contact support if the issue persists.", "Unexpected Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An unexpected error has occurred. Please try again later or contact support if the issue persists.", "Unexpected Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void enableEdits() {
        fullNameTextField.setEditable(true);
        emailTextField.setEditable(true);
        usertypeComboBox.setEnabled(true);
        updateChangesButton.setEnabled(true);
        enableEditsButton.setEnabled(false);
    }

    private void resetData() {
        loadData();
        fullNameTextField.setEditable(false);
        emailTextField.setEditable(false);
        usertypeComboBox.setEnabled(false);
        updateChangesButton.setEnabled(false);
        enableEditsButton.setEnabled(true);

    }

    private void updateDetails() {

        String fullName = fullNameTextField.getText();
        String email = emailTextField.getText();
        String userTypeId = userTypeMap.get(usertypeComboBox.getSelectedItem().toString());

        if (fullName.equals(userEditableData.getFullName()) && email.equals(userEditableData.getEmail()) && userTypeId.equals(userEditableData.getUserTypeId())) {
            JOptionPane.showMessageDialog(this,
                    "No changes detected to update.",
                    "Same Details",
                    JOptionPane.WARNING_MESSAGE);
        } else {

            try {
                if (fullName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "User's Full Name is Empty", "Warning", JOptionPane.WARNING_MESSAGE);
                    fullNameTextField.grabFocus();
                } else if (fullName.length() > 100) {
                    JOptionPane.showMessageDialog(this, "The Fullname must contain fewer than 100 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                    fullNameTextField.grabFocus();
                    fullNameTextField.selectAll();
                } else if (email.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please Enter the User's Email", "Warning", JOptionPane.WARNING_MESSAGE);
                    emailTextField.grabFocus();
                } else if (email.length() > 100) {
                    JOptionPane.showMessageDialog(this, "The email must contain fewer than 100 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                    emailTextField.grabFocus();
                    emailTextField.selectAll();
                } else {

                    ResultSet emailResultSet = MySQL.execute("SELECT * FROM user WHERE email='" + email + "' AND username != '" + username + "' ");

                    if (emailResultSet.next()) {
                        JOptionPane.showMessageDialog(this, "Email is already in use", "Warning", JOptionPane.WARNING_MESSAGE);
                        emailTextField.grabFocus();
                        emailTextField.selectAll();
                    } else {

                        MySQL.execute("UPDATE user SET full_name='" + fullName + "', email = '" + email + "', user_type_id='" + userTypeId + "' "
                                + "WHERE username = '" + username + "'");

                        JOptionPane.showMessageDialog(this, "User Updated Successfully", "Success", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/resource/success.png")));

                        resetData();

                        if (userManagement != null) {
                            userManagement.loadUserTable();
                        }

                    }

                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Unexpected Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();

            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        titleLable = new javax.swing.JLabel();
        userTypeLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        fullNameTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        usertypeComboBox = new javax.swing.JComboBox<>();
        updateChangesButton = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        emailTextField = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        usernameTextField = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        userStatusTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        enableEditsButton = new javax.swing.JButton();
        forgotPasswordButton = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        verificationStatusTextField = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        regDateTimeTextField = new javax.swing.JTextField();
        changeUserStatusButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("User Details");
        setResizable(false);

        titleLable.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        titleLable.setForeground(new java.awt.Color(0, 105, 75));
        titleLable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/user.png"))); // NOI18N
        titleLable.setText("User");

        userTypeLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        userTypeLabel.setForeground(new java.awt.Color(0, 105, 75));
        userTypeLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        userTypeLabel.setText("Admin");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(titleLable, javax.swing.GroupLayout.PREFERRED_SIZE, 439, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(userTypeLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(titleLable)
                    .addComponent(userTypeLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSeparator1.setForeground(new java.awt.Color(204, 204, 204));

        fullNameTextField.setEditable(false);
        fullNameTextField.setPreferredSize(new java.awt.Dimension(64, 35));

        jLabel6.setText("Full Name");

        jLabel8.setText("User Type");

        usertypeComboBox.setEnabled(false);
        usertypeComboBox.setPreferredSize(new java.awt.Dimension(72, 35));

        updateChangesButton.setText("Update Changes");
        updateChangesButton.setBorder(null);
        updateChangesButton.setEnabled(false);
        updateChangesButton.setPreferredSize(new java.awt.Dimension(99, 35));
        updateChangesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateChangesButtonActionPerformed(evt);
            }
        });

        resetButton.setBackground(new java.awt.Color(102, 102, 102));
        resetButton.setForeground(new java.awt.Color(255, 255, 255));
        resetButton.setText("Reset");
        resetButton.setBorder(null);
        resetButton.setPreferredSize(new java.awt.Dimension(99, 35));
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        jLabel10.setText("Email");

        emailTextField.setEditable(false);
        emailTextField.setPreferredSize(new java.awt.Dimension(64, 35));

        jLabel11.setText("Username");

        usernameTextField.setEditable(false);
        usernameTextField.setPreferredSize(new java.awt.Dimension(64, 35));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fullNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(emailTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(usernameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(updateChangesButton, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                    .addComponent(resetButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(usertypeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11)
                            .addComponent(jLabel8))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fullNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(emailTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usertypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(updateChangesButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resetButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(9, Short.MAX_VALUE))
        );

        userStatusTextField.setEditable(false);
        userStatusTextField.setPreferredSize(new java.awt.Dimension(64, 35));

        jLabel7.setText("User Status");

        enableEditsButton.setText("Enable Edits");
        enableEditsButton.setBorder(null);
        enableEditsButton.setPreferredSize(new java.awt.Dimension(99, 35));
        enableEditsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enableEditsButtonActionPerformed(evt);
            }
        });

        forgotPasswordButton.setBackground(new java.awt.Color(102, 102, 102));
        forgotPasswordButton.setForeground(new java.awt.Color(255, 255, 255));
        forgotPasswordButton.setText("Forgot Password");
        forgotPasswordButton.setBorder(null);
        forgotPasswordButton.setPreferredSize(new java.awt.Dimension(99, 35));
        forgotPasswordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                forgotPasswordButtonActionPerformed(evt);
            }
        });

        jLabel12.setText("Verification Status");

        verificationStatusTextField.setEditable(false);
        verificationStatusTextField.setPreferredSize(new java.awt.Dimension(64, 35));

        jLabel13.setText("Registered Date Time");

        regDateTimeTextField.setEditable(false);
        regDateTimeTextField.setPreferredSize(new java.awt.Dimension(64, 35));

        changeUserStatusButton.setBackground(new java.awt.Color(102, 102, 102));
        changeUserStatusButton.setForeground(new java.awt.Color(255, 255, 255));
        changeUserStatusButton.setText(" ");
        changeUserStatusButton.setBorder(null);
        changeUserStatusButton.setPreferredSize(new java.awt.Dimension(99, 35));
        changeUserStatusButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeUserStatusButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(userStatusTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(verificationStatusTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(regDateTimeTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(enableEditsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(forgotPasswordButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13))
                        .addGap(0, 130, Short.MAX_VALUE))
                    .addComponent(changeUserStatusButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userStatusTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(verificationStatusTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(regDateTimeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(enableEditsButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(forgotPasswordButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(changeUserStatusButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(9, 9, 9))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void updateChangesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateChangesButtonActionPerformed
        updateDetails();
    }//GEN-LAST:event_updateChangesButtonActionPerformed

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        // TODO add your handling code here:
        resetData();
    }//GEN-LAST:event_resetButtonActionPerformed

    private void forgotPasswordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_forgotPasswordButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_forgotPasswordButtonActionPerformed

    private void enableEditsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enableEditsButtonActionPerformed
        enableEdits();
    }//GEN-LAST:event_enableEditsButtonActionPerformed

    private void changeUserStatusButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeUserStatusButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_changeUserStatusButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UserDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UserDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UserDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UserDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton changeUserStatusButton;
    private javax.swing.JTextField emailTextField;
    private javax.swing.JButton enableEditsButton;
    private javax.swing.JButton forgotPasswordButton;
    private javax.swing.JTextField fullNameTextField;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField regDateTimeTextField;
    private javax.swing.JButton resetButton;
    private javax.swing.JLabel titleLable;
    private javax.swing.JButton updateChangesButton;
    private javax.swing.JTextField userStatusTextField;
    private javax.swing.JLabel userTypeLabel;
    private javax.swing.JTextField usernameTextField;
    private javax.swing.JComboBox<String> usertypeComboBox;
    private javax.swing.JTextField verificationStatusTextField;
    // End of variables declaration//GEN-END:variables
}
