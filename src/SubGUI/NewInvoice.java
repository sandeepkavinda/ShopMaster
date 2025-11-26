package SubGUI;

import DTO.InvoiceItem;
import DTO.InvoicePaymentData;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import model.MySQL;
import model.Numbers;
import model.Validation;
import javax.swing.table.DefaultTableCellRenderer;
import model.Generate;
import model.IdGenerater;
import panels.InvoiceManagement;
import utils.ToastUtils;

/**
 *
 * @author Sandeep
 */
public class NewInvoice extends javax.swing.JFrame {

    //Inisilize Varables
    private HashMap<String, Integer> rowNumberMap = new HashMap<String, Integer>();

    private InvoiceItem selectedInoviceItem;

    private double invoiceTotal;
    private double invoiceDiscount;
    private double invoiceNetTotal;
    private InvoiceManagement invoiceManagement;

    public NewInvoice(InvoiceManagement invoiceManagement) {
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resource/icon.png")));
        initComponents();
        this.invoiceManagement = invoiceManagement;
        stockBarcodeTextField.grabFocus();
    }

    public void setInvoiceItem(String stockBarcode) {
        //Set Stock Datails For Row inputs
        try {
            ResultSet results = MySQL.execute("SELECT * FROM stock s "
                    + "INNER JOIN product p ON s.product_id=p.id "
                    + "INNER JOIN measurement_unit mu ON p.measurement_unit_id=mu.id "
                    + "WHERE `barcode`='" + stockBarcode + "'");

            if (results.next()) {

                double markedPrice = Double.parseDouble(results.getString("marked_price"));
                double sellingDiscount = Double.parseDouble(results.getString("selling_discount"));
                double sellingPrice = Double.parseDouble(results.getString("selling_price"));

                selectedInoviceItem = new InvoiceItem();

                selectedInoviceItem.setStockBarcode(results.getString("s.barcode"));
                selectedInoviceItem.setProductName(results.getString("p.name"));
                selectedInoviceItem.setMarkedPrice(Numbers.formatPrice(markedPrice));
                selectedInoviceItem.setSellingDiscount(Numbers.formatPrice(sellingDiscount));
                selectedInoviceItem.setSellingPrice(Numbers.formatPrice(sellingPrice));
                selectedInoviceItem.setMeasurementUnit(results.getString("mu.short_form"));
                selectedInoviceItem.setAvalibleQuantity(Double.parseDouble(results.getString("current_quantity")));

                stockBarcodeTextField.setText(selectedInoviceItem.getStockBarcode());
                productNameTextField.setText(selectedInoviceItem.getProductName());
                markedPriceTextField.setText(selectedInoviceItem.getMarkedPrice());
                sellingDiscountTextField.setText(selectedInoviceItem.getSellingDiscount());
                sellingPriceTextField.setText(selectedInoviceItem.getSellingPrice());
                quantityTextField.setText("1");

                quantityTextField.grabFocus();
                quantityTextField.selectAll();
                stockBarcodeTextField.setEditable(false);
                quantityTextField.setEditable(true);

            } else {
                JOptionPane.showMessageDialog(this, "Barcode Not Found", "Warning", JOptionPane.WARNING_MESSAGE);

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void addInvoiceItem() {

        try {
            String quantity = quantityTextField.getText();

            if (selectedInoviceItem == null) {
                JOptionPane.showMessageDialog(this, "Please Select the Stock", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (quantity.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please Enter Quantity", "Warning", JOptionPane.WARNING_MESSAGE);
                quantityTextField.grabFocus();
            } else if (!Validation.isValidQuantity(quantity)) {
                JOptionPane.showMessageDialog(this, "<html>Quantity must be a whole number or decimal<br>(e.g., 10 or 10.5).</html>", "Warning", JOptionPane.WARNING_MESSAGE);
                quantityTextField.grabFocus();
                quantityTextField.selectAll();
            } else if (Double.parseDouble(quantity) == 0) {
                JOptionPane.showMessageDialog(this, "Quantity Can't be \"0\"", "Warning", JOptionPane.WARNING_MESSAGE);
                quantityTextField.grabFocus();
                quantityTextField.selectAll();
            } else if (Double.parseDouble(quantity) < 0) {
                JOptionPane.showMessageDialog(this, "Quantity Can't be Negetive", "Warning", JOptionPane.WARNING_MESSAGE);
                quantityTextField.grabFocus();
                quantityTextField.selectAll();
            } else {

                if (rowNumberMap.containsKey(selectedInoviceItem.getStockBarcode())) {

                    //Already Have a row from this stock
                    int rowNumber = rowNumberMap.get(selectedInoviceItem.getStockBarcode());
                    double currentQty = Double.parseDouble(String.valueOf(invoiceItemsTable.getValueAt(rowNumber, 6)));
                    double newQuantity = Double.parseDouble(quantity);

                    if (selectedInoviceItem.getAvalibleQuantity() < (currentQty + newQuantity)) {
                        JOptionPane.showMessageDialog(this, "<html>Only <b>" + Numbers.formatQuantity(selectedInoviceItem.getAvalibleQuantity()) + selectedInoviceItem.getMeasurementUnit() + "</b> are available in stock.<html>", "Available Quantity exceed", JOptionPane.WARNING_MESSAGE);
                        quantityTextField.grabFocus();
                        quantityTextField.selectAll();
                    } else {
                        invoiceItemsTable.setValueAt(Numbers.formatQuantity(currentQty + newQuantity), rowNumber, 6);
                        invoiceItemsTable.setRowSelectionInterval(rowNumber, rowNumber);
                        stockBarcodeTextField.grabFocus();
                        calculateTotal();
                        cleanInvoiceItemInputs();
                    }

                } else {

                    if (selectedInoviceItem.getAvalibleQuantity() < Double.parseDouble(quantity)) {
                        JOptionPane.showMessageDialog(this, "<html>Only <b>" + Numbers.formatQuantity(selectedInoviceItem.getAvalibleQuantity()) + selectedInoviceItem.getMeasurementUnit() + "</b> are available in stock.<html>", "Available Quantity exceed", JOptionPane.WARNING_MESSAGE);
                        quantityTextField.grabFocus();
                        quantityTextField.selectAll();
                    } else {

                        //Add New Row
                        DefaultTableModel model = (DefaultTableModel) invoiceItemsTable.getModel();
                        int rowCount = invoiceItemsTable.getRowCount();

                        //Input To Invoice items Table
                        Vector v = new Vector();
                        v.add(rowCount + 1);
                        v.add(selectedInoviceItem.getStockBarcode());
                        v.add(selectedInoviceItem.getProductName());
                        v.add(selectedInoviceItem.getMarkedPrice());
                        v.add(selectedInoviceItem.getSellingDiscount());
                        v.add(selectedInoviceItem.getSellingPrice());
                        v.add(Numbers.formatQuantity(Double.parseDouble(quantity)));
                        v.add(selectedInoviceItem.getMeasurementUnit());
                        model.addRow(v);

                        //Add To Row Numbers Hashmap
                        rowNumberMap.put(selectedInoviceItem.getStockBarcode(), rowCount);
                        invoiceItemsTable.setRowSelectionInterval(rowCount, rowCount);
                        stockBarcodeTextField.grabFocus();
                        calculateTotal();
                        cleanInvoiceItemInputs();

                    }

                }

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Something Went Wrong Try Again", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void cleanInvoiceItemInputs() {
        stockBarcodeTextField.setText("");
        stockBarcodeTextField.setEditable(true);
        productNameTextField.setText("");
        markedPriceTextField.setText("");
        sellingDiscountTextField.setText("");
        sellingPriceTextField.setText("");
        quantityTextField.setText("");
        quantityTextField.setEditable(false);
        sellingDiscountTextField.setText("");
        quantityLabel.setText("Quantity");

        selectedInoviceItem = null;
    }

    private void calculateTotal() {

        try {

            invoiceTotal = 0.00;

            int numOfRows = invoiceItemsTable.getRowCount();

            for (int i = 0; i < numOfRows; i++) {
                double markedPrice = Double.parseDouble(String.valueOf(invoiceItemsTable.getValueAt(i, 3)).replace(",", ""));
                double discount = Double.parseDouble(String.valueOf(invoiceItemsTable.getValueAt(i, 4)).replace(",", ""));
                double quantity = Double.parseDouble(String.valueOf(invoiceItemsTable.getValueAt(i, 6)).replace(",", ""));
                double itemTotal = (markedPrice - discount) * quantity;
                invoiceItemsTable.setValueAt(Numbers.formatPrice(itemTotal), i, 8);
                invoiceTotal += itemTotal;
            }

            totalValueLabel.setText(Numbers.formatPrice(invoiceTotal));
            discountValueLabel.setText(Numbers.formatPrice(invoiceDiscount));
            invoiceNetTotal = invoiceTotal - invoiceDiscount;
            netTotalValueLabel.setText(Numbers.formatPrice(invoiceNetTotal));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Something Went Wrong", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void setInvoiceDiscount() {

        if (!(presentageDiscountRadioButton.isSelected() || amountDiscountRadioButton.isSelected())) {
            JOptionPane.showMessageDialog(this, "Please Select Discount Type", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (invoiceDiscountFromattedTextField.getText().equals("0.00")) {
            JOptionPane.showMessageDialog(this, "Please enter a discount to set.", "Warning", JOptionPane.WARNING_MESSAGE);
            invoiceDiscountFromattedTextField.grabFocus();
            invoiceDiscountFromattedTextField.selectAll();
        } else {

            if (presentageDiscountRadioButton.isSelected()) {
                double discountPresentage = Double.parseDouble(invoiceDiscountFromattedTextField.getText());
                if (discountPresentage > 100) {
                    JOptionPane.showMessageDialog(this, "Invoice discount must be less than 100%", "Warning", JOptionPane.WARNING_MESSAGE);
                    invoiceDiscountFromattedTextField.setText("0.00");
                } else if (discountPresentage < 0) {
                    JOptionPane.showMessageDialog(this, "Invoice discount should not be negative", "Warning", JOptionPane.WARNING_MESSAGE);
                    invoiceDiscountFromattedTextField.setText("0.00");
                } else {
                    discountTextLabel.setText(Numbers.formatPresentage(discountPresentage) + "% Disount (Rs.) :");
                    invoiceDiscount = invoiceTotal * (discountPresentage / 100);
                    ToastUtils.showBottomToast(this, "Discount Added", 2000);

                }
            }

            if (amountDiscountRadioButton.isSelected()) {
                double discountAmount = Double.parseDouble(invoiceDiscountFromattedTextField.getText());
                if (discountAmount > invoiceTotal) {
                    JOptionPane.showMessageDialog(this, "Invoice discount must be less than Total Amount", "Warning", JOptionPane.WARNING_MESSAGE);
                    invoiceDiscountFromattedTextField.setText("0.00");
                } else if (discountAmount < 0) {
                    JOptionPane.showMessageDialog(this, "Invoice discount should not be negative", "Warning", JOptionPane.WARNING_MESSAGE);
                    invoiceDiscountFromattedTextField.setText("0.00");
                } else {
                    invoiceDiscount = discountAmount;
                    discountTextLabel.setText("Disount (Rs.) :");
                    ToastUtils.showBottomToast(this, "Discount Added", 2000);

                }
            }
        }
        calculateTotal();
    }

    private void removeDiscount() {
        invoiceDiscount = 0.00;
        invoiceDiscountFromattedTextField.setText("0.00");
        discountTextLabel.setText("Disount (Rs.) :");
        calculateTotal();
    }

    private void deleteSelectedRow() {
        int selectedRow = invoiceItemsTable.getSelectedRow();

        if (selectedRow != -1) {
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to remove row number " + invoiceItemsTable.getValueAt(selectedRow, 0),
                    "Clear Items",
                    JOptionPane.YES_NO_OPTION
            );
            if (result == JOptionPane.YES_OPTION) {
                //Remove Row 
                DefaultTableModel model = (DefaultTableModel) invoiceItemsTable.getModel();
                model.removeRow(selectedRow);

                updateRowNumberMap();
                calculateTotal();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please Select a Row Remove", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void openStockSelector() {
        invoiceItemsTable.clearSelection();
        cleanInvoiceItemInputs();
        SelectStock selectStock = new SelectStock(this, true, null, this);
        selectStock.setVisible(true);
    }

    private void updateRowNumberMap() {
        int newRowcount = invoiceItemsTable.getRowCount();
        rowNumberMap.clear();
        for (int i = 0; i < newRowcount; i++) {
            String stockId = String.valueOf(invoiceItemsTable.getValueAt(i, 1));
            rowNumberMap.put(stockId, i);
        }
    }

    private void removeAllInvoiceItems() {
        int rowCount = invoiceItemsTable.getRowCount();
        if (rowCount < 1) {
            JOptionPane.showMessageDialog(this, "No Items To Remove", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "<html>Are you sure you want to remove all items<html>",
                    "Clear Items",
                    JOptionPane.YES_NO_OPTION
            );

            if (result == JOptionPane.YES_OPTION) {
                DefaultTableModel model = (DefaultTableModel) invoiceItemsTable.getModel();
                model.setRowCount(0);
                rowNumberMap.clear();
                JOptionPane.showMessageDialog(this, "Removed All Items Successfully", "Success", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/resource/success.png")));
            }
        }
        removeDiscount();
        calculateTotal();
    }

    public String saveInvoice(String returnVoucherId, double returnPaymentAmount, double paidAmount, String paymentMethodId, double usedReturnedVoucherAmount) {
        int rowCount = invoiceItemsTable.getRowCount();

        if (rowCount < 1) {
            JOptionPane.showMessageDialog(this, "No Items in the Invoice", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                //Generate Invoice Barcode
                String newBarcode = IdGenerater.generateId("invoice", "invoice_id", "INV");

                //Get Current Date Time
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedCurrentDate = dateFormat.format(date);

                //Add Invoice 
                if (returnVoucherId != null) {
                    MySQL.execute("INSERT INTO invoice (invoice_id, datetime, total_amount, discount, return_voucher_id, return_payment_amount, paid_amount, item_count, payment_method_id) "
                            + "VALUES ('" + newBarcode + "','" + formattedCurrentDate + "','" + invoiceTotal + "','" + invoiceDiscount + "','" + returnVoucherId + "','" + returnPaymentAmount + "','" + paidAmount + "','" + rowCount + "','" + paymentMethodId + "')");

                } else {
                    MySQL.execute("INSERT INTO invoice (invoice_id, datetime, total_amount, discount, return_payment_amount, paid_amount, item_count, payment_method_id) "
                            + "VALUES ('" + newBarcode + "','" + formattedCurrentDate + "','" + invoiceTotal + "','" + invoiceDiscount + "','" + returnPaymentAmount + "','" + paidAmount + "','" + rowCount + "','" + paymentMethodId + "')");
                }

                //Add Invoice Items
                for (int i = 0; i < rowCount; i++) {
                    String stockBarcode = String.valueOf(invoiceItemsTable.getValueAt(i, 1));
                    String markedPrice = String.valueOf(invoiceItemsTable.getValueAt(i, 3)).replace(",", "");
                    String sellingDiscount = String.valueOf(invoiceItemsTable.getValueAt(i, 4)).replace(",", "");
                    String quantity = String.valueOf(invoiceItemsTable.getValueAt(i, 6));

                    MySQL.execute("INSERT INTO `invoice_item` (`invoice_id`,`stock_barcode`,`marked_price`,`selling_discount`,`quantity`) "
                            + "VALUES ('" + newBarcode + "','" + stockBarcode + "','" + markedPrice + "','" + sellingDiscount + "','" + quantity + "')");

                    MySQL.execute("UPDATE stock SET current_quantity = current_quantity - '" + quantity + "' WHERE barcode = '" + stockBarcode + "' ");

                }

                //Update Return Voucher
                if (returnVoucherId != null) {
                    MySQL.execute("UPDATE return_voucher SET used_amount = used_amount +'" + usedReturnedVoucherAmount + "' WHERE id = '" + returnVoucherId + "' ");
                }

                invoiceManagement.loadInvoiceTable();
                return newBarcode;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                e.printStackTrace();
            }
        }
        return null;
    }

    private void processToNormalPayment() {

        int rowCount = invoiceItemsTable.getRowCount();

        if (rowCount < 1) {
            JOptionPane.showMessageDialog(this, "No Items in the Invoice", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            InvoicePaymentData invoicePaymentData = new InvoicePaymentData(invoiceTotal, invoiceDiscount, invoiceNetTotal, null);
            InvoicePayment invoicePayment = new InvoicePayment(this, true, invoicePaymentData, this);
            invoicePayment.setVisible(true);
        }

    }

    private void processToPayWithReturns() {
        int rowCount = invoiceItemsTable.getRowCount();

        if (rowCount < 1) {
            JOptionPane.showMessageDialog(this, "No Items in the Invoice", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            InvoicePaymentData invoicePaymentData = new InvoicePaymentData(invoiceTotal, invoiceDiscount, invoiceNetTotal, null);
            ReturnIdInputToInvoicePayment returnIdInputToInvoicePayment = new ReturnIdInputToInvoicePayment(this, true, invoicePaymentData, this);
            returnIdInputToInvoicePayment.setVisible(true);
        }
    }

    public void resetInvoice() {

        DefaultTableModel model = (DefaultTableModel) invoiceItemsTable.getModel();
        model.setRowCount(0);

        rowNumberMap = new HashMap<String, Integer>();

        InvoiceItem selectedInoviceItem = null;

        invoiceTotal = 0.00;
        invoiceDiscount = 0.00;
        invoiceNetTotal = 0.00;

        removeDiscount();
        calculateTotal();
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
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        stockBarcodeLabel = new javax.swing.JLabel();
        addStockButton = new javax.swing.JButton();
        stockBarcodeTextField = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        productLabel = new javax.swing.JLabel();
        productNameTextField = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        buyingPriceLabel = new javax.swing.JLabel();
        markedPriceTextField = new javax.swing.JTextField();
        jPanel34 = new javax.swing.JPanel();
        itemDiscountLabel = new javax.swing.JLabel();
        sellingDiscountTextField = new javax.swing.JTextField();
        jPanel19 = new javax.swing.JPanel();
        sellingPriceLabel = new javax.swing.JLabel();
        sellingPriceTextField = new javax.swing.JTextField();
        jPanel33 = new javax.swing.JPanel();
        quantityLabel = new javax.swing.JLabel();
        quantityTextField = new javax.swing.JTextField();
        jPanel27 = new javax.swing.JPanel();
        addItemButton = new javax.swing.JButton();
        clearRowButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        discountLabel = new javax.swing.JLabel();
        setDiscountButton = new javax.swing.JButton();
        amountDiscountRadioButton = new javax.swing.JRadioButton();
        presentageDiscountRadioButton = new javax.swing.JRadioButton();
        invoiceDiscountFromattedTextField = new javax.swing.JFormattedTextField();
        clearDiscountButton = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        totalValueLabel = new javax.swing.JLabel();
        discountValueLabel = new javax.swing.JLabel();
        netTotalValueLabel = new javax.swing.JLabel();
        netTotalTextLabel = new javax.swing.JLabel();
        discountTextLabel = new javax.swing.JLabel();
        totalTextLabel = new javax.swing.JLabel();
        payButton = new javax.swing.JButton();
        payWithReturnsButton = new javax.swing.JButton();
        removeAllButton = new javax.swing.JButton();
        removeSelectedButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        invoiceItemsTable = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
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
        setTitle("New Customer Invoice");
        setMinimumSize(new java.awt.Dimension(1090, 696));

        jPanel11.setLayout(new java.awt.GridLayout(1, 0));

        jPanel12.setForeground(new java.awt.Color(255, 51, 51));

        stockBarcodeLabel.setText("Stock Barcode");

        addStockButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/add.png"))); // NOI18N
        addStockButton.setToolTipText("Select Product (F1)");
        addStockButton.setBorder(null);
        addStockButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStockButtonActionPerformed(evt);
            }
        });

        stockBarcodeTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stockBarcodeTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(stockBarcodeTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addStockButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(stockBarcodeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(stockBarcodeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addStockButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(stockBarcodeTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel11.add(jPanel12);

        jPanel13.setForeground(new java.awt.Color(255, 51, 51));

        productLabel.setText("Product");

        productNameTextField.setEditable(false);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(productNameTextField)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(productLabel)
                        .addGap(0, 91, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(productLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(productNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel11.add(jPanel13);

        jPanel14.setForeground(new java.awt.Color(255, 51, 51));

        buyingPriceLabel.setText("Marked Price");

        markedPriceTextField.setEditable(false);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(buyingPriceLabel)
                        .addGap(0, 64, Short.MAX_VALUE))
                    .addComponent(markedPriceTextField))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buyingPriceLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(markedPriceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel11.add(jPanel14);

        jPanel34.setForeground(new java.awt.Color(255, 51, 51));

        itemDiscountLabel.setText("Discount");

        sellingDiscountTextField.setEditable(false);

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addComponent(itemDiscountLabel)
                        .addGap(0, 86, Short.MAX_VALUE))
                    .addComponent(sellingDiscountTextField))
                .addContainerGap())
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(itemDiscountLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sellingDiscountTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel11.add(jPanel34);

        jPanel19.setForeground(new java.awt.Color(255, 51, 51));

        sellingPriceLabel.setText("Selling Price");

        sellingPriceTextField.setEditable(false);

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(sellingPriceLabel)
                        .addGap(0, 69, Short.MAX_VALUE))
                    .addComponent(sellingPriceTextField))
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sellingPriceLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sellingPriceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel11.add(jPanel19);

        jPanel33.setForeground(new java.awt.Color(255, 51, 51));

        quantityLabel.setText("Quantity");

        quantityTextField.setEditable(false);
        quantityTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quantityTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel33Layout.createSequentialGroup()
                        .addComponent(quantityLabel)
                        .addGap(0, 87, Short.MAX_VALUE))
                    .addComponent(quantityTextField))
                .addContainerGap())
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(quantityLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(quantityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel11.add(jPanel33);

        jPanel27.setForeground(new java.awt.Color(255, 51, 51));

        addItemButton.setText("Add Item");
        addItemButton.setToolTipText("Add New Product");
        addItemButton.setBorder(null);
        addItemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addItemButtonActionPerformed(evt);
            }
        });

        clearRowButton.setBackground(new java.awt.Color(102, 102, 102));
        clearRowButton.setForeground(new java.awt.Color(255, 255, 255));
        clearRowButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/clean.png"))); // NOI18N
        clearRowButton.setBorder(null);
        clearRowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearRowButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addItemButton, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clearRowButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addItemButton, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(clearRowButton, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel11.add(jPanel27);

        discountLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        discountLabel.setText("Invoice Discount");

        setDiscountButton.setText("Set");
        setDiscountButton.setToolTipText("Add New Product");
        setDiscountButton.setBorder(null);
        setDiscountButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setDiscountButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(amountDiscountRadioButton);
        amountDiscountRadioButton.setText("Amount (Rs.)");

        buttonGroup1.add(presentageDiscountRadioButton);
        presentageDiscountRadioButton.setText("Presentage (%)");

        invoiceDiscountFromattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("0.00"))));
        invoiceDiscountFromattedTextField.setText("0.00");
        invoiceDiscountFromattedTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invoiceDiscountFromattedTextFieldActionPerformed(evt);
            }
        });

        clearDiscountButton.setBackground(new java.awt.Color(102, 102, 102));
        clearDiscountButton.setForeground(new java.awt.Color(255, 255, 255));
        clearDiscountButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/clean.png"))); // NOI18N
        clearDiscountButton.setBorder(null);
        clearDiscountButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearDiscountButtonActionPerformed(evt);
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
                        .addComponent(invoiceDiscountFromattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(setDiscountButton, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(discountLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(presentageDiscountRadioButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(amountDiscountRadioButton)
                        .addGap(12, 12, 12)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clearDiscountButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(discountLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(setDiscountButton, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(invoiceDiscountFromattedTextField)
                    .addComponent(clearDiscountButton, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(presentageDiscountRadioButton)
                    .addComponent(amountDiscountRadioButton))
                .addContainerGap())
        );

        totalValueLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        totalValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalValueLabel.setText("0.00");

        discountValueLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        discountValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        discountValueLabel.setText("0.00");

        netTotalValueLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        netTotalValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        netTotalValueLabel.setText("0.00");

        netTotalTextLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        netTotalTextLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        netTotalTextLabel.setText("Net Total (Rs.) :");

        discountTextLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        discountTextLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        discountTextLabel.setText("Disount (Rs.) :");

        totalTextLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        totalTextLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalTextLabel.setText("Invoice Amount (Rs.) :");

        payButton.setText("Pay");
        payButton.setToolTipText("Add New Product");
        payButton.setBorder(null);
        payButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payButtonActionPerformed(evt);
            }
        });

        payWithReturnsButton.setText("Pay With Returns");
        payWithReturnsButton.setToolTipText("Add New Product");
        payWithReturnsButton.setBorder(null);
        payWithReturnsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payWithReturnsButtonActionPerformed(evt);
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

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(removeSelectedButton, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeAllButton, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(payWithReturnsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(payButton, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(discountTextLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(netTotalTextLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(discountValueLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(netTotalValueLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(totalTextLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalTextLabel)
                    .addComponent(totalValueLabel))
                .addGap(0, 0, 0)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                        .addComponent(discountTextLabel)
                        .addGap(0, 0, 0)
                        .addComponent(netTotalTextLabel))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(discountValueLabel)
                        .addGap(0, 0, 0)
                        .addComponent(netTotalValueLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(payButton, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(payWithReturnsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(removeAllButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(removeSelectedButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        invoiceItemsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Stock Barcode", "Product Name", "Marked Price", "Discount", "Selling Price", "Quantity", "Measurement Unit", "Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        invoiceItemsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        invoiceItemsTable.getTableHeader().setReorderingAllowed(false);

        // Center-align the first column (index 0)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        invoiceItemsTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        invoiceItemsTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        invoiceItemsTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        invoiceItemsTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        invoiceItemsTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        invoiceItemsTable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        invoiceItemsTable.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
        invoiceItemsTable.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);

        // Right-align the second column (index 1)
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        invoiceItemsTable.getColumnModel().getColumn(8).setCellRenderer(rightRenderer);
        jScrollPane1.setViewportView(invoiceItemsTable);
        if (invoiceItemsTable.getColumnModel().getColumnCount() > 0) {
            invoiceItemsTable.getColumnModel().getColumn(0).setMinWidth(40);
            invoiceItemsTable.getColumnModel().getColumn(0).setPreferredWidth(40);
            invoiceItemsTable.getColumnModel().getColumn(0).setMaxWidth(40);
        }

        jSeparator1.setForeground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 105, 75));
        jLabel1.setText("New Invoice");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1018, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addComponent(jSeparator1)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 1030, Short.MAX_VALUE))
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

    private void addStockButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStockButtonActionPerformed
        openStockSelector();
    }//GEN-LAST:event_addStockButtonActionPerformed

    private void stockBarcodeTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stockBarcodeTextFieldActionPerformed
        String barcode = stockBarcodeTextField.getText().replaceAll("\\s+", "");
        setInvoiceItem(barcode);
    }//GEN-LAST:event_stockBarcodeTextFieldActionPerformed

    private void quantityTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quantityTextFieldActionPerformed
        addInvoiceItem();
    }//GEN-LAST:event_quantityTextFieldActionPerformed

    private void removeAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeAllButtonActionPerformed
        removeAllInvoiceItems();
    }//GEN-LAST:event_removeAllButtonActionPerformed

    private void invoiceDiscountFromattedTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invoiceDiscountFromattedTextFieldActionPerformed
        setInvoiceDiscount();
    }//GEN-LAST:event_invoiceDiscountFromattedTextFieldActionPerformed

    private void setDiscountButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setDiscountButtonActionPerformed
        setInvoiceDiscount();
    }//GEN-LAST:event_setDiscountButtonActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        openStockSelector();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void payWithReturnsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payWithReturnsButtonActionPerformed
        processToPayWithReturns();
    }//GEN-LAST:event_payWithReturnsButtonActionPerformed

    private void removeSelectedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeSelectedButtonActionPerformed
        deleteSelectedRow();
    }//GEN-LAST:event_removeSelectedButtonActionPerformed

    private void clearRowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearRowButtonActionPerformed
        cleanInvoiceItemInputs();
    }//GEN-LAST:event_clearRowButtonActionPerformed

    private void addItemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addItemButtonActionPerformed
        addInvoiceItem();
    }//GEN-LAST:event_addItemButtonActionPerformed

    private void clearDiscountButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearDiscountButtonActionPerformed
        removeDiscount();
    }//GEN-LAST:event_clearDiscountButtonActionPerformed

    private void payButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payButtonActionPerformed
        processToNormalPayment();
    }//GEN-LAST:event_payButtonActionPerformed

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
    private javax.swing.JButton addItemButton;
    private javax.swing.JButton addStockButton;
    private javax.swing.JRadioButton amountDiscountRadioButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel buyingPriceLabel;
    private javax.swing.JButton clearDiscountButton;
    private javax.swing.JButton clearRowButton;
    private com.raven.datechooser.DateChooser dateChooser;
    private javax.swing.JLabel discountLabel;
    private javax.swing.JLabel discountTextLabel;
    private javax.swing.JLabel discountValueLabel;
    private javax.swing.JFormattedTextField invoiceDiscountFromattedTextField;
    private javax.swing.JTable invoiceItemsTable;
    private javax.swing.JLabel itemDiscountLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField markedPriceTextField;
    private javax.swing.JLabel netTotalTextLabel;
    private javax.swing.JLabel netTotalValueLabel;
    private javax.swing.JButton payButton;
    private javax.swing.JButton payWithReturnsButton;
    private javax.swing.JRadioButton presentageDiscountRadioButton;
    private javax.swing.JLabel productLabel;
    private javax.swing.JTextField productNameTextField;
    private javax.swing.JLabel quantityLabel;
    private javax.swing.JTextField quantityTextField;
    private javax.swing.JButton removeAllButton;
    private javax.swing.JButton removeSelectedButton;
    private javax.swing.JTextField sellingDiscountTextField;
    private javax.swing.JLabel sellingPriceLabel;
    private javax.swing.JTextField sellingPriceTextField;
    private javax.swing.JButton setDiscountButton;
    private javax.swing.JLabel stockBarcodeLabel;
    private javax.swing.JTextField stockBarcodeTextField;
    private javax.swing.JLabel supplierNameShowLabel;
    private com.raven.swing.TimePicker timePicker;
    private javax.swing.JLabel totalTextLabel;
    private javax.swing.JLabel totalValueLabel;
    // End of variables declaration//GEN-END:variables
}
