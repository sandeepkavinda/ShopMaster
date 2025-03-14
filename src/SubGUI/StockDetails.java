/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package SubGUI;

import java.sql.ResultSet;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.MySQL;
import model.Numbers;

/**
 *
 * @author Sandeep
 */
public class StockDetails extends javax.swing.JDialog {

    private String barcode;
    private java.awt.Frame parent;
    private boolean modal;
    private double markedPrice;
    private double sellingDiscount;

    public StockDetails(java.awt.Frame parent, boolean modal, String barcode) {
        super(parent, modal);
        this.parent = parent;
        this.modal = modal;
        this.barcode = barcode;
        initComponents();
        loadData();
    }

    private void loadData() {
        try {

            enableSellingDiscountEditing(false);
            
            DefaultTableModel model = (DefaultTableModel) stockDetailsTable.getModel();
            model.setRowCount(0);

            ResultSet results = MySQL.execute(""
                    + "SELECT *,(`marked_price`-`selling_discount`) as `selling_price` FROM `stock` "
                    + "INNER JOIN `product` ON `stock`.`product_id`=`product`.`id` "
                    + "INNER JOIN `category` ON `product`.`category_id`=`category`.`id` "
                    + "INNER JOIN `measurement_unit` ON `product`.`measurement_unit_id`=`measurement_unit`.`id` "
                    + "WHERE `stock`.`barcode`='" + barcode + "'");

            if (results.next()) {
                this.setTitle("Stock Details (" + results.getString("product.name") + "-" + results.getString("stock.barcode") + ")");
                titleLable.setText("Barcode - " + results.getString("stock.barcode"));

                double currentQty = Double.parseDouble(results.getString("current_quantity"));
                double buyingPrice = Double.parseDouble(results.getString("buying_price"));
                markedPrice = Double.parseDouble(results.getString("marked_price"));
                sellingDiscount = Double.parseDouble(results.getString("selling_discount"));
                double sellingPrice = Double.parseDouble(results.getString("selling_price"));

                Object[][] rowData = {
                    {"Barcode", results.getString("stock.barcode")},
                    {"Product Name", results.getString("product.name")},
                    {"Category", results.getString("category.name")},
                    {"Measurement Unit", results.getString("measurement_unit.name")},
                    {"Buying Price", Numbers.formatPriceWithCurrencyCode(buyingPrice)},
                    {"Marked Price", Numbers.formatPriceWithCurrencyCode(markedPrice)},
                    {"Selling Discount", Numbers.formatPriceWithCurrencyCode(sellingDiscount)},
                    {"Selling Price", Numbers.formatPriceWithCurrencyCode(sellingPrice)},
                    {"Current Quantity", Numbers.formatQuantity(currentQty) + results.getString("measurement_unit.short_form")},
                    {"Added Date Time", results.getString("stock.added_date_time")},};

                // Add new rows to the model
                for (Object[] row : rowData) {
                    model.addRow(row);
                }
                sellingDiscountFormattedTextField.setText(Numbers.formatPrice(sellingDiscount));
                sellingPriceLabel.setText(" ");
            } else {
                JOptionPane.showMessageDialog(this, "Something Went Wrong", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An unexpected error has occurred. Please try again later or contact support if the issue persists.", "Unexpected Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void enableSellingDiscountEditing(boolean isEditing) {
        sellingPriceLabel.setText(" ");
        if (isEditing) {
            sellingDiscountLabel.setEnabled(true);
            sellingDiscountFormattedTextField.setEnabled(true);
            updateButton.setEnabled(true);
            resetButton.setEnabled(true);
        } else {
            sellingDiscountLabel.setEnabled(false);
            sellingDiscountFormattedTextField.setEnabled(false);
            updateButton.setEnabled(false);
            resetButton.setEnabled(false);
            sellingDiscountFormattedTextField.setText(Numbers.formatPrice(sellingDiscount));
        }
        enableEditingCheckBox.setSelected(isEditing);
    }

    private void changeSellingDiscount() {

        try {
            String sellingDiscount = sellingDiscountFormattedTextField.getText();

            //Validations
            if (sellingDiscount.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please Enter Selling Discount", "Warning", JOptionPane.WARNING_MESSAGE);
                sellingDiscountFormattedTextField.grabFocus();
            } else if (Double.parseDouble(sellingDiscount) < 0) {
                JOptionPane.showMessageDialog(this, "Selling Discount Cannot be Less Than Zero.", "Warning", JOptionPane.WARNING_MESSAGE);
                sellingDiscountFormattedTextField.grabFocus();
                sellingDiscountFormattedTextField.selectAll();
            } else if (markedPrice < Double.parseDouble(sellingDiscount)) {
                JOptionPane.showMessageDialog(this, "The selling discount cannot exceed the marked price.", "Warning", JOptionPane.WARNING_MESSAGE);
                sellingDiscountFormattedTextField.grabFocus();
                sellingDiscountFormattedTextField.selectAll();
            } else {

                int option = JOptionPane.showConfirmDialog(this, "<html>Are you sure you want to Change Selling Discount to <b>Rs." + sellingDiscountFormattedTextField.getText() + "</b> </html>", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.YES_NO_OPTION, new ImageIcon(getClass().getResource("/resource/question.png")));

                if (option == 0) {
                    try {

                        MySQL.execute("UPDATE `stock` SET `selling_discount`='" + Double.parseDouble(sellingDiscount) + "' "
                                + "WHERE `stock`.`barcode`='" + barcode + "' ");

                        JOptionPane.showMessageDialog(this, "Discount Changed Successfully", "Success", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/resource/success.png")));
                        loadData();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "An unexpected error has occurred. Please try again later or contact support if the issue persists.", "Unexpected Error", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                }

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Something Went Wrong", "Unexpected Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }

    private void changeNewSellingPriceLable() {
        try {
            String sellingDiscount = sellingDiscountFormattedTextField.getText();
            //Validations
            if (sellingDiscount.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please Enter Selling Discount", "Warning", JOptionPane.WARNING_MESSAGE);
                sellingDiscountFormattedTextField.grabFocus();
            } else if (Double.parseDouble(sellingDiscount) < 0) {
                JOptionPane.showMessageDialog(this, "Selling Discount Cannot be Less Than Zero.", "Warning", JOptionPane.WARNING_MESSAGE);
                sellingDiscountFormattedTextField.grabFocus();
                sellingDiscountFormattedTextField.selectAll();
            } else if (markedPrice < Double.parseDouble(sellingDiscount)) {
                JOptionPane.showMessageDialog(this, "The selling discount cannot exceed the marked price.", "Warning", JOptionPane.WARNING_MESSAGE);
                sellingDiscountFormattedTextField.grabFocus();
                sellingDiscountFormattedTextField.selectAll();
            } else {
                sellingPriceLabel.setText("New Selling Price : " + Numbers.formatPriceWithCurrencyCode(markedPrice - Double.parseDouble(sellingDiscount)) + "");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Something Went Wrong", "Unexpected Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
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
        titleLable = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        stockDetailsTable = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        sellingDiscountLabel = new javax.swing.JLabel();
        sellingDiscountFormattedTextField = new javax.swing.JFormattedTextField();
        jPanel6 = new javax.swing.JPanel();
        updateButton = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel8 = new javax.swing.JPanel();
        sellingPriceLabel = new javax.swing.JLabel();
        enableEditingCheckBox = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Stock Details - Munchee Super Cream Cracker");
        setResizable(false);

        titleLable.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        titleLable.setForeground(new java.awt.Color(0, 105, 75));
        titleLable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLable.setText("-");
        titleLable.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

        jScrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jScrollPane2.setForeground(new java.awt.Color(204, 204, 204));

        stockDetailsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Title", " Data"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(stockDetailsTable);

        jPanel3.setLayout(new java.awt.GridLayout(1, 0, 10, 0));

        jButton1.setText("Add Quantity");
        jButton1.setBorder(null);
        jPanel3.add(jButton1);

        jButton2.setBackground(new java.awt.Color(51, 51, 51));
        jButton2.setText("Remove Quantity");
        jButton2.setBorder(null);
        jPanel3.add(jButton2);

        jPanel4.setLayout(new java.awt.GridLayout());

        sellingDiscountLabel.setText("Selling Discount");

        sellingDiscountFormattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("0.00"))));
        sellingDiscountFormattedTextField.setText("0.00");
        sellingDiscountFormattedTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sellingDiscountFormattedTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sellingDiscountLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                    .addComponent(sellingDiscountFormattedTextField))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sellingDiscountLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sellingDiscountFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel7);

        updateButton.setText("Update");
        updateButton.setBorder(null);
        updateButton.setPreferredSize(new java.awt.Dimension(99, 35));
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
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

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resetButton, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(28, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resetButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel4.add(jPanel6);

        jSeparator1.setForeground(new java.awt.Color(204, 204, 204));

        jSeparator2.setForeground(new java.awt.Color(204, 204, 204));

        sellingPriceLabel.setText("Selling Price : 0:00");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sellingPriceLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(sellingPriceLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        enableEditingCheckBox.setText("Enable Editing");
        enableEditingCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                enableEditingCheckBoxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(titleLable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(enableEditingCheckBox)
                .addGap(173, 173, 173))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLable, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(enableEditingCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateButtonActionPerformed
        changeSellingDiscount();
    }//GEN-LAST:event_updateButtonActionPerformed

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        loadData();
    }//GEN-LAST:event_resetButtonActionPerformed

    private void enableEditingCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_enableEditingCheckBoxItemStateChanged
        if (enableEditingCheckBox.isSelected()) {
            enableSellingDiscountEditing(true);
        } else {
            enableSellingDiscountEditing(false);

        }
    }//GEN-LAST:event_enableEditingCheckBoxItemStateChanged

    private void sellingDiscountFormattedTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sellingDiscountFormattedTextFieldActionPerformed
        changeNewSellingPriceLable();
    }//GEN-LAST:event_sellingDiscountFormattedTextFieldActionPerformed

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
            java.util.logging.Logger.getLogger(StockDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StockDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StockDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StockDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox enableEditingCheckBox;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JButton resetButton;
    private javax.swing.JFormattedTextField sellingDiscountFormattedTextField;
    private javax.swing.JLabel sellingDiscountLabel;
    private javax.swing.JLabel sellingPriceLabel;
    private javax.swing.JTable stockDetailsTable;
    private javax.swing.JLabel titleLable;
    private javax.swing.JButton updateButton;
    // End of variables declaration//GEN-END:variables
}
