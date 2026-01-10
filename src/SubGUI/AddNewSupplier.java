/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package SubGUI;

import java.awt.Toolkit;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.MySQL;
import panels.SupplierManagement;

/**
 *
 * @author Sandeep
 */
public class AddNewSupplier extends javax.swing.JDialog {

    private SupplierManagement supplierManagement;
    private NewGRN newGRN;

    /**
     * Creates new form AddNewProduct
     */
    public AddNewSupplier(java.awt.Frame parent, boolean modal, SupplierManagement supplierManagement, NewGRN newGRN) {
        super(parent, modal);
        this.supplierManagement = supplierManagement;
        this.newGRN = newGRN;
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resource/icon.png")));
        initComponents();
        loadSupplierTable();

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        supplierTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        supplierTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        supplierTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

    }

    private void loadSupplierTable() {
        try {
            DefaultTableModel model = (DefaultTableModel) supplierTable.getModel();
            model.setRowCount(0);

            ResultSet results = MySQL.execute("SELECT * FROM supplier s "
                    + "ORDER BY s.id DESC");

            while (results.next()) {

                Vector v = new Vector();
                v.add(results.getString("s.id"));
                v.add(results.getString("s.name"));
                v.add(results.getString("s.phone"));
                model.addRow(v);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetFields() {
        nameTextField.setText("");
        phoneTextField.setText("");
        nameTextField.grabFocus();
    }

    private void addNewSupplier() {
        String name = nameTextField.getText();
        String phone = phoneTextField.getText();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter the Supplier's Name", "Warning", JOptionPane.WARNING_MESSAGE);
            nameTextField.grabFocus();
        } else if (name.length() > 100) {
            JOptionPane.showMessageDialog(this, "The name must contain fewer than 100 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
            nameTextField.grabFocus();
            nameTextField.selectAll();
        } else if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter the Supplier's Phone", "Warning", JOptionPane.WARNING_MESSAGE);
            phoneTextField.grabFocus();
        } else if (phone.length() != 10) {
            JOptionPane.showMessageDialog(this, "The phone must contain 10 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
            phoneTextField.grabFocus();
            phoneTextField.selectAll();
        } else {

            try {
                ResultSet resultSet = MySQL.execute("SELECT * FROM supplier WHERE name='" + name + "' AND phone = '" + phone + "' ");

                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(this, "A supplier with this name and phone number already exists.", "Warning", JOptionPane.WARNING_MESSAGE);
                    nameTextField.grabFocus();
                } else {

                    MySQL.execute("INSERT INTO supplier (name, phone) "
                            + "VALUES ('" + name + "', '" + phone + "')");
                    JOptionPane.showMessageDialog(this, "Supplier Added Successfully", "Success", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/resource/success.png")));
                    loadSupplierTable();
                    if (supplierManagement != null) {
                        supplierManagement.loadSupplierTable();
                    }

                    if (newGRN != null) {
                        ResultSet supplierRes = MySQL.execute("SELECT id FROM supplier "
                                + "WHERE name ='" + name + "' AND phone = '" + phone + "' ");
                        if (supplierRes.next()) {
                            newGRN.setSupplier(supplierRes.getString("id"));
                            this.dispose();
                        } else {
                            JOptionPane.showMessageDialog(this, "An unexpected error has occurred. Please try again later or contact support if the issue persists.", "Unexpected Error", JOptionPane.ERROR_MESSAGE);
                        }

                    }

                    resetFields();
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
        nameTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        addSupplierButton = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        phoneTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        supplierTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add New Supplier");
        setResizable(false);

        nameTextField.setPreferredSize(new java.awt.Dimension(64, 35));
        nameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameTextFieldActionPerformed(evt);
            }
        });

        jLabel6.setText("Name");

        addSupplierButton.setText("Add Supplier");
        addSupplierButton.setBorder(null);
        addSupplierButton.setPreferredSize(new java.awt.Dimension(99, 35));
        addSupplierButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSupplierButtonActionPerformed(evt);
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

        jLabel10.setText("Phone");

        phoneTextField.setPreferredSize(new java.awt.Dimension(64, 35));
        phoneTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phoneTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(phoneTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addSupplierButton, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
                    .addComponent(resetButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel10))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(phoneTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(addSupplierButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resetButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        supplierTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Id", "Name", "Phone"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(supplierTable);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1)
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
        jLabel1.setText("Add New Supplier");
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
                .addContainerGap())
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

    private void addSupplierButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSupplierButtonActionPerformed
        addNewSupplier();
    }//GEN-LAST:event_addSupplierButtonActionPerformed

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        resetFields();
    }//GEN-LAST:event_resetButtonActionPerformed

    private void nameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameTextFieldActionPerformed
        phoneTextField.grabFocus();
    }//GEN-LAST:event_nameTextFieldActionPerformed

    private void phoneTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phoneTextFieldActionPerformed
        addSupplierButton.grabFocus();
    }//GEN-LAST:event_phoneTextFieldActionPerformed

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
            java.util.logging.Logger.getLogger(AddNewSupplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddNewSupplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddNewSupplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddNewSupplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
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
    private javax.swing.JButton addSupplierButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JTextField phoneTextField;
    private javax.swing.JButton resetButton;
    private javax.swing.JTable supplierTable;
    // End of variables declaration//GEN-END:variables
}
