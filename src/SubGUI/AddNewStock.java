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
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.Generate;
import model.MySQL;
import model.Numbers;
import panels.StockManagement;

/**
 *
 * @author Sandeep
 */
public class AddNewStock extends javax.swing.JDialog {

    HashMap<Integer, String> productIdMap = new HashMap<>();
    HashMap<String, Integer> comboBoxIndexMap = new HashMap<>();
    HashMap<String, String> productMeasurementUnitMap = new HashMap<>();

    StockManagement stockManagement;
    java.awt.Frame parent;

    /**
     * Creates new form AddNewProduct
     */
    public AddNewStock(java.awt.Frame parent, boolean modal, StockManagement stockManagement) {
        super(parent, modal);
        this.parent = parent;
        this.stockManagement = stockManagement;
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resource/icon.png")));
        initComponents();
        loadProducts();
        loadStockTable();

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        stockTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        stockTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        stockTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        stockTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
    }

    private void loadProducts() {
        try {
            ResultSet result = MySQL.execute("SELECT * FROM product p "
                    + "INNER JOIN measurement_unit mu ON p.measurement_unit_id = mu.id");
            Vector v = new Vector();
            v.add("Select");

            int comboboxIndex = 1;

            while (result.next()) {
                v.add(result.getString("p.name"));
                productIdMap.put(comboboxIndex, result.getString("p.id"));
                comboBoxIndexMap.put(result.getString("p.id"), comboboxIndex);
                productMeasurementUnitMap.put(result.getString("p.id"), result.getString("mu.name"));
                comboboxIndex++;
            }

            DefaultComboBoxModel model = new DefaultComboBoxModel(v);
            productComboBox.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setProduct(String id) {
        loadProducts();
        int comboboxIndex = comboBoxIndexMap.get(id);
        productComboBox.setSelectedIndex(comboboxIndex);
    }

    private void loadStockTable() {
        try {

            DefaultTableModel model = (DefaultTableModel) stockTable.getModel();
            model.setRowCount(0);

            ResultSet results = MySQL.execute("SELECT * FROM stock s "
                    + "INNER JOIN product p ON s.product_id = p.id "
                    + "INNER JOIN measurement_unit mu ON p.measurement_unit_id=mu.id "
                    + "ORDER BY s.created_at DESC");

            while (results.next()) {

                Vector v = new Vector();

                v.add(results.getString("s.barcode"));
                v.add(results.getString("p.id"));
                v.add(results.getString("p.name"));
                v.add(Numbers.formatQuantity(results.getDouble("s.reorder_level")));
                v.add(results.getString("mu.name"));

                model.addRow(v);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetData() {
        stockBarcodeFormattedTextField.setText("");
        productComboBox.setSelectedIndex(0);
        reorderLevelFormattedTextField.setText("0");
        stockBarcodeFormattedTextField.grabFocus();

        loadProducts();
        loadStockTable();
    }

    private void changeMeasurementUnitToSelectedProduct() {

        int selectedIndex = productComboBox.getSelectedIndex();

        if (selectedIndex != 0) {
            String selectedProductId = productIdMap.get(selectedIndex);
            String measUnitName = productMeasurementUnitMap.get(selectedProductId);
            reorderLevelLabel.setText("Reorder Level (" + measUnitName + ")");
        }

    }
    
    private void generateUniqueBarcode() {

        String generatedBarcode = Generate.GenerateBarcode();
        boolean isUniqueBarcode = false;

        try {
            while (!isUniqueBarcode) {
                ResultSet resultSet = MySQL.execute("SELECT * FROM `stock` WHERE `barcode`='" + generatedBarcode + "'");

                if (resultSet.next()) {
                    generatedBarcode = Generate.GenerateBarcode();
                } else {
                    stockBarcodeFormattedTextField.setText(generatedBarcode);
                    isUniqueBarcode = true;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
    
    private void addStock() {

        String stockBarcode = stockBarcodeFormattedTextField.getText();
        String productId = productIdMap.get(productComboBox.getSelectedIndex());
        String reorderLevel = reorderLevelFormattedTextField.getText();

        if (stockBarcode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter the Stock Barcode", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (stockBarcode.length() > 20) {
            JOptionPane.showMessageDialog(this, "Stock barcode cannot be longer than 20 characters", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (productId == null) {
            JOptionPane.showMessageDialog(this, "Please Select the Product", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (reorderLevel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter Reorder Level", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                ResultSet resultSet = MySQL.execute("SELECT * FROM stock WHERE barcode = '" + stockBarcode + "' ");

                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(this, "The entered stock barcode already exists in the system.", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {

                    try {
                        Double.parseDouble(reorderLevel);

                        try {
                            MySQL.execute("INSERT INTO stock (barcode,product_id,reorder_level,current_quantity) "
                                    + "VALUES ('" + stockBarcode + "','" + productId + "','" + reorderLevel + "','0')");
                            JOptionPane.showMessageDialog(this, "Stock Added Successfully", "Success", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/resource/success.png")));
                            resetData();
                            stockManagement.loadStockTable();
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(this, "An unexpected error has occurred. Please try again later or contact support if the issue persists.", "Unexpected Error", JOptionPane.ERROR_MESSAGE);
                            e.printStackTrace();
                        }

                    } catch (NumberFormatException e) {
                        // invalid double
                        JOptionPane.showMessageDialog(this, "Invalid Reorder Level", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "An unexpected error has occurred. Please try again later or contact support if the issue persists.", "Unexpected Error", JOptionPane.ERROR_MESSAGE);
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
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        productComboBox = new javax.swing.JComboBox<>();
        addNewStockButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        addNewProductButton2 = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        stockBarcodeFormattedTextField = new javax.swing.JFormattedTextField();
        reorderLevelLabel = new javax.swing.JLabel();
        reorderLevelFormattedTextField = new javax.swing.JFormattedTextField();
        generateBarcodeButton = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        stockTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add New Stock");
        setMinimumSize(new java.awt.Dimension(700, 500));
        setResizable(false);

        jLabel6.setText("Stock Barcode");

        jLabel7.setText("Product");

        productComboBox.setPreferredSize(new java.awt.Dimension(72, 35));
        productComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                productComboBoxItemStateChanged(evt);
            }
        });
        productComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                productComboBoxActionPerformed(evt);
            }
        });

        addNewStockButton.setText("Add Stock");
        addNewStockButton.setBorder(null);
        addNewStockButton.setPreferredSize(new java.awt.Dimension(99, 35));
        addNewStockButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewStockButtonActionPerformed(evt);
            }
        });

        jSeparator1.setForeground(new java.awt.Color(204, 204, 204));

        addNewProductButton2.setBackground(new java.awt.Color(255, 255, 255));
        addNewProductButton2.setForeground(new java.awt.Color(0, 105, 75));
        addNewProductButton2.setText("Go to Add Product");
        addNewProductButton2.setBorder(null);
        addNewProductButton2.setPreferredSize(new java.awt.Dimension(99, 35));
        addNewProductButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewProductButton2ActionPerformed(evt);
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

        stockBarcodeFormattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        stockBarcodeFormattedTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stockBarcodeFormattedTextFieldActionPerformed(evt);
            }
        });

        reorderLevelLabel.setText("Reorder Level");

        reorderLevelFormattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#.###"))));
        reorderLevelFormattedTextField.setText("0");
        reorderLevelFormattedTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reorderLevelFormattedTextFieldActionPerformed(evt);
            }
        });

        generateBarcodeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/refresh.png"))); // NOI18N
        generateBarcodeButton.setToolTipText("Generate Unique Barcode");
        generateBarcodeButton.setBorder(null);
        generateBarcodeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateBarcodeButtonActionPerformed(evt);
            }
        });

        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/add.png"))); // NOI18N
        jButton2.setToolTipText("Select Product");
        jButton2.setBorder(null);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addNewStockButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addComponent(addNewProductButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(resetButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(reorderLevelFormattedTextField)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(stockBarcodeFormattedTextField, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(0, 159, Short.MAX_VALUE))
                            .addComponent(productComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(generateBarcodeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(reorderLevelLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(generateBarcodeButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(stockBarcodeFormattedTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(productComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reorderLevelLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reorderLevelFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(addNewStockButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resetButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addNewProductButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9))
        );

        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        stockTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Barcode", "Product Id", "Product Name", "Reorder Level", "Measurement Unit"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(stockTable);
        if (stockTable.getColumnModel().getColumnCount() > 0) {
            stockTable.getColumnModel().getColumn(2).setMinWidth(160);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 638, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 105, 75));
        jLabel1.setText("Add New Stock");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

        jSeparator2.setForeground(new java.awt.Color(204, 204, 204));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 105, 75));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/logo-extra-sm.png"))); // NOI18N
        jLabel2.setIconTextGap(20);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
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
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void addNewStockButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewStockButtonActionPerformed
        addStock();
    }//GEN-LAST:event_addNewStockButtonActionPerformed

    private void addNewProductButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewProductButton2ActionPerformed
        AddNewProduct addNewProduct = new AddNewProduct(stockManagement.home, true, null, this);
        addNewProduct.setVisible(true);
    }//GEN-LAST:event_addNewProductButton2ActionPerformed

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        resetData();
    }//GEN-LAST:event_resetButtonActionPerformed

    private void generateBarcodeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateBarcodeButtonActionPerformed
       generateUniqueBarcode();
    }//GEN-LAST:event_generateBarcodeButtonActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        SelectProduct selectProduct = new SelectProduct(parent, true, this);
        selectProduct.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void stockBarcodeFormattedTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stockBarcodeFormattedTextFieldActionPerformed
        productComboBox.grabFocus();
    }//GEN-LAST:event_stockBarcodeFormattedTextFieldActionPerformed

    private void productComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_productComboBoxActionPerformed
        reorderLevelFormattedTextField.grabFocus();
        reorderLevelFormattedTextField.selectAll();
    }//GEN-LAST:event_productComboBoxActionPerformed

    private void reorderLevelFormattedTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reorderLevelFormattedTextFieldActionPerformed
        addNewStockButton.grabFocus();
    }//GEN-LAST:event_reorderLevelFormattedTextFieldActionPerformed

    private void productComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_productComboBoxItemStateChanged
        changeMeasurementUnitToSelectedProduct();
    }//GEN-LAST:event_productComboBoxItemStateChanged

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
            java.util.logging.Logger.getLogger(AddNewStock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddNewStock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddNewStock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddNewStock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
    private javax.swing.JButton addNewProductButton2;
    private javax.swing.JButton addNewStockButton;
    private javax.swing.JButton generateBarcodeButton;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JComboBox<String> productComboBox;
    private javax.swing.JFormattedTextField reorderLevelFormattedTextField;
    private javax.swing.JLabel reorderLevelLabel;
    private javax.swing.JButton resetButton;
    private javax.swing.JFormattedTextField stockBarcodeFormattedTextField;
    private javax.swing.JTable stockTable;
    // End of variables declaration//GEN-END:variables
}
