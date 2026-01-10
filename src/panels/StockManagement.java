/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package panels;

import GUI.Home;
import java.sql.SQLException;
import SubGUI.AddNewStock;
import SubGUI.StockDetails;
import java.awt.event.ItemEvent;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.MySQL;
import model.Numbers;
import utils.ToastUtils;

/**
 *
 * @author Sandeep
 */
public class StockManagement extends javax.swing.JPanel {

    public Home home;
    private HashMap<Integer, String> productIdMap = new HashMap<>();

    /**
     * Creates new form ProductManagement
     */
    public StockManagement(Home home) {
        this.home = home;
        initComponents();
        loadProducts();
        loadStockTable();
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

        stockTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        stockTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        stockTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        stockTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        stockTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        stockTable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        stockTable.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
        
    }

    private void loadProducts() {
        try {
            ResultSet result = MySQL.execute("SELECT * FROM product p "
                    + "INNER JOIN measurement_unit mu ON p.measurement_unit_id = mu.id");
            Vector v = new Vector();
            v.add("All Products");

            int comboboxIndex = 1;

            while (result.next()) {
                v.add(result.getString("p.id")+" - "+result.getString("p.name"));
                productIdMap.put(comboboxIndex, result.getString("p.id"));
                comboboxIndex++;
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
            String searchText = searchTextField.getText();
            String productId = productIdMap.get(productComboBox.getSelectedIndex());

            int sortBy = sortByComboBox.getSelectedIndex();

            String sortByColumn = "";
            String sortByType = "";

            if (sortBy == 0) {
                sortByColumn = "s.created_at";
                sortByType = "DESC";
            } else if (sortBy == 1) {
                sortByColumn = "s.created_at";
                sortByType = "ASC";
            } else if (sortBy == 2) {
                sortByColumn = "p.name";
                sortByType = "ASC";
            } else if (sortBy == 3) {
                sortByColumn = "p.name";
                sortByType = "DESC";
            }

            String searchByProductQueryPart = "";
            
            if(productId != null){
                searchByProductQueryPart = "AND p.id = '"+productId+"' ";
            }

            DefaultTableModel model = (DefaultTableModel) stockTable.getModel();
            model.setRowCount(0);

            ResultSet results = MySQL.execute(""
                    + "SELECT * FROM stock s "
                    + "INNER JOIN product p ON s.product_id = p.id "
                    + "INNER JOIN measurement_unit mu ON p.measurement_unit_id=mu.id "
                    + "WHERE (p.name LIKE '%" + searchText + "%' OR s.barcode LIKE '%" + searchText + "%' OR s.created_at LIKE '%" + searchText + "%') "
                    + searchByProductQueryPart       
                    + "ORDER BY " + sortByColumn + " " + sortByType + "");

            while (results.next()) {

                Vector v = new Vector();
                v.add(results.getString("s.barcode"));
                v.add(results.getString("p.id"));
                v.add(results.getString("p.name"));
                v.add(Numbers.formatQuantity(results.getDouble("s.current_quantity")));
                v.add(Numbers.formatQuantity(results.getDouble("s.reorder_level")));
                v.add(results.getString("mu.name"));
                v.add(results.getString("s.created_at"));
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
        sortByComboBox.setSelectedIndex(0);

        loadStockTable();
    }

    private void deleteSelectedStock() {

        int selectedRow = stockTable.getSelectedRow();

        if (selectedRow != -1) {
            String barcode = String.valueOf(stockTable.getValueAt(selectedRow, 0));
            String productName = String.valueOf(stockTable.getValueAt(selectedRow, 1));
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "<html>Are you sure you want to delete, <br> <strong>" + productName + " (Barcode: " + barcode + ")?</strong></html>",
                    "Delete Stock",
                    JOptionPane.YES_NO_OPTION
            );

            if (result == JOptionPane.YES_OPTION) {
                try {
                    MySQL.execute("DELETE FROM `stock` WHERE `barcode` = '" + barcode + "'");
                    JOptionPane.showMessageDialog(this, "Product Deleted Successfully", "Success", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/resource/success.png")));
                    loadStockTable();
                } catch (SQLException e) {
                    // Check if the exception message indicates a foreign key constraint violation
                    if (e.getErrorCode() == 1451) {
                        JOptionPane.showMessageDialog(this, "This Stock linked to other records in the database.", "Cannot Delete", JOptionPane.WARNING_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Cannot delete the product. It is linked to other records.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete", "Warning", JOptionPane.WARNING_MESSAGE);

        }

    }

    private void searchByBarcode() {

        // Remove all whitespaces (spaces, tabs, newlines)
        String barcode = barcodeTextField.getText().replaceAll("\\s+", "");
        barcodeTextField.setText(barcode);

        if (!barcode.isBlank()) {

            try {
                //Reset Other Feilds
                searchTextField.setText("");
                sortByComboBox.setSelectedIndex(0);

                DefaultTableModel model = (DefaultTableModel) stockTable.getModel();
                model.setRowCount(0);

                ResultSet results = MySQL.execute("SELECT * FROM stock s "
                        + "INNER JOIN product p ON s.product_id = p.id "
                        + "INNER JOIN measurement_unit mu ON p.measurement_unit_id=mu.id "
                        + "WHERE s.barcode='" + barcode + "'");

                if (results.next()) {
                    Vector v = new Vector();
                    v.add(results.getString("s.barcode"));
                    v.add(results.getString("p.id"));
                    v.add(results.getString("p.name"));
                    v.add(Numbers.formatQuantity(results.getDouble("s.current_quantity")));
                    v.add(Numbers.formatQuantity(results.getDouble("s.reorder_level")));
                    v.add(results.getString("mu.name"));
                    v.add(results.getString("s.created_at"));
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        barcodeTextField = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        searchTextField = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        productComboBox = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        sortByComboBox = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        addNewProductButton1 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        addNewProductButton = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        stockTable = new javax.swing.JTable();

        setMinimumSize(new java.awt.Dimension(852, 617));
        setPreferredSize(new java.awt.Dimension(852, 617));

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 105, 75));
        jLabel1.setText("Stock Management");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

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
                    .addComponent(barcodeTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE))
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

        searchTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchTextFieldActionPerformed(evt);
            }
        });
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
                    .addComponent(searchTextField)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 93, Short.MAX_VALUE)))
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

        productComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                productComboBoxItemStateChanged(evt);
            }
        });

        jLabel7.setText("Product");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(productComboBox, 0, 128, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(productComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(jPanel6);

        jPanel4.setForeground(new java.awt.Color(255, 51, 51));

        sortByComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Added (Newest First)", "Added (Oldest First)", "Product Name (A-Z)", "Product Name (Z-A)" }));
        sortByComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                sortByComboBoxItemStateChanged(evt);
            }
        });

        jLabel5.setText("Sort By");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sortByComboBox, 0, 128, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sortByComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(jPanel4);

        jPanel10.setForeground(new java.awt.Color(255, 51, 51));

        addNewProductButton1.setBackground(new java.awt.Color(102, 102, 102));
        addNewProductButton1.setForeground(new java.awt.Color(255, 255, 255));
        addNewProductButton1.setText("Clear Search");
        addNewProductButton1.setBorder(null);
        addNewProductButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewProductButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addNewProductButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                .addGap(12, 12, 12))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(addNewProductButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(jPanel10);

        jPanel8.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, new java.awt.Color(204, 204, 204)));
        jPanel8.setForeground(new java.awt.Color(255, 51, 51));

        addNewProductButton.setText("New Stock");
        addNewProductButton.setToolTipText("Add New Product");
        addNewProductButton.setBorder(null);
        addNewProductButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewProductButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(addNewProductButton, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(addNewProductButton, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(jPanel8);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        stockTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Barcode", "Product Id", "Product Name", "Current Quantity", "Reorder Level", "Quantity Unit", "Added Date Time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        stockTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        stockTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                stockTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(stockTable);
        if (stockTable.getColumnModel().getColumnCount() > 0) {
            stockTable.getColumnModel().getColumn(2).setMinWidth(200);
            stockTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        }

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void sortByComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_sortByComboBoxItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            loadStockTable();
        }
    }//GEN-LAST:event_sortByComboBoxItemStateChanged

    private void addNewProductButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewProductButtonActionPerformed
        // TODO add your handling code here:
        AddNewStock addNewStock = new AddNewStock(home, true, this);
        addNewStock.setVisible(true);
    }//GEN-LAST:event_addNewProductButtonActionPerformed

    private void addNewProductButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewProductButton1ActionPerformed
        // TODO add your handling code here:
        clearSearch();
        ToastUtils.showBottomToast(home, "Search Cleared", 2000);

    }//GEN-LAST:event_addNewProductButton1ActionPerformed

    private void barcodeTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_barcodeTextFieldActionPerformed
        // TODO add your handling code here:
        searchByBarcode();
    }//GEN-LAST:event_barcodeTextFieldActionPerformed

    private void stockTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stockTableMouseClicked
        if (evt.getClickCount() == 3) {
            deleteSelectedStock();
        } else if (evt.getClickCount() == 2) {
            int selectedRow = stockTable.getSelectedRow();

            if (selectedRow != -1) {
                String barcode = String.valueOf(stockTable.getValueAt(selectedRow, 0));
                StockDetails stockDetails = new StockDetails(home, true, barcode);
                stockDetails.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row to stock details", "Warning", JOptionPane.WARNING_MESSAGE);
            }

        }
    }//GEN-LAST:event_stockTableMouseClicked

    private void searchTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchTextFieldActionPerformed
        loadStockTable();
    }//GEN-LAST:event_searchTextFieldActionPerformed

    private void searchTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchTextFieldKeyReleased
        loadStockTable();
    }//GEN-LAST:event_searchTextFieldKeyReleased

    private void productComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_productComboBoxItemStateChanged
        loadStockTable();
    }//GEN-LAST:event_productComboBoxItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addNewProductButton;
    private javax.swing.JButton addNewProductButton1;
    private javax.swing.JTextField barcodeTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> productComboBox;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JComboBox<String> sortByComboBox;
    private javax.swing.JTable stockTable;
    // End of variables declaration//GEN-END:variables
}
