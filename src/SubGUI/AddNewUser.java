/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package SubGUI;

import java.awt.Toolkit;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.MySQL;
import panels.UserManagement;
import java.sql.Timestamp;


/**
 *
 * @author Sandeep
 */
public class AddNewUser extends javax.swing.JDialog {

    UserManagement userManagement;

    HashMap<String, String> userTypeMap = new HashMap<>();

    /**
     * Creates new form AddNewProduct
     */
    public AddNewUser(java.awt.Frame parent, boolean modal, UserManagement userManagement) {
        super(parent, modal);
        this.userManagement = userManagement;
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resource/icon.png")));
        initComponents();
        loadUserTypes();
        loadUserTable();
    }

    private void loadUserTypes() {
        try {
            ResultSet result = MySQL.execute("SELECT * FROM `user_type` WHERE id != 'Admin' ");
            Vector v = new Vector();
            v.add("Select");
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

    private void loadUserTable() {
        try {
            DefaultTableModel model = (DefaultTableModel) userTable.getModel();
            model.setRowCount(0);

            ResultSet results = MySQL.execute("SELECT * FROM user u "
                    + "INNER JOIN user_type ut ON u.user_type_id = ut.id "
                    + "INNER JOIN user_status us ON u.user_status_id = us.id "
                    + "ORDER BY u.registered_date_time DESC");

            while (results.next()) {

                Vector v = new Vector();
                v.add(results.getString("u.username"));
                v.add(results.getString("u.email"));
                v.add(results.getString("u.full_name"));
                v.add(results.getString("ut.name"));
                v.add(results.getString("u.registered_date_time"));
                model.addRow(v);
            }
            userManagement.loadUserTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetFields() {
        fullNameTextField.setText("");
        emailTextField.setText("");
        usernameTextField.setText("");
        fullNameTextField.setText("");
        usertypeComboBox.setSelectedIndex(0);
        fullNameTextField.grabFocus();
    }

    private void addNewUser() {
        String fullName = fullNameTextField.getText();
        String email = emailTextField.getText();
        String userName = usernameTextField.getText();
        String userTypeId = userTypeMap.get(usertypeComboBox.getSelectedItem());

        if (fullName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter the User's Full Name", "Warning", JOptionPane.WARNING_MESSAGE);
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
        } else if (userName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter a Username", "Warning", JOptionPane.WARNING_MESSAGE);
            usernameTextField.grabFocus();
        } else if (userName.length() > 20) {
            JOptionPane.showMessageDialog(this, "The Username must contain fewer than 20 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
            usernameTextField.grabFocus();
            usernameTextField.selectAll();
        } else if (userTypeId == null) {
            JOptionPane.showMessageDialog(this, "Please Select User Type", "Warning", JOptionPane.WARNING_MESSAGE);
            usertypeComboBox.grabFocus();
        } else {

            try {
                ResultSet emailResultSet = MySQL.execute("SELECT * FROM `user` WHERE `email`='" + email + "'");
                ResultSet userNameResultSet = MySQL.execute("SELECT * FROM `user` WHERE `username`='" + userName + "'");

                if (emailResultSet.next()) {
                    JOptionPane.showMessageDialog(this, "Email is already in use", "Warning", JOptionPane.WARNING_MESSAGE);
                    emailTextField.grabFocus();
                    emailTextField.selectAll();
                } else if (userNameResultSet.next()) {
                    JOptionPane.showMessageDialog(this, "Username is already in use", "Warning", JOptionPane.WARNING_MESSAGE);
                    usernameTextField.grabFocus();
                    usernameTextField.selectAll();
                } else {

                    LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(10);
                    Timestamp expiryTimestamp = Timestamp.valueOf(expiresAt);

                    SecureRandom random = new SecureRandom();
                    int verificationCode = 100000 + random.nextInt(900000);

                    MySQL.execute("INSERT INTO user (full_name, username, email, password, user_type_id, user_status_id, is_verified, verification_code, verification_code_expiry) "
                            + "VALUES ('" + fullName + "', '" + userName + "', '" + email + "', " + null + ", '" + userTypeId + "', '1', '0', '" + verificationCode + "', '" + expiryTimestamp + "')");
                    JOptionPane.showMessageDialog(this, "User Added Successfully", "Success", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/resource/success.png")));
                    loadUserTable();
                    resetFields();
                    
                    UserOtpDetails userOtpDetails = new UserOtpDetails(null, true, userName);
                    userOtpDetails.setVisible(true);
                    
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        jPanel2 = new javax.swing.JPanel();
        fullNameTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        usertypeComboBox = new javax.swing.JComboBox<>();
        addUserButton = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        emailTextField = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        usernameTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        userTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add New User");
        setMinimumSize(new java.awt.Dimension(700, 500));
        setResizable(false);

        fullNameTextField.setPreferredSize(new java.awt.Dimension(64, 35));
        fullNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fullNameTextFieldActionPerformed(evt);
            }
        });

        jLabel6.setText("Full Name");

        jLabel8.setText("User Type");

        usertypeComboBox.setPreferredSize(new java.awt.Dimension(72, 35));
        usertypeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usertypeComboBoxActionPerformed(evt);
            }
        });

        addUserButton.setText("Add User");
        addUserButton.setBorder(null);
        addUserButton.setPreferredSize(new java.awt.Dimension(99, 35));
        addUserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addUserButtonActionPerformed(evt);
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

        emailTextField.setPreferredSize(new java.awt.Dimension(64, 35));
        emailTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailTextFieldActionPerformed(evt);
            }
        });

        jLabel11.setText("Username");

        usernameTextField.setPreferredSize(new java.awt.Dimension(64, 35));
        usernameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fullNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(emailTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(usernameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addUserButton, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
                    .addComponent(resetButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(usertypeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11)
                            .addComponent(jLabel8))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
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
                .addComponent(addUserButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resetButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        userTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Username", "Email", "Full Name", "User Type", "Registered Date Time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(userTable);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 603, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 105, 75));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/logo-extra-sm.png"))); // NOI18N
        jLabel2.setIconTextGap(20);

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 105, 75));
        jLabel1.setText("Add New User");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

        jSeparator1.setForeground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void addUserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addUserButtonActionPerformed
        addNewUser();
    }//GEN-LAST:event_addUserButtonActionPerformed

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        // TODO add your handling code here:
        resetFields();
    }//GEN-LAST:event_resetButtonActionPerformed

    private void fullNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fullNameTextFieldActionPerformed
        emailTextField.grabFocus();
    }//GEN-LAST:event_fullNameTextFieldActionPerformed

    private void usertypeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usertypeComboBoxActionPerformed
        addUserButton.grabFocus();
    }//GEN-LAST:event_usertypeComboBoxActionPerformed

    private void emailTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailTextFieldActionPerformed
        usernameTextField.grabFocus();
    }//GEN-LAST:event_emailTextFieldActionPerformed

    private void usernameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameTextFieldActionPerformed
        usertypeComboBox.grabFocus();
    }//GEN-LAST:event_usernameTextFieldActionPerformed

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
            java.util.logging.Logger.getLogger(AddNewUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddNewUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddNewUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddNewUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ;
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addUserButton;
    private javax.swing.JTextField emailTextField;
    private javax.swing.JTextField fullNameTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton resetButton;
    private javax.swing.JTable userTable;
    private javax.swing.JTextField usernameTextField;
    private javax.swing.JComboBox<String> usertypeComboBox;
    // End of variables declaration//GEN-END:variables
}
