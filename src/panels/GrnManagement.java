/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package panels;

import SubGUI.NewGRN;
import java.awt.event.ItemEvent;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.MySQL;
import model.Numbers;

/**
 *
 * @author Sandeep
 */
public class GrnManagement extends javax.swing.JPanel {

    /**
     * Creates new form ProductManagement
     */
    public GrnManagement() {
        initComponents();
        loadYears();
        loadGRNTable();
    }

    private void loadYears() {
        try {
            ResultSet result = MySQL.execute("SELECT * FROM `grn` ORDER BY `date_time` DESC ");
            Vector v = new Vector();
            v.add("All Years");
            while (result.next()) {
                //Get Year From Date Time
                String dateTime = result.getString("date_time");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);
                int year = localDateTime.getYear();

                if (!v.contains(year)) {
                    v.add(year);
                }
            }
            DefaultComboBoxModel model = new DefaultComboBoxModel(v);
            yearComboBox.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadGRNTable() {
        try {
            barcodeTextField.setText("");

            String search = searchTextField.getText();
            String year = String.valueOf(yearComboBox.getSelectedItem());
            int sortBy = sortByComboBox.getSelectedIndex();

            String sortByColumn = "";
            String sortByType = "";

            if (sortBy == 0) {
                sortByColumn = "`grn`.`date_time`";
                sortByType = "DESC";
            } else if (sortBy == 1) {
                sortByColumn = "`grn`.`date_time`";
                sortByType = "ASC";
            } else if (sortBy == 2) {
                sortByColumn = "`grn`.`supplier_name`";
                sortByType = "ASC";
            } else if (sortBy == 3) {
                sortByColumn = "`grn`.`supplier_name`";
                sortByType = "DESC";
            } else if (sortBy == 4) {
                sortByColumn = "`grn`.`amount`";
                sortByType = "ASC";
            } else if (sortBy == 5) {
                sortByColumn = "`grn`.`amount`";
                sortByType = "DESC";
            } else if (sortBy == 6) {
                sortByColumn = "`grn`.`discount`";
                sortByType = "ASC";
            } else if (sortBy == 7) {
                sortByColumn = "`grn`.`discount`";
                sortByType = "DESC";
            }
            String searchByYearQueryPart = "";

            if (year != "All Years") {
                searchByYearQueryPart = "AND `grn`.`date_time` LIKE '" + year + "%' ";
            }

            DefaultTableModel model = (DefaultTableModel) grnTable.getModel();
            model.setRowCount(0);

            ResultSet results = MySQL.execute(""
                    + "SELECT * FROM `grn` "
                    + "WHERE (`grn`.`barcode` LIKE '%" + search + "%' OR `grn`.`supplier_name` LIKE '%" + search + "%' OR `grn`.`note` LIKE '%" + search + "%' OR `grn`.`date_time` LIKE '%" + search + "%') "
                    + searchByYearQueryPart
                    + "ORDER BY " + sortByColumn + " " + sortByType + "");

            while (results.next()) {
                ResultSet numOfItemsResult = MySQL.execute("SELECT COUNT(*) AS `items_count` FROM `grn_item` WHERE `grn_barcode` = '" + results.getString("grn.barcode") + "'");

                if (numOfItemsResult.next()) {
                    double amount = Double.parseDouble(results.getString("amount"));
                    double discount = Double.parseDouble(results.getString("discount"));

                    Vector v = new Vector();
                    v.add(results.getString("barcode"));
                    v.add(results.getString("supplier_name"));
                    v.add(results.getString("supplier_mobile"));
                    v.add(results.getString("date_time"));
                    v.add(Numbers.formatPrice(amount));
                    v.add(Numbers.formatPrice(discount));
                    v.add(numOfItemsResult.getString("items_count"));
                    model.addRow(v);
                } else {
                    JOptionPane.showMessageDialog(this, "An unexpected error has occurred. Please try again later or contact support if the issue persists.", "Unexpected Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearSearch() {
        barcodeTextField.setText("");
        searchTextField.setText("");
        yearComboBox.setSelectedIndex(0);
        sortByComboBox.setSelectedIndex(0);

    }

    private void searchByBarcode() {

        // Remove all whitespaces (spaces, tabs, newlines)
        String barcode = barcodeTextField.getText().replaceAll("\\s+", "");
        barcodeTextField.setText(barcode);

        if (!barcode.isBlank()) {

            try {
                //Reset Other Feilds
                searchTextField.setText("");
                yearComboBox.setSelectedIndex(0);
                sortByComboBox.setSelectedIndex(0);

                DefaultTableModel model = (DefaultTableModel) grnTable.getModel();
                model.setRowCount(0);

                ResultSet results = MySQL.execute("SELECT * FROM `grn` "
                        + "WHERE `barcode`='" + barcode + "'");

                if (results.next()) {
                    ResultSet numOfItemsResult = MySQL.execute("SELECT COUNT(*) AS `items_count` FROM `grn_item` WHERE `grn_barcode` = '" + results.getString("grn.barcode") + "'");

                    if (numOfItemsResult.next()) {
                        double amount = Double.parseDouble(results.getString("amount"));
                        double discount = Double.parseDouble(results.getString("discount"));

                        Vector v = new Vector();
                        v.add(results.getString("barcode"));
                        v.add(results.getString("supplier_name"));
                        v.add(results.getString("note"));
                        v.add(results.getString("date_time"));
                        v.add(Numbers.formatPriceWithCurrencyCode(amount));
                        v.add(Numbers.formatPriceWithCurrencyCode(discount));
                        v.add(numOfItemsResult.getString("items_count"));
                        model.addRow(v);
                    } else {
                        JOptionPane.showMessageDialog(this, "An unexpected error has occurred. Please try again later or contact support if the issue persists.", "Unexpected Error", JOptionPane.ERROR_MESSAGE);
                    }

                } else {
                    JOptionPane.showMessageDialog(this, "Not Found This Barcode", "Warning", JOptionPane.WARNING_MESSAGE);
                    barcodeTextField.setText("");
                    loadGRNTable();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "An unexpected error has occurred. Please try again later or contact support if the issue persists.", "Unexpected Error", JOptionPane.ERROR_MESSAGE);

            }
        } else {
            JOptionPane.showMessageDialog(this, "Please Enter Barcode First", "Warning", JOptionPane.WARNING_MESSAGE);
            loadGRNTable();
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

        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        barcodeTextField = new javax.swing.JTextField();
        newInvoiceButton1 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        searchTextField = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        yearComboBox = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        sortByComboBox = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        clearSearchButton = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        grnTable = new javax.swing.JTable();

        setMinimumSize(new java.awt.Dimension(852, 617));
        setPreferredSize(new java.awt.Dimension(852, 617));

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 105, 75));
        jLabel1.setText("Goods Received Notes Management");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        jPanel7.setForeground(new java.awt.Color(255, 51, 51));

        jLabel6.setText("GRN Barcode");

        barcodeTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barcodeTextFieldActionPerformed(evt);
            }
        });

        newInvoiceButton1.setBackground(new java.awt.Color(102, 102, 102));
        newInvoiceButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/search.png"))); // NOI18N
        newInvoiceButton1.setBorder(null);
        newInvoiceButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newInvoiceButton1ActionPerformed(evt);
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
                        .addGap(0, 15, Short.MAX_VALUE))
                    .addComponent(barcodeTextField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newInvoiceButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(newInvoiceButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(barcodeTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel2.add(jPanel7);

        jPanel5.setForeground(new java.awt.Color(255, 51, 51));

        jLabel3.setText("Search");

        searchTextField.setToolTipText("You can search using GRN ID, Supplier Name, Suppier Mobile And Added Date Time.");
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
                        .addGap(0, 93, Short.MAX_VALUE))
                    .addComponent(searchTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE))
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

        jPanel3.setForeground(new java.awt.Color(255, 51, 51));

        yearComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        yearComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                yearComboBoxItemStateChanged(evt);
            }
        });

        jLabel4.setText("Year");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(yearComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 105, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(yearComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(jPanel3);

        jPanel4.setForeground(new java.awt.Color(255, 51, 51));

        sortByComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Newest to Oldest", "Oldest to Newest", "Supplier Name (A-Z)", "Supplier Name (Z-A)", "Amount (Low to High)", "Amount (High to Low)", "Discount (Low to High)", "Discount (High to Low)" }));
        sortByComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                sortByComboBoxItemStateChanged(evt);
            }
        });
        sortByComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortByComboBoxActionPerformed(evt);
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
                .addComponent(clearSearchButton, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                .addGap(12, 12, 12))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(clearSearchButton, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(jPanel10);

        jPanel8.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, new java.awt.Color(204, 204, 204)));
        jPanel8.setForeground(new java.awt.Color(255, 51, 51));

        jButton2.setText("New GRN");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(jPanel8);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        grnTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Barcode", "Supplier", "Supplier Mobile", "Added Date Time", "Amount (Rs.)", "Discount (Rs.)", "Number Of Items"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        grnTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(grnTable);
        if (grnTable.getColumnModel().getColumnCount() > 0) {
            grnTable.getColumnModel().getColumn(3).setMinWidth(200);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void searchTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchTextFieldKeyReleased
        loadGRNTable();
    }//GEN-LAST:event_searchTextFieldKeyReleased

    private void yearComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_yearComboBoxItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            loadGRNTable();
        }
    }//GEN-LAST:event_yearComboBoxItemStateChanged

    private void sortByComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_sortByComboBoxItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            loadGRNTable();
        }
    }//GEN-LAST:event_sortByComboBoxItemStateChanged

    private void clearSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearSearchButtonActionPerformed
        // TODO add your handling code here:
        clearSearch();
    }//GEN-LAST:event_clearSearchButtonActionPerformed

    private void barcodeTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_barcodeTextFieldActionPerformed
        searchByBarcode();
    }//GEN-LAST:event_barcodeTextFieldActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        new NewGRN(this).setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void newInvoiceButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newInvoiceButton1ActionPerformed
        searchByBarcode();
    }//GEN-LAST:event_newInvoiceButton1ActionPerformed

    private void sortByComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortByComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sortByComboBoxActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField barcodeTextField;
    private javax.swing.JButton clearSearchButton;
    private javax.swing.JTable grnTable;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton newInvoiceButton1;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JComboBox<String> sortByComboBox;
    private javax.swing.JComboBox<String> yearComboBox;
    // End of variables declaration//GEN-END:variables
}
