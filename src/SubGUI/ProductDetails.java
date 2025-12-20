package SubGUI;

import DTO.ProductEditableData;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import model.MySQL;
import panels.ProductManagement;

/**
 *
 * @author Sandeep
 */
public class ProductDetails extends javax.swing.JDialog {

    private ProductManagement productManagement;
    private String productId;
    private ProductEditableData productEditableData;

    HashMap<String, Integer> categoryIdMap = new HashMap<>();

    public ProductDetails(java.awt.Frame parent, boolean modal, String productId, ProductManagement productManagement, boolean edibable) {
        super(parent, modal);
        this.productId = productId;
        this.productManagement = productManagement;
        initComponents();
        loadCategories();
        
        if(edibable){
            enableEdits();
        }
        
        loadData();
    }

    private void loadCategories() {
        try {
            ResultSet result = MySQL.execute("SELECT * FROM category ");
            Vector v = new Vector();
            while (result.next()) {
                v.add(result.getString("name"));
                categoryIdMap.put(result.getString("name"), result.getInt("id"));
            }
            DefaultComboBoxModel model = new DefaultComboBoxModel(v);
            categoryComboBox.setModel(model);
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadData() {
        try {

            ResultSet result = MySQL.execute("SELECT * FROM product p "
                    + "INNER JOIN category c ON p.category_id = c.id "
                    + "INNER JOIN measurement_unit mu ON p.measurement_unit_id = mu.id "
                    + "WHERE p.id = '" + productId + "' ");

            if (result.next()) {
                titleLable.setText(result.getString("p.name"));
                productIdLabel.setText("ID: " + result.getString("p.id"));

                productNameTextField.setText(result.getString("p.name"));
                categoryComboBox.setSelectedItem(result.getString("c.name"));
                MeasurementUnitTextField.setText(result.getString("mu.name"));

                productEditableData = new ProductEditableData(
                        result.getString("p.name"),
                        result.getInt("c.id"),
                        result.getString("c.name"),
                        result.getInt("mu.id")
                );

                this.setVisible(true);

            } else {
                JOptionPane.showMessageDialog(null, "An unexpected error has occurred. Please try again later or contact support if the issue persists.", "Unexpected Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An unexpected error has occurred. Please try again later or contact support if the issue persists.", "Unexpected Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void enableEdits() {
        productNameTextField.setEditable(true);
        categoryComboBox.setEnabled(true);
        
        enableEditsButton.setEnabled(false);
        updateChangesButton.setEnabled(true);
        resetButton.setEnabled(true);
    }

    private void resetData() {
        loadData();
        productNameTextField.setEditable(false);
        categoryComboBox.setEnabled(false);

        enableEditsButton.setEnabled(true);
        updateChangesButton.setEnabled(false);
        resetButton.setEnabled(false);

    }

    private void updateDetails() {

        String productName = productNameTextField.getText();
        int categoryId = categoryIdMap.get(categoryComboBox.getSelectedItem().toString());
        int measUnitId = productEditableData.getMeasurementUnitId();
        
        if (productName.equals(productEditableData.getProductName()) && categoryId == productEditableData.getCategoryId()) {
            JOptionPane.showMessageDialog(this,
                    "No changes detected to update.",
                    "Same Details",
                    JOptionPane.WARNING_MESSAGE);
        } else {

            try {
                if (productName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "User's Full Name is Empty", "Warning", JOptionPane.WARNING_MESSAGE);
                    productNameTextField.grabFocus();
                } else if (productName.length() > 100) {
                    JOptionPane.showMessageDialog(this, "The Product Name must contain fewer than 100 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                    productNameTextField.grabFocus();
                    productNameTextField.selectAll();
                } else {

                    ResultSet resultset = MySQL.execute("SELECT * FROM product WHERE name='" + productName + "' AND category_id='" + categoryId + "' AND measurement_unit_id='" + measUnitId + "' ");
                    if (resultset.next()) {
                        JOptionPane.showMessageDialog(this, "This product already exists with the same category and unit.", "Duplicate Product", JOptionPane.WARNING_MESSAGE);
                    } else {

                        MySQL.execute("UPDATE product SET name='" + productName + "', category_id = '" + categoryId + "' "
                                + "WHERE id = '" + productId + "'");

                        JOptionPane.showMessageDialog(this, "Product Updated Successfully", "Success", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/resource/success.png")));

                        resetData();

                        if (productManagement != null) {
                            productManagement.loadProductTable();
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
        productIdLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        productNameTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        updateChangesButton = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        categoryComboBox = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        enableEditsButton = new javax.swing.JButton();
        MeasurementUnitTextField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Product Details");
        setResizable(false);

        titleLable.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        titleLable.setForeground(new java.awt.Color(0, 105, 75));
        titleLable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/product.png"))); // NOI18N
        titleLable.setText("User");

        productIdLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        productIdLabel.setForeground(new java.awt.Color(0, 105, 75));
        productIdLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        productIdLabel.setText("ID : 25");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(titleLable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(productIdLabel)
                .addGap(30, 30, 30))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(titleLable)
                    .addComponent(productIdLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSeparator1.setForeground(new java.awt.Color(204, 204, 204));

        productNameTextField.setEditable(false);
        productNameTextField.setPreferredSize(new java.awt.Dimension(64, 35));

        jLabel6.setText("Product Name");

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
        resetButton.setEnabled(false);
        resetButton.setPreferredSize(new java.awt.Dimension(99, 35));
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        jLabel9.setText("Category");

        categoryComboBox.setEnabled(false);
        categoryComboBox.setPreferredSize(new java.awt.Dimension(72, 35));

        jLabel8.setText("Measurement Unit");

        enableEditsButton.setText("Enable Edits");
        enableEditsButton.setBorder(null);
        enableEditsButton.setPreferredSize(new java.awt.Dimension(99, 35));
        enableEditsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enableEditsButtonActionPerformed(evt);
            }
        });

        MeasurementUnitTextField.setEditable(false);
        MeasurementUnitTextField.setPreferredSize(new java.awt.Dimension(64, 35));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(productNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(updateChangesButton, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                    .addComponent(resetButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(categoryComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel9)
                            .addComponent(jLabel8))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(enableEditsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(MeasurementUnitTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(productNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(categoryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(MeasurementUnitTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(enableEditsButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(updateChangesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resetButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(9, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(88, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(11, 11, 11)
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

    private void enableEditsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enableEditsButtonActionPerformed
        enableEdits();
    }//GEN-LAST:event_enableEditsButtonActionPerformed

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
            java.util.logging.Logger.getLogger(ProductDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ProductDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ProductDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ProductDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField MeasurementUnitTextField;
    private javax.swing.JComboBox<String> categoryComboBox;
    private javax.swing.JButton enableEditsButton;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel productIdLabel;
    private javax.swing.JTextField productNameTextField;
    private javax.swing.JButton resetButton;
    private javax.swing.JLabel titleLable;
    private javax.swing.JButton updateChangesButton;
    // End of variables declaration//GEN-END:variables
}
