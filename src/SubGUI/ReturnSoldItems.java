package SubGUI;

import java.awt.Toolkit;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.IdGenerater;
import model.Numbers;
import model.MySQL;
import panels.ReturnManagement;

/**
 *
 * @author Sandeep
 */
public class ReturnSoldItems extends javax.swing.JFrame {

    //Inisilize Varables
    private HashMap<String, Integer> returningItemRowNumberMap = new HashMap<String, Integer>();
    private HashMap<String, Integer> invoiceItemRowNumberMap = new HashMap<String, Integer>();
    private HashMap<String, String> invoiceItemIdMap = new HashMap<String, String>();
    private String invoiceId;
    private double returningTotal;
    private ReturnManagement returnManagement;
    private ResultSet results;

    public ReturnSoldItems(ReturnManagement returnManagement, ResultSet results) {
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resource/icon.png")));
        initComponents();
        this.returnManagement = returnManagement;
        this.results = results;

        try {
            invoiceId = results.getString("invoice_id");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An unexpected error has occurred. Please try again later or contact support if the issue persists.", "Unexpected Error", JOptionPane.ERROR_MESSAGE);

        }
        loadInvoiceDetails();
        loadInvoiceItems();
        itemBarcodeTextField.grabFocus();
    }

    private void loadInvoiceDetails() {
        try {
            DefaultTableModel model = (DefaultTableModel) invoiceDataTable.getModel();
            model.setRowCount(0);

            Vector v = new Vector();

            v.add(results.getString("invoice_id"));
            v.add(results.getString("datetime"));
            v.add(results.getString("item_count"));
            v.add(Numbers.formatPrice(Double.parseDouble(results.getString("total_amount"))));
            v.add(Numbers.formatPrice(Double.parseDouble(results.getString("discount"))));
            v.add(Numbers.formatPrice(Double.parseDouble(results.getString("net_total"))));
            v.add(Numbers.formatPrice(Double.parseDouble(results.getString("return_payment_amount"))));
            v.add(Numbers.formatPrice(Double.parseDouble(results.getString("payable_amount"))));
            v.add(Numbers.formatPrice(Double.parseDouble(results.getString("paid_amount"))));
            v.add(results.getString("balance"));
            v.add(results.getString("pm.name"));
            model.addRow(v);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An unexpected error has occurred. Please try again later or contact support if the issue persists.", "Unexpected Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void loadInvoiceItems() {
        try {
            DefaultTableModel model = (DefaultTableModel) invoiceItemsTable.getModel();
            model.setRowCount(0);

            ResultSet results = MySQL.execute(""
                    + "SELECT * FROM invoice_item ii "
                    + "INNER JOIN stock s ON ii.stock_barcode = s.barcode  "
                    + "INNER JOIN product p ON s.product_id = p.id  "
                    + "INNER JOIN measurement_unit mu ON p.measurement_unit_id = mu.id  "
                    + "WHERE ii.invoice_id = '" + invoiceId + "' ");

            int rowNumber = 0;

            while (results.next()) {
                Vector v = new Vector();
                v.add(results.getString("stock_barcode"));
                v.add(results.getString("p.name"));
                v.add(Numbers.formatPrice(results.getDouble("selling_price")));
                v.add(Numbers.formatQuantity(results.getDouble("quantity")));
                v.add(Numbers.formatQuantity(results.getDouble("returned_quantity")));
                v.add(Numbers.formatQuantity(results.getDouble("returnable_quantity")));
                v.add(results.getString("mu.name"));
                v.add(Numbers.formatPrice(results.getDouble("selling_price") * results.getDouble("quantity")));

                // Load Invoice Item Row Map
                invoiceItemRowNumberMap.put(results.getString("stock_barcode"), rowNumber);
                rowNumber++;

                // Load Invoice Item Map
                invoiceItemIdMap.put(results.getString("stock_barcode"), results.getString("ii.id"));

                model.addRow(v);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An unexpected error has occurred. Please try again later or contact support if the issue persists.", "Unexpected Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void addToRetrurn() {

        try {
            String itemBarcode = itemBarcodeTextField.getText();
            double returningQty = Double.parseDouble(returningQtyTextField.getText());
            updateReturnData(itemBarcode, returningQty);

        } catch (NumberFormatException e) {
            // Invalid double
            JOptionPane.showMessageDialog(this, "Invalid Returning Quantity", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void updateReturnData(String itemBarcode, double returningQuantity) {

        try {

            ResultSet results = MySQL.execute(""
                    + "SELECT * FROM invoice_item ii "
                    + "INNER JOIN stock s ON ii.stock_barcode = s.barcode  "
                    + "INNER JOIN product p ON s.product_id = p.id  "
                    + "INNER JOIN measurement_unit mu ON p.measurement_unit_id = mu.id  "
                    + "WHERE ii.invoice_id = '" + invoiceId + "' AND ii.stock_barcode = '" + itemBarcode + "' ");

            if (results.next()) {

                if (returningItemRowNumberMap.containsKey(itemBarcode)) {
                    // Already Added Row
                    int rowNumber = returningItemRowNumberMap.get(itemBarcode);
                    double currentQty = Double.parseDouble(String.valueOf(returningItemsTable.getValueAt(rowNumber, 3)));
                    double newQuantity = currentQty + returningQuantity;

                    if (results.getDouble("returnable_quantity") >= newQuantity) {
                        //Valid for return
                        returningItemsTable.setValueAt(Numbers.formatQuantity(newQuantity), rowNumber, 3);
                        returningItemsTable.setRowSelectionInterval(rowNumber, rowNumber);
                        itemBarcodeTextField.grabFocus();
                        calculateTotal();
                        clearInputs();
                    } else {
                        JOptionPane.showMessageDialog(this, "Entered quantity exceeds the available returnable quantity.", "Quantity exceeded", JOptionPane.WARNING_MESSAGE);
                    }

                } else {
                    // New Row
                    if (results.getDouble("returnable_quantity") >= returningQuantity) {
                        //Valid for return
                        DefaultTableModel model = (DefaultTableModel) returningItemsTable.getModel();
                        int rowCount = returningItemsTable.getRowCount();

                        //Input To Invoice items Table
                        Vector v = new Vector();
                        v.add(results.getString("ii.stock_barcode"));
                        v.add(results.getString("p.name"));
                        v.add(Numbers.formatPrice(results.getDouble("ii.selling_price")));
                        v.add(Numbers.formatQuantity(returningQuantity));
                        v.add(results.getString("mu.name"));
                        model.addRow(v);

                        //Add To Row Numbers Hashmap
                        updateRowNumberMap();
                        returningItemsTable.setRowSelectionInterval(rowCount, rowCount);
                        itemBarcodeTextField.grabFocus();
                        calculateTotal();
                        clearInputs();

                    } else {
                        JOptionPane.showMessageDialog(this, "Entered quantity exceeds the available returnable quantity.", "Quantity exceeded", JOptionPane.WARNING_MESSAGE);
                    }
                }

            } else {
                JOptionPane.showMessageDialog(this, "Item not found in this invoice.", "Invalid Item", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An unexpected error has occurred. Please try again later or contact support if the issue persists.", "Unexpected Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void updateRowNumberMap() {
        int newRowcount = returningItemsTable.getRowCount();
        returningItemRowNumberMap.clear();
        for (int i = 0; i < newRowcount; i++) {
            String stockId = String.valueOf(returningItemsTable.getValueAt(i, 0));
            returningItemRowNumberMap.put(stockId, i);
        }
    }

    private void clearInputs() {
        itemBarcodeTextField.setText("");
        returningQtyTextField.setText("");
        itemBarcodeTextField.grabFocus();

    }

    private void calculateTotal() {

        try {

            returningTotal = 0.00;

            int numOfRows = returningItemsTable.getRowCount();

            for (int i = 0; i < numOfRows; i++) {
                double soldPrice = Double.parseDouble(String.valueOf(returningItemsTable.getValueAt(i, 2)).replace(",", ""));
                double returningQuantity = Double.parseDouble(String.valueOf(returningItemsTable.getValueAt(i, 3)).replace(",", ""));
                double itemAmount = soldPrice * returningQuantity;
                returningItemsTable.setValueAt(Numbers.formatPrice(itemAmount), i, 5);
                returningTotal += itemAmount;
            }

            returnTotalValueLabel.setText(Numbers.formatPrice(returningTotal));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Something Went Wrong", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void setFullQuantityToQuantityInput(String stockBarcode) {

        if (invoiceItemRowNumberMap.containsKey(stockBarcode)) {
            int rowNumber = invoiceItemRowNumberMap.get(stockBarcode);
            String returningQuantity = String.valueOf(invoiceItemsTable.getValueAt(rowNumber, 5)).replace(",", "");
            returningQtyTextField.setText(returningQuantity);
            returningQtyTextField.grabFocus();
            returningQtyTextField.selectAll();
        }

    }

    private void removeSelectedReturningRow() {

        int selectedRow = returningItemsTable.getSelectedRow();

        if (selectedRow != -1) {
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to remove the selected returning item?",
                    "Clear Items",
                    JOptionPane.YES_NO_OPTION
            );
            if (result == JOptionPane.YES_OPTION) {
                //Remove Row 
                DefaultTableModel model = (DefaultTableModel) returningItemsTable.getModel();
                model.removeRow(selectedRow);

                updateRowNumberMap();
                calculateTotal();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a returning item row to remove.", "Warning", JOptionPane.WARNING_MESSAGE);
        }

    }

    private void saveReturns() {
        int rowCount = returningItemsTable.getRowCount();

        if (rowCount > 0) {

            try {
                String newId = IdGenerater.generateId("return_voucher", "id", "RET");

                //Get Current Date Time
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedCurrentDate = dateFormat.format(date);

                //Save Return Voucher
                MySQL.execute("INSERT INTO return_voucher (id, datetime, reference_invoice_id, returned_amount) "
                        + "VALUES ('" + newId + "','" + formattedCurrentDate + "','" + invoiceId + "','" + returningTotal + "')");

                for (int i = 0; i < rowCount; i++) {
                    String stockBarcode = String.valueOf(returningItemsTable.getValueAt(i, 0));
                    String invoiceItemId = invoiceItemIdMap.get(stockBarcode);
                    String quantity = String.valueOf(returningItemsTable.getValueAt(i, 3));

                    //Save Return Item Details
                    MySQL.execute("INSERT INTO `returned_items` (`return_voucher_id`,`invoice_item_id`,`returned_quantity`) "
                            + "VALUES ('" + newId + "','" + invoiceItemId + "','" + quantity + "')");

                    //Update Invoice Items's Returned Quantity
                    MySQL.execute("UPDATE invoice_item SET returned_quantity = returned_quantity + '" + quantity + "' WHERE id = '" + invoiceItemId + "' ");

                    //Update Stoc's Current Quantity
                    MySQL.execute("UPDATE stock SET current_quantity = current_quantity + '" + quantity + "' WHERE barcode = '" + stockBarcode + "' ");

                    JOptionPane.showMessageDialog(this, "Items Returned Successfully", "Success", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/resource/success.png")));

                    if (returnManagement != null) {
                        returnManagement.loadReturnTable();
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, e.getMessage(), "Something Went Wrong", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(this, "There are no items to return.", "Warning", JOptionPane.WARNING_MESSAGE);

        }

    }

    private void printReturnVoucher() {
        System.out.println("Print Invoice");
    }

    private void removeAllReturnings() {

        int result = JOptionPane.showConfirmDialog(
                this,
                "<html>Are you sure you want to remove all returns?<html>",
                "Clear Items",
                JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {
            DefaultTableModel model = (DefaultTableModel) returningItemsTable.getModel();
            model.setRowCount(0);
            updateRowNumberMap();
            calculateTotal();
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

        dateChooser = new com.raven.datechooser.DateChooser();
        buttonGroup1 = new javax.swing.ButtonGroup();
        timePicker = new com.raven.swing.TimePicker();
        jMenu1 = new javax.swing.JMenu();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        invoiceDataTable = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jScrollPane2 = new javax.swing.JScrollPane();
        invoiceItemsTable = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jScrollPane4 = new javax.swing.JScrollPane();
        returningItemsTable = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        saveButton = new javax.swing.JButton();
        removeAllButton = new javax.swing.JButton();
        removeSelectedButton = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        totalTextLabel = new javax.swing.JLabel();
        returnTotalValueLabel = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        itemBarcodeTextField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        returningQtyTextField = new javax.swing.JTextField();
        addToReturnButton = new javax.swing.JButton();
        clearInputsButton = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        supplierNameShowLabel = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        dateChooser.setForeground(new java.awt.Color(0, 105, 75));
        dateChooser.setDateFormat("yyyy-MM-dd");

        timePicker.setForeground(new java.awt.Color(0, 105, 75));
        timePicker.set24hourMode(true);

        jMenu1.setText("jMenu1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Return Sold Items");
        setMinimumSize(new java.awt.Dimension(1090, 696));

        jSeparator1.setForeground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 105, 75));
        jLabel1.setText("Return Items");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        invoiceDataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Invoice Id", "Issued Date Time", "Number of items", "Total", "Discount", "Net Total", "Return Payments", "Payable Amount", "Paid Amount", "Balance", "Payment Method"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        invoiceDataTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(invoiceDataTable);
        if (invoiceDataTable.getColumnModel().getColumnCount() > 0) {
            invoiceDataTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        }

        jPanel4.setLayout(new java.awt.GridLayout(1, 0));

        jLabel3.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel3.setText("Invoice Items");
        jLabel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

        jSeparator3.setForeground(new java.awt.Color(204, 204, 204));

        jScrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        invoiceItemsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Stock Barcode", "Product Name", "Sold Price", "Purchased Quantity", "Returned Quantity", "Returnable Quantity", "Unit", "Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        invoiceItemsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        invoiceItemsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                invoiceItemsTableMouseClicked(evt);
            }
        });
        invoiceItemsTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                invoiceItemsTableKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(invoiceItemsTable);
        if (invoiceItemsTable.getColumnModel().getColumnCount() > 0) {
            invoiceItemsTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        }

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator3)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(9, 9, 9))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.add(jPanel6);

        jLabel5.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel5.setText("Returning Items");
        jLabel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

        jSeparator5.setForeground(new java.awt.Color(204, 204, 204));

        jScrollPane4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        returningItemsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Stock Barcode", "Product Name", "Sold Price", "Returning Quantity", "Measurement Unit", "Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        returningItemsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        returningItemsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                returningItemsTableMouseClicked(evt);
            }
        });
        returningItemsTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                returningItemsTableKeyReleased(evt);
            }
        });
        jScrollPane4.setViewportView(returningItemsTable);
        if (returningItemsTable.getColumnModel().getColumnCount() > 0) {
            returningItemsTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        }

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator5)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.add(jPanel5);

        saveButton.setText("Return & Print Return Voucher");
        saveButton.setToolTipText("Add New Product");
        saveButton.setBorder(null);
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        removeAllButton.setBackground(new java.awt.Color(102, 102, 102));
        removeAllButton.setForeground(new java.awt.Color(255, 255, 255));
        removeAllButton.setText("Remove All");
        removeAllButton.setBorder(null);
        removeAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeAllButtonActionPerformed(evt);
            }
        });

        removeSelectedButton.setBackground(new java.awt.Color(102, 102, 102));
        removeSelectedButton.setForeground(new java.awt.Color(255, 255, 255));
        removeSelectedButton.setText("Remove Selected");
        removeSelectedButton.setBorder(null);
        removeSelectedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeSelectedButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(removeAllButton, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removeSelectedButton, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeSelectedButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(removeAllButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        totalTextLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        totalTextLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalTextLabel.setText("Returning Amount (Rs.) :");

        returnTotalValueLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        returnTotalValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        returnTotalValueLabel.setText("0.00");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(totalTextLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(returnTotalValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalTextLabel)
                    .addComponent(returnTotalValueLabel)))
        );

        jLabel8.setText("Item Barcode");

        itemBarcodeTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemBarcodeTextFieldActionPerformed(evt);
            }
        });

        jLabel9.setText("Returning Quantity");

        returningQtyTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returningQtyTextFieldActionPerformed(evt);
            }
        });

        addToReturnButton.setText("Add To Return");
        addToReturnButton.setToolTipText("Add New Product");
        addToReturnButton.setBorder(null);
        addToReturnButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addToReturnButtonActionPerformed(evt);
            }
        });

        clearInputsButton.setBackground(new java.awt.Color(102, 102, 102));
        clearInputsButton.setForeground(new java.awt.Color(255, 255, 255));
        clearInputsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/clean.png"))); // NOI18N
        clearInputsButton.setBorder(null);
        clearInputsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearInputsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(itemBarcodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9)
                    .addComponent(returningQtyTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addToReturnButton, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clearInputsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(itemBarcodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(returningQtyTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addToReturnButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(clearInputsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 0, 0, 0));
        jPanel9.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 343, Short.MAX_VALUE)
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        jPanel9.add(jPanel20);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 105, 75));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/logo-extra-sm.png"))); // NOI18N
        jLabel2.setIconTextGap(20);
        jPanel9.add(jLabel2);

        supplierNameShowLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(supplierNameShowLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(supplierNameShowLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel9.add(jPanel18);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jMenu2.setText("File");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        jMenuItem1.setText("Select Stock");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void invoiceItemsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_invoiceItemsTableMouseClicked

        if (evt.getClickCount() == 2) {
            int selectedRow = invoiceItemsTable.getSelectedRow();

            if (selectedRow != -1) {
                String selectedStockBarcode = String.valueOf(invoiceItemsTable.getValueAt(selectedRow, 0));
                String selectedReturnableQty = String.valueOf(invoiceItemsTable.getValueAt(selectedRow, 5));

                itemBarcodeTextField.setText(selectedStockBarcode);
                returningQtyTextField.setText(selectedReturnableQty);
                returningQtyTextField.grabFocus();
                returningQtyTextField.selectAll();
            }
        }
    }//GEN-LAST:event_invoiceItemsTableMouseClicked

    private void invoiceItemsTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_invoiceItemsTableKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_invoiceItemsTableKeyReleased

    private void returningItemsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_returningItemsTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_returningItemsTableMouseClicked

    private void returningItemsTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_returningItemsTableKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_returningItemsTableKeyReleased

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        saveReturns();

    }//GEN-LAST:event_saveButtonActionPerformed

    private void addToReturnButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addToReturnButtonActionPerformed
        addToRetrurn();
    }//GEN-LAST:event_addToReturnButtonActionPerformed

    private void itemBarcodeTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemBarcodeTextFieldActionPerformed
        setFullQuantityToQuantityInput(itemBarcodeTextField.getText());
    }//GEN-LAST:event_itemBarcodeTextFieldActionPerformed

    private void clearInputsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearInputsButtonActionPerformed
        clearInputs();
    }//GEN-LAST:event_clearInputsButtonActionPerformed

    private void returningQtyTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returningQtyTextFieldActionPerformed
        addToRetrurn();
    }//GEN-LAST:event_returningQtyTextFieldActionPerformed

    private void removeAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeAllButtonActionPerformed
        removeAllReturnings();
    }//GEN-LAST:event_removeAllButtonActionPerformed

    private void removeSelectedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeSelectedButtonActionPerformed
        removeSelectedReturningRow();
    }//GEN-LAST:event_removeSelectedButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addToReturnButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton clearInputsButton;
    private com.raven.datechooser.DateChooser dateChooser;
    private javax.swing.JTable invoiceDataTable;
    private javax.swing.JTable invoiceItemsTable;
    private javax.swing.JTextField itemBarcodeTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JButton removeAllButton;
    private javax.swing.JButton removeSelectedButton;
    private javax.swing.JLabel returnTotalValueLabel;
    private javax.swing.JTable returningItemsTable;
    private javax.swing.JTextField returningQtyTextField;
    private javax.swing.JButton saveButton;
    private javax.swing.JLabel supplierNameShowLabel;
    private com.raven.swing.TimePicker timePicker;
    private javax.swing.JLabel totalTextLabel;
    // End of variables declaration//GEN-END:variables
}
