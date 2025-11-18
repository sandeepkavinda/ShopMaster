/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package SubGUI;

import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import model.MySQL;
import model.Numbers;

/**
 *
 * @author Sandeep
 */
public class SelectStock extends javax.swing.JDialog {

    private NewGRN newGRN;
    private NewInvoice newInvoice;
    HashMap<String, String> productMap = new HashMap<>();

    public SelectStock(java.awt.Frame parent, boolean modal, NewGRN newGRN, NewInvoice newInvoice) {
        super(parent, modal);
        this.newGRN = newGRN;
        this.newInvoice = newInvoice;
        initComponents();
        loadProducts();
        loadStockTable();
        stockTable.grabFocus();
        stockTable.setRowSelectionInterval(0, 0);
    }

    private void loadProducts() {
        try {
            ResultSet result = MySQL.execute("SELECT * FROM `product`");
            Vector v = new Vector();
            v.add("Any Product");
            while (result.next()) {
                v.add(result.getString("name"));
                productMap.put(result.getString("name"), result.getString("id"));
            }
            DefaultComboBoxModel model = new DefaultComboBoxModel(v);
            productComboBox.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadStockTable() {
        try {

            barcodeTextField.setText("");
            String productId = productMap.get(String.valueOf(productComboBox.getSelectedItem()));
            String search = searchTextField.getText();

            String searchByProductQueryPart = "";

            if (productId != null) {
                searchByProductQueryPart = "AND `stock`.`product_id`='" + productId + "' ";
            }

            DefaultTableModel model = (DefaultTableModel) stockTable.getModel();
            model.setRowCount(0);

            ResultSet results = MySQL.execute(""
                    + "SELECT *,(`marked_price`-`selling_discount`) as `selling_price` FROM `stock` "
                    + "INNER JOIN `product` ON `stock`.`product_id`=`product`.`id` "
                    + "INNER JOIN `measurement_unit` ON `product`.`measurement_unit_id`=`measurement_unit`.`id` "
                    + "WHERE `product`.`name` LIKE '%" + search + "%'"
                    + searchByProductQueryPart
                    + "ORDER BY `stock`.`added_date_time` DESC ");

            while (results.next()) {

                double currentQty = Double.parseDouble(results.getString("current_quantity"));
                double buyingPrice = Double.parseDouble(results.getString("buying_price"));
                double markedPrice = Double.parseDouble(results.getString("marked_price"));
                double sellingDiscount = Double.parseDouble(results.getString("selling_discount"));
                double sellingPrice = Double.parseDouble(results.getString("selling_price"));

                Vector v = new Vector();
                v.add(results.getString("barcode"));
                v.add(results.getString("product.name"));
                v.add(Numbers.formatPrice(buyingPrice));
                v.add(Numbers.formatPrice(markedPrice));
                v.add(Numbers.formatPrice(sellingDiscount));
                v.add(Numbers.formatPrice(sellingPrice));
                v.add(Numbers.formatQuantity(currentQty));
                v.add(results.getString("measurement_unit.name"));
                model.addRow(v);
            }
        } catch (Exception e) {
             JOptionPane.showMessageDialog(this, "An unexpected error has occurred. Please try again later or contact support if the issue persists.", "Unexpected Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void clearSearch() {
        barcodeTextField.setText("");
        searchTextField.setText("");
        productComboBox.setSelectedIndex(0);
        loadStockTable();
    }

    private void searchByBarcode() {

        // Remove all whitespaces (spaces, tabs, newlines)
        String barcode = barcodeTextField.getText().replaceAll("\\s+", "");
        barcodeTextField.setText(barcode);

        if (!barcode.isBlank()) {

            try {
                //Reset Other Feilds
                searchTextField.setText("");
                productComboBox.setSelectedIndex(0);

                DefaultTableModel model = (DefaultTableModel) stockTable.getModel();
                model.setRowCount(0);

                ResultSet results = MySQL.execute("SELECT *,(`marked_price`-`selling_discount`) as `selling_price` FROM `stock` "
                        + "INNER JOIN `product` ON `stock`.`product_id`=`product`.`id` "
                        + "INNER JOIN `measurement_unit` ON `product`.`measurement_unit_id`=`measurement_unit`.`id` "
                        + "WHERE `barcode`='" + barcode + "'");

                if (results.next()) {

                    double currentQty = Double.parseDouble(results.getString("current_quantity"));
                    double buyingPrice = Double.parseDouble(results.getString("buying_price"));
                    double sellingPrice = Double.parseDouble(results.getString("selling_price"));

                    Vector v = new Vector();
                    v.add(results.getString("barcode"));
                    v.add(results.getString("product.name"));
                    v.add(Numbers.formatPrice(buyingPrice));
                    v.add(Numbers.formatPrice(sellingPrice));
                    v.add(Numbers.formatQuantity(currentQty));
                    v.add(results.getString("measurement_unit.name"));
                    model.addRow(v);
                    stockTable.setRowSelectionInterval(0, 0);
                } else {
                    JOptionPane.showMessageDialog(this, "Not Found This Barcode", "Warning", JOptionPane.WARNING_MESSAGE);
                    barcodeTextField.setText("");
                    loadStockTable();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            loadStockTable();
        }

    }

    private void selectStock() {
        int selectedRow = stockTable.getSelectedRow();

        if (selectedRow != -1) {
            String barcode = String.valueOf(stockTable.getValueAt(selectedRow, 0));
            //Set to New Grn
            if (newGRN != null) {
                newGRN.setStock(barcode);
            }else if (newInvoice != null) {
                newInvoice.setInvoiceItem(barcode);
            }else {
                JOptionPane.showMessageDialog(this, "Something Went Wrong", "Unexpected Error", JOptionPane.WARNING_MESSAGE);
            }
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row", "Unexpected Error", JOptionPane.WARNING_MESSAGE);
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
        jPanel7 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        barcodeTextField = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        searchTextField = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        productComboBox = new javax.swing.JComboBox<>();
        jPanel10 = new javax.swing.JPanel();
        clearSearchButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        stockTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        selectStockButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Select Product");
        setResizable(false);

        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        jPanel7.setForeground(new java.awt.Color(255, 51, 51));

        jLabel6.setText("Barcode");

        barcodeTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barcodeTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(barcodeTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(barcodeTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(jPanel7);

        jPanel5.setForeground(new java.awt.Color(255, 51, 51));

        jLabel3.setText("Search");

        searchTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchTextFieldKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 159, Short.MAX_VALUE))
                    .addComponent(searchTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(jPanel5);

        jPanel6.setForeground(new java.awt.Color(255, 51, 51));

        jLabel4.setText("Product");

        productComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                productComboBoxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(productComboBox, 0, 194, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(productComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(jPanel6);

        jPanel10.setForeground(new java.awt.Color(255, 51, 51));

        clearSearchButton.setBackground(new java.awt.Color(102, 102, 102));
        clearSearchButton.setForeground(new java.awt.Color(255, 255, 255));
        clearSearchButton.setText("Clear Search");
        clearSearchButton.setBorder(null);
        clearSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearSearchButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(clearSearchButton, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(28, Short.MAX_VALUE)
                .addComponent(clearSearchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.add(jPanel10);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        stockTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Barcode", "Product Name", "Buying Price", "Marked Price", "Selling Discount", "Selling Price", "current_quantity", "Measurement Unit"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        stockTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        stockTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
        .put(KeyStroke.getKeyStroke("ENTER"), "none");
        stockTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                stockTableMouseClicked(evt);
            }
        });
        stockTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                stockTableKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(stockTable);
        if (stockTable.getColumnModel().getColumnCount() > 0) {
            stockTable.getColumnModel().getColumn(0).setMinWidth(100);
            stockTable.getColumnModel().getColumn(0).setPreferredWidth(100);
            stockTable.getColumnModel().getColumn(1).setMinWidth(200);
            stockTable.getColumnModel().getColumn(1).setPreferredWidth(200);
            stockTable.getColumnModel().getColumn(2).setMinWidth(100);
            stockTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        }

        jLabel1.setFont(new java.awt.Font("Poppins", 2, 12)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("(Double Click OR Click Select Stock Button To Select the Stock)");

        selectStockButton.setForeground(new java.awt.Color(255, 255, 255));
        selectStockButton.setText("Select Stock");
        selectStockButton.setBorder(null);
        selectStockButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectStockButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 814, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selectStockButton, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(selectStockButton, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 826, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void clearSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearSearchButtonActionPerformed
        clearSearch();
    }//GEN-LAST:event_clearSearchButtonActionPerformed

    private void stockTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stockTableMouseClicked
        if (evt.getClickCount() == 2) {
            selectStock();
        }
    }//GEN-LAST:event_stockTableMouseClicked

    private void searchTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchTextFieldKeyReleased
        loadStockTable();
    }//GEN-LAST:event_searchTextFieldKeyReleased

    private void productComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_productComboBoxItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            loadStockTable();
        }
    }//GEN-LAST:event_productComboBoxItemStateChanged

    private void barcodeTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_barcodeTextFieldActionPerformed
        searchByBarcode();
    }//GEN-LAST:event_barcodeTextFieldActionPerformed

    private void selectStockButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectStockButtonActionPerformed
        selectStock();
    }//GEN-LAST:event_selectStockButtonActionPerformed

    private void stockTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_stockTableKeyReleased
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            selectStock();
        }
    }//GEN-LAST:event_stockTableKeyReleased

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
            java.util.logging.Logger.getLogger(SelectStock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SelectStock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SelectStock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SelectStock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
    private javax.swing.JTextField barcodeTextField;
    private javax.swing.JButton clearSearchButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> productComboBox;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JButton selectStockButton;
    private javax.swing.JTable stockTable;
    // End of variables declaration//GEN-END:variables
}
