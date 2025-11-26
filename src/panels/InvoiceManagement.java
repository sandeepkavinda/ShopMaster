/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package panels;

import SubGUI.InvoiceDetails;
import SubGUI.NewInvoice;
import SubGUI.StockDetails;
import java.awt.event.ItemEvent;
import model.MySQL;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.Numbers;

/**
 *
 * @author Sandeep
 */
public class InvoiceManagement extends javax.swing.JPanel {

    private HashMap<String, String> paymentMethodMap = new HashMap<>();

    /**
     * Creates new form ProductManagement
     */
    public InvoiceManagement() {
        initComponents();
        loadPaymentMethods();
        loadInvoiceTable();

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

        invoiceTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        invoiceTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        invoiceTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        invoiceTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        invoiceTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        invoiceTable.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
        invoiceTable.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
        invoiceTable.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
        invoiceTable.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);
        invoiceTable.getColumnModel().getColumn(9).setCellRenderer(centerRenderer);
        invoiceTable.getColumnModel().getColumn(10).setCellRenderer(centerRenderer);

    }

    private void loadPaymentMethods() {
        try {
            ResultSet result = MySQL.execute("SELECT * FROM `payment_method`");
            Vector v = new Vector();
            v.add("All Payment Methods");
            while (result.next()) {
                v.add(result.getString("name"));
                paymentMethodMap.put(result.getString("name"), result.getString("id"));
            }
            DefaultComboBoxModel model = new DefaultComboBoxModel(v);
            paymentMethodComboBox.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An unexpected error has occurred. Please try again later or contact support if the issue persists.", "Unexpected Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void loadInvoiceTable() {
        try {
            invoiceIdTextField.setText("");

            String search = searchTextField.getText();
            String paymentMethodId = paymentMethodMap.get(String.valueOf(paymentMethodComboBox.getSelectedItem()));
            int sortBy = sortByComboBox.getSelectedIndex();

            String sortByColumn = "";
            String sortByType = "";

            if (sortBy == 0) {
                sortByColumn = "datetime";
                sortByType = "DESC";
            } else if (sortBy == 1) {
                sortByColumn = "datetime";
                sortByType = "ASC";
            } else if (sortBy == 2) {
                sortByColumn = "total_amount";
                sortByType = "DESC";
            } else if (sortBy == 3) {
                sortByColumn = "total_amount";
                sortByType = "ASC";
            } else if (sortBy == 4) {
                sortByColumn = "discount";
                sortByType = "DESC";
            } else if (sortBy == 5) {
                sortByColumn = "discount";
                sortByType = "ASC";
            } else if (sortBy == 6) {
                sortByColumn = "net_total";
                sortByType = "DESC";
            } else if (sortBy == 7) {
                sortByColumn = "net_total";
                sortByType = "ASC";
            } else if (sortBy == 8) {
                sortByColumn = "return_payment_amount";
                sortByType = "DESC";
            } else if (sortBy == 9) {
                sortByColumn = "i.return_payment_amount";
                sortByType = "ASC";
            } else if (sortBy == 10) {
                sortByColumn = "payable_amount";
                sortByType = "DESC";
            } else if (sortBy == 11) {
                sortByColumn = "payable_amount";
                sortByType = "ASC";
            } else if (sortBy == 12) {
                sortByColumn = "paid_amount";
                sortByType = "DESC";
            } else if (sortBy == 13) {
                sortByColumn = "paid_amount";
                sortByType = "ASC";
            } else if (sortBy == 14) {
                sortByColumn = "balance";
                sortByType = "DESC";
            } else if (sortBy == 15) {
                sortByColumn = "balance";
                sortByType = "ASC";
            } else if (sortBy == 16) {
                sortByColumn = "item_count";
                sortByType = "DESC";
            } else if (sortBy == 17) {
                sortByColumn = "item_count";
                sortByType = "ASC";
            }

            String searchByPaymentMethodQueryPart = "";

            if (paymentMethodId != null) {
                searchByPaymentMethodQueryPart = "AND i.payment_method_id = '" + paymentMethodId + "%' ";
            }

            DefaultTableModel model = (DefaultTableModel) invoiceTable.getModel();
            model.setRowCount(0);

            ResultSet results = MySQL.execute(""
                    + "SELECT * FROM invoice i "
                    + "INNER JOIN payment_method pm ON i.payment_method_id = pm.id  "
                    + "WHERE (i.invoice_id LIKE '%" + search + "%' OR i.datetime LIKE '%" + search + "%') "
                    + searchByPaymentMethodQueryPart
                    + "ORDER BY " + sortByColumn + " " + sortByType + " ");

            while (results.next()) {
                Vector v = new Vector();
                v.add(results.getString("i.invoice_id"));
                v.add(Numbers.formatPrice(Double.parseDouble(results.getString("total_amount"))));
                v.add(Numbers.formatPrice(Double.parseDouble(results.getString("discount"))));
                v.add(Numbers.formatPrice(Double.parseDouble(results.getString("net_total"))));
                v.add(Numbers.formatPrice(Double.parseDouble(results.getString("return_payment_amount"))));
                v.add(Numbers.formatPrice(Double.parseDouble(results.getString("payable_amount"))));
                v.add(Numbers.formatPrice(Double.parseDouble(results.getString("paid_amount"))));
                v.add(results.getString("balance"));
                v.add(results.getString("pm.name"));
                v.add(results.getString("i.item_count"));
                v.add(results.getString("datetime"));
                model.addRow(v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearSearch() {
        invoiceIdTextField.setText("");
        searchTextField.setText("");
        paymentMethodComboBox.setSelectedIndex(0);
        sortByComboBox.setSelectedIndex(0);
        loadInvoiceTable();
    }

    private void searchByBarcode() {

        // Remove all whitespaces (spaces, tabs, newlines)
        String invoiceId = invoiceIdTextField.getText().replaceAll("\\s+", "");

        if (!invoiceId.isBlank()) {

            try {
                //Reset Other Feilds
                clearSearch();
                invoiceIdTextField.setText(invoiceId);

                DefaultTableModel model = (DefaultTableModel) invoiceTable.getModel();
                model.setRowCount(0);

                ResultSet results = MySQL.execute(""
                        + "SELECT * FROM invoice i "
                        + "INNER JOIN payment_method pm ON i.payment_method_id = pm.id  "
                        + "WHERE i.invoice_id = '" + invoiceId + "' ");

                if (results.next()) {

                    Vector v = new Vector();
                    v.add(results.getString("i.invoice_id"));
                    v.add(Numbers.formatPrice(Double.parseDouble(results.getString("total_amount"))));
                    v.add(Numbers.formatPrice(Double.parseDouble(results.getString("discount"))));
                    v.add(Numbers.formatPrice(Double.parseDouble(results.getString("net_total"))));
                    v.add(Numbers.formatPrice(Double.parseDouble(results.getString("return_payment_amount"))));
                    v.add(Numbers.formatPrice(Double.parseDouble(results.getString("payable_amount"))));
                    v.add(Numbers.formatPrice(Double.parseDouble(results.getString("paid_amount"))));
                    v.add(results.getString("balance"));
                    v.add(results.getString("pm.name"));
                    v.add(results.getString("i.item_count"));
                    v.add(results.getString("datetime"));
                    model.addRow(v);

                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Barcode/Id", "Warning", JOptionPane.WARNING_MESSAGE);
                    clearSearch();
                    loadInvoiceTable();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "An unexpected error has occurred. Please try again later or contact support if the issue persists.", "Unexpected Error", JOptionPane.ERROR_MESSAGE);

            }
        } else {
            JOptionPane.showMessageDialog(this, "Please Enter Barcode First", "Warning", JOptionPane.WARNING_MESSAGE);
            loadInvoiceTable();
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
        invoiceIdTextField = new javax.swing.JTextField();
        newInvoiceButton1 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        searchTextField = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        paymentMethodComboBox = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        sortByComboBox = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        clearSearchButton = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        newInvoiceButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        invoiceTable = new javax.swing.JTable();

        setMinimumSize(new java.awt.Dimension(852, 617));
        setPreferredSize(new java.awt.Dimension(852, 617));

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 105, 75));
        jLabel1.setText("Invoice  Management");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        jPanel7.setForeground(new java.awt.Color(255, 51, 51));

        jLabel6.setText("Invoice Barcode/ Id");

        invoiceIdTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invoiceIdTextFieldActionPerformed(evt);
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
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(invoiceIdTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(newInvoiceButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                    .addComponent(invoiceIdTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel2.add(jPanel7);

        jPanel5.setForeground(new java.awt.Color(255, 51, 51));

        jLabel7.setText("Search");

        searchTextField.setToolTipText("You can search using Invoice ID and issued date and time.");
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
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(searchTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(jPanel5);

        jPanel3.setForeground(new java.awt.Color(255, 51, 51));

        paymentMethodComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All" }));
        paymentMethodComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                paymentMethodComboBoxItemStateChanged(evt);
            }
        });

        jLabel4.setText("Payment Method");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(paymentMethodComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 36, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(paymentMethodComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(jPanel3);

        jPanel4.setForeground(new java.awt.Color(255, 51, 51));

        sortByComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Newest to Oldest", "Oldest to Newest", "Total (High to Low)", "Total (Low to High)", "Discount (High to Low)", "Discount (Low to High)", "Net Total (High to Low)", "Net Total (Low to High)", "Return Payment (High to Low)", "Return Payment (Low to High)", "Payable Amount (High to Low)", "Payable Amount (Low to High)", "Paid Amount (High to Low)", "Paid Amount (Low to High)", "Balance (High to Low)", "Balance (Low to High)", "Item Count (High to Low)", "Item Count (Low to High)" }));
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

        jPanel8.setForeground(new java.awt.Color(255, 51, 51));

        clearSearchButton.setBackground(new java.awt.Color(102, 102, 102));
        clearSearchButton.setForeground(new java.awt.Color(255, 255, 255));
        clearSearchButton.setText("Clear Search");
        clearSearchButton.setBorder(null);
        clearSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearSearchButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(clearSearchButton, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(clearSearchButton, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(jPanel8);

        jPanel6.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, new java.awt.Color(204, 204, 204)));
        jPanel6.setForeground(new java.awt.Color(255, 51, 51));

        newInvoiceButton.setText("New Invoice");
        newInvoiceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newInvoiceButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(newInvoiceButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(newInvoiceButton, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(jPanel6);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        invoiceTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Invoice Id", "Total", "Discount", "Net Total", "Return Payment", "Payable Amount", "Paid Amount", "Balance", "Payment Method", "Item Count", "Issued Date Time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        invoiceTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        invoiceTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                invoiceTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(invoiceTable);
        if (invoiceTable.getColumnModel().getColumnCount() > 0) {
            invoiceTable.getColumnModel().getColumn(10).setMinWidth(150);
            invoiceTable.getColumnModel().getColumn(10).setPreferredWidth(150);
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

    private void clearSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearSearchButtonActionPerformed
        clearSearch();
    }//GEN-LAST:event_clearSearchButtonActionPerformed

    private void invoiceIdTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invoiceIdTextFieldActionPerformed
        searchByBarcode();
    }//GEN-LAST:event_invoiceIdTextFieldActionPerformed

    private void newInvoiceButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newInvoiceButton1ActionPerformed
        searchByBarcode();
    }//GEN-LAST:event_newInvoiceButton1ActionPerformed

    private void searchTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchTextFieldKeyReleased
        loadInvoiceTable();
    }//GEN-LAST:event_searchTextFieldKeyReleased

    private void paymentMethodComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_paymentMethodComboBoxItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            loadInvoiceTable();
        }
    }//GEN-LAST:event_paymentMethodComboBoxItemStateChanged

    private void sortByComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_sortByComboBoxItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            loadInvoiceTable();
        }
    }//GEN-LAST:event_sortByComboBoxItemStateChanged

    private void newInvoiceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newInvoiceButtonActionPerformed
        NewInvoice newInvoice = new NewInvoice(this);
        newInvoice.setVisible(true);
    }//GEN-LAST:event_newInvoiceButtonActionPerformed

    private void invoiceTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_invoiceTableMouseClicked
       if (evt.getClickCount() == 2) {
            int selectedRow = invoiceTable.getSelectedRow();

            if (selectedRow != -1) {
                String invoiceId = String.valueOf(invoiceTable.getValueAt(selectedRow, 0));
                InvoiceDetails invoiceDetails = new InvoiceDetails(invoiceId);
                invoiceDetails.setVisible(true);
            }

        }
    }//GEN-LAST:event_invoiceTableMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton clearSearchButton;
    private javax.swing.JTextField invoiceIdTextField;
    private javax.swing.JTable invoiceTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton newInvoiceButton;
    private javax.swing.JButton newInvoiceButton1;
    private javax.swing.JComboBox<String> paymentMethodComboBox;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JComboBox<String> sortByComboBox;
    // End of variables declaration//GEN-END:variables
}
