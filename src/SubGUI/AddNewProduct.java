/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package SubGUI;

import java.awt.Toolkit;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.MySQL;
import panels.ProductManagement;

/**
 *
 * @author Sandeep
 */
public class AddNewProduct extends javax.swing.JDialog {

    ProductManagement productManagement;
    
    HashMap<String, String> categoryMap = new HashMap<>();
    HashMap<String, String> measUnitsMap = new HashMap<>();

    /**
     * Creates new form AddNewProduct
     */
    public AddNewProduct(java.awt.Frame parent, boolean modal,ProductManagement productManagement) {
        super(parent, modal);
        this.productManagement = productManagement;
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resource/icon.png")));
        initComponents();
        loadCategories();
        loadMeasurementUnits();
        loadProductTable();
    }

    private void loadCategories() {
        try {
            ResultSet result = MySQL.execute("SELECT * FROM `category`");
            Vector v = new Vector();
            v.add("Select");
            while (result.next()) {
                v.add(result.getString("name"));
                categoryMap.put(result.getString("name"), result.getString("id"));
            }
            DefaultComboBoxModel model = new DefaultComboBoxModel(v);
            categotyComboBox.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMeasurementUnits() {
        try {
            ResultSet result = MySQL.execute("SELECT * FROM `measurement_unit`");
            Vector v = new Vector();
            v.add("Select");
            while (result.next()) {
                v.add(result.getString("name"));
                measUnitsMap.put(result.getString("name"), result.getString("id"));
            }
            DefaultComboBoxModel model = new DefaultComboBoxModel(v);
            measUnitsComboBox.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadProductTable() {
        try {

            DefaultTableModel model = (DefaultTableModel) productTable.getModel();
            model.setRowCount(0);

            ResultSet results = MySQL.execute(""
                    + "SELECT * FROM `product` "
                    + "INNER JOIN `category` ON `product`.`category_id`=`category`.`id` "
                    + "ORDER BY `product`.`id` DESC");

            while (results.next()) {

                Vector v = new Vector();
                v.add(results.getString("id"));
                v.add(results.getString("name"));
                v.add(results.getString("category.name"));
                model.addRow(v);
            }
            
            productManagement.loadProductTable();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetFields() {
        productNameTextField.setText("");
        categotyComboBox.setSelectedIndex(0);
        measUnitsComboBox.setSelectedIndex(0);

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
        productNameTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        categotyComboBox = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        measUnitsComboBox = new javax.swing.JComboBox<>();
        addNewProductButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        addNewProductButton1 = new javax.swing.JButton();
        addNewProductButton2 = new javax.swing.JButton();
        addNewProductButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        productTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add New Product");
        setMinimumSize(new java.awt.Dimension(700, 500));
        setResizable(false);

        productNameTextField.setPreferredSize(new java.awt.Dimension(64, 35));
        productNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                productNameTextFieldKeyReleased(evt);
            }
        });

        jLabel6.setText("Product Name");

        jLabel7.setText("Category");

        categotyComboBox.setPreferredSize(new java.awt.Dimension(72, 35));

        jLabel8.setText("Measurement Unit");

        measUnitsComboBox.setPreferredSize(new java.awt.Dimension(72, 35));

        addNewProductButton.setText("Add Product");
        addNewProductButton.setBorder(null);
        addNewProductButton.setPreferredSize(new java.awt.Dimension(99, 35));
        addNewProductButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewProductButtonActionPerformed(evt);
            }
        });

        jSeparator1.setForeground(new java.awt.Color(204, 204, 204));

        addNewProductButton1.setBackground(new java.awt.Color(255, 255, 255));
        addNewProductButton1.setFont(new java.awt.Font("Poppins", 0, 10)); // NOI18N
        addNewProductButton1.setForeground(new java.awt.Color(0, 105, 75));
        addNewProductButton1.setText("Go to Add Measurement Unit");
        addNewProductButton1.setBorder(null);
        addNewProductButton1.setPreferredSize(new java.awt.Dimension(99, 35));
        addNewProductButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewProductButton1ActionPerformed(evt);
            }
        });

        addNewProductButton2.setBackground(new java.awt.Color(255, 255, 255));
        addNewProductButton2.setFont(new java.awt.Font("Poppins", 0, 10)); // NOI18N
        addNewProductButton2.setForeground(new java.awt.Color(0, 105, 75));
        addNewProductButton2.setText("Go to Add Category");
        addNewProductButton2.setBorder(null);
        addNewProductButton2.setPreferredSize(new java.awt.Dimension(99, 35));
        addNewProductButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewProductButton2ActionPerformed(evt);
            }
        });

        addNewProductButton3.setBackground(new java.awt.Color(255, 255, 255));
        addNewProductButton3.setForeground(new java.awt.Color(0, 105, 75));
        addNewProductButton3.setText("Reset");
        addNewProductButton3.setBorder(null);
        addNewProductButton3.setPreferredSize(new java.awt.Dimension(99, 35));
        addNewProductButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewProductButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(productNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(categotyComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(measUnitsComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addNewProductButton, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(addNewProductButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                    .addComponent(addNewProductButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                    .addComponent(addNewProductButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(productNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(categotyComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(measUnitsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(addNewProductButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addNewProductButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(addNewProductButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(addNewProductButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        productTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Id", "Product Name", "Category"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(productTable);
        if (productTable.getColumnModel().getColumnCount() > 0) {
            productTable.getColumnModel().getColumn(0).setMaxWidth(50);
            productTable.getColumnModel().getColumn(1).setMinWidth(200);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void productNameTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_productNameTextFieldKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_productNameTextFieldKeyReleased

    private void addNewProductButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewProductButtonActionPerformed
        // TODO add your handling code here:

        String productName = productNameTextField.getText();
        String categoryId = categoryMap.get(categotyComboBox.getSelectedItem());
        String measUnitId = measUnitsMap.get(measUnitsComboBox.getSelectedItem());

        if (productName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter the Product Name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (productName.length() > 50) {
            JOptionPane.showMessageDialog(this, "The Product Name must contain fewer than 50 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (categoryId == null) {
            JOptionPane.showMessageDialog(this, "Please Select a Category", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (measUnitId == null) {
            JOptionPane.showMessageDialog(this, "Please Select a Measurement Id", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            try {
                ResultSet resultset = MySQL.execute("SELECT * FROM `product` WHERE `name`='" + productName + "'");
                if (resultset.next()) {
                    JOptionPane.showMessageDialog(this, '"' + productName + '"' + " is Already Added", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {

                    MySQL.execute("INSERT INTO `product` (`name`,`category_id`,`measurement_unit_id`) "
                            + "VALUES ('" + productName + "','" + categoryId + "','" + measUnitId + "')");
                    JOptionPane.showMessageDialog(this, "Product Added Successfully", "Success", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/resource/success.png")));
                    loadProductTable();
                    resetFields();
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }

        }


    }//GEN-LAST:event_addNewProductButtonActionPerformed

    private void addNewProductButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewProductButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addNewProductButton1ActionPerformed

    private void addNewProductButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewProductButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addNewProductButton2ActionPerformed

    private void addNewProductButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewProductButton3ActionPerformed
        // TODO add your handling code here:
        resetFields();
    }//GEN-LAST:event_addNewProductButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(AddNewProduct.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddNewProduct.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddNewProduct.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddNewProduct.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
;
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addNewProductButton;
    private javax.swing.JButton addNewProductButton1;
    private javax.swing.JButton addNewProductButton2;
    private javax.swing.JButton addNewProductButton3;
    private javax.swing.JComboBox<String> categotyComboBox;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JComboBox<String> measUnitsComboBox;
    private javax.swing.JTextField productNameTextField;
    private javax.swing.JTable productTable;
    // End of variables declaration//GEN-END:variables
}
