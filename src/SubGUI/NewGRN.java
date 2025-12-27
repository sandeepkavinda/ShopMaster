/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package SubGUI;

import java.awt.Toolkit;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
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
import model.IdGenerater;
import panels.GrnManagement;

/**
 *
 * @author Sandeep
 */
public class NewGRN extends javax.swing.JFrame {

    //Inisilize Varables
    private HashMap<String, Integer> rowNumberMap = new HashMap<String, Integer>();
    private String supplierId;
    private String selectedStockBarcode;
    private String selectedProductName;
    private String selectedBuyingPrice;
    private String selectedSellingPrice;
    private String selectedMeasUnit;
    private boolean isConfirmed;
    private double grnTotal;
    private double grnDiscount;
    private GrnManagement grnManagement;

    public NewGRN(GrnManagement grnManagement) {
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resource/icon.png")));
        initComponents();
        this.grnManagement = grnManagement;
        supplierNameTextField.grabFocus();
        currentDateTimeCheckBox.setSelected(true);
        timePickerTextField.setText("Now");
        confirmedGRN(false);
        rowDiscountTextField.setText("0.00");
    }

    public void setSupplier(String id) {
        try {
            ResultSet results = MySQL.execute("SELECT * FROM stock supplier "
                    + "WHERE id='" + id + "'");

            if (results.next()) {
                supplierId = id;

                supplierNameTextField.setText(results.getString("name"));
                supplierPhoneTextField.setText(results.getString("phone"));

                noteTextField.grabFocus();
                noteTextField.selectAll();
            } else {
                JOptionPane.showMessageDialog(this, "Something Went Wrong", "Unexpected Error", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Something Went Wrong", "Unexpected Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void clearSupplier() {
        supplierId = null;
        supplierNameTextField.setText("");
        supplierPhoneTextField.setText("");
    }

    public void setStock(String stockBarcode) {
        //Set Stock Datails For Row inputs
        try {
            ResultSet results = MySQL.execute("SELECT *,(`marked_price`-`selling_discount`) as `selling_price` FROM `stock` "
                    + "INNER JOIN `product` ON `stock`.`product_id`=`product`.`id` "
                    + "INNER JOIN `measurement_unit` ON `product`.`measurement_unit_id`=`measurement_unit`.`id` "
                    + "WHERE `barcode`='" + stockBarcode + "'");

            if (results.next()) {

                double currentQty = Double.parseDouble(results.getString("current_quantity"));
                double buyingPrice = Double.parseDouble(results.getString("buying_price"));
                double sellingPrice = Double.parseDouble(results.getString("selling_price"));

                selectedStockBarcode = results.getString("stock.barcode");
                selectedProductName = results.getString("product.name");
                selectedBuyingPrice = Numbers.formatPrice(buyingPrice);
                selectedSellingPrice = Numbers.formatPrice(sellingPrice);
                selectedMeasUnit = results.getString("measurement_unit.name");

                stockBarcodeTextField.setText(selectedStockBarcode);
                productNameTextField.setText(selectedProductName);
                buyingPriceTextField.setText(selectedBuyingPrice);
                sellingPriceTextField.setText(selectedSellingPrice);
                quantityLabel.setText("Quantity in " + selectedMeasUnit);
                quantityTextField.grabFocus();
                quantityTextField.selectAll();
            } else {
                JOptionPane.showMessageDialog(this, "Something Went Wrong", "Unexpected Error", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Something Went Wrong", "Unexpected Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }

    }

    //Change Designs Confimed GRNS or Not
    private void confirmedGRN(boolean isConfirmed) {
        if (isConfirmed) {
            noteLabel.setEnabled(false);
            dateLabel.setEnabled(false);
            timeLabel.setEnabled(false);
            supplierNameTextField.setEnabled(false);
            supplierPhoneTextField.setEnabled(false);
            noteTextField.setEnabled(false);
            currentDateTimeCheckBox.setEnabled(false);
            if (!currentDateTimeCheckBox.isSelected()) {
                setDateButton.setEnabled(false);
                setTimeButton.setEnabled(false);
                dateChooseTextField.setEnabled(false);
                timePickerTextField.setEnabled(false);
            }
            confirmGrnButton.setEnabled(false);
            editDetailsButton.setEnabled(true);
            stockBarcodeLabel.setEnabled(true);
            productLabel.setEnabled(true);
            buyingPriceLabel.setEnabled(true);
            sellingPriceLabel.setEnabled(true);
            quantityLabel.setEnabled(true);
            itemDiscountLabel.setEnabled(true);
            stockBarcodeTextField.setEnabled(true);
            addStockButton.setEnabled(true);
            productNameTextField.setEnabled(true);
            buyingPriceTextField.setEnabled(true);
            sellingPriceTextField.setEnabled(true);
            quantityTextField.setEnabled(true);
            rowDiscountTextField.setEnabled(true);
            addRowButton.setEnabled(true);
            clearRowButton.setEnabled(true);
            grnItemsTable.setEnabled(true);
            grnDiscountLabel.setEnabled(true);
            grnDiscountFromattedTextField.setEnabled(true);
            grnSetDiscountButton.setEnabled(true);
            presentageDiscountRadioButton.setEnabled(true);
            amountDiscountRadioButton.setEnabled(true);
            totalTextLabel.setEnabled(true);
            totalValueLabel.setEnabled(true);
            discountTextLabel.setEnabled(true);
            discountValueLabel.setEnabled(true);
            netTotalTextLabel.setEnabled(true);
            netTotalValueLabel.setEnabled(true);
            removeSelectedButton.setEnabled(true);
            removeAllButton.setEnabled(true);
            saveOnlyButton.setEnabled(true);
            saveAndPrintButton.setEnabled(true);
            confirmGrnButton.setText("Confirmed");

        } else {
            noteLabel.setEnabled(true);
            dateLabel.setEnabled(true);
            timeLabel.setEnabled(true);
            supplierNameTextField.setEnabled(true);
            supplierPhoneTextField.setEnabled(true);
            noteTextField.setEnabled(true);
            currentDateTimeCheckBox.setEnabled(true);
            if (!currentDateTimeCheckBox.isSelected()) {
                setDateButton.setEnabled(true);
                setTimeButton.setEnabled(true);
                dateChooseTextField.setEnabled(true);
                timePickerTextField.setEnabled(true);
            }
            confirmGrnButton.setEnabled(true);
            editDetailsButton.setEnabled(false);
            stockBarcodeLabel.setEnabled(false);
            productLabel.setEnabled(false);
            buyingPriceLabel.setEnabled(false);
            sellingPriceLabel.setEnabled(false);
            quantityLabel.setEnabled(false);
            itemDiscountLabel.setEnabled(false);
            stockBarcodeTextField.setEnabled(false);
            addStockButton.setEnabled(false);
            productNameTextField.setEnabled(false);
            buyingPriceTextField.setEnabled(false);
            sellingPriceTextField.setEnabled(false);
            quantityTextField.setEnabled(false);
            rowDiscountTextField.setEnabled(false);
            addRowButton.setEnabled(false);
            clearRowButton.setEnabled(false);
            grnItemsTable.setEnabled(false);
            grnDiscountLabel.setEnabled(false);
            grnDiscountFromattedTextField.setEnabled(false);
            grnSetDiscountButton.setEnabled(false);
            presentageDiscountRadioButton.setEnabled(false);
            amountDiscountRadioButton.setEnabled(false);
            totalTextLabel.setEnabled(false);
            totalValueLabel.setEnabled(false);
            discountTextLabel.setEnabled(false);
            discountValueLabel.setEnabled(false);
            netTotalTextLabel.setEnabled(false);
            netTotalValueLabel.setEnabled(false);
            removeSelectedButton.setEnabled(false);
            removeAllButton.setEnabled(false);
            saveOnlyButton.setEnabled(false);
            saveAndPrintButton.setEnabled(false);
            confirmGrnButton.setText("Confirm GRN");
        }
        this.isConfirmed = isConfirmed;
    }

    private void cleanRowInputs() {
        stockBarcodeTextField.setText("");
        stockBarcodeTextField.setEditable(true);
        productNameTextField.setText("");
        buyingPriceTextField.setText("");
        sellingPriceTextField.setText("");
        quantityTextField.setText("");
        rowDiscountTextField.setText("0.00");
        quantityLabel.setText("Quantity");

        selectedStockBarcode = null;
        selectedProductName = null;
        selectedBuyingPrice = null;
        selectedSellingPrice = null;
        selectedMeasUnit = null;
    }

    private void calculateTotals() {
        double grnTotal = 0.00;
        double grnDiscount = 0.00;

        try {
            int numOfRows = grnItemsTable.getRowCount();
            for (int i = 0; i < numOfRows; i++) {
                double buyingPrice = Double.parseDouble(String.valueOf(grnItemsTable.getValueAt(i, 3)));
                double quantity = Double.parseDouble(String.valueOf(grnItemsTable.getValueAt(i, 5)));
                double discount = Double.parseDouble(String.valueOf(grnItemsTable.getValueAt(i, 7)).replace(",", ""));
                double itemTotal = (buyingPrice * quantity) - discount;
                grnItemsTable.setValueAt(Numbers.formatPrice(itemTotal), i, 8);
                grnTotal += itemTotal;
            }

            if (presentageDiscountRadioButton.isSelected()) {
                double discountPresentage = Double.parseDouble(grnDiscountFromattedTextField.getText());
                if (discountPresentage > 100) {
                    JOptionPane.showMessageDialog(this, "GRN discount must be less than 100%", "Warning", JOptionPane.WARNING_MESSAGE);
                    discountTextLabel.setText("Disount (Rs.) :");
                    grnDiscountFromattedTextField.setText("0.00");
                } else if (discountPresentage < 0) {
                    JOptionPane.showMessageDialog(this, "GRN discount should not be negative", "Warning", JOptionPane.WARNING_MESSAGE);
                    discountTextLabel.setText("Disount (Rs.) :");
                    grnDiscountFromattedTextField.setText("0.00");
                } else {
                    discountTextLabel.setText(Numbers.formatPresentage(discountPresentage) + "% Disount (Rs.) :");
                    grnDiscount = grnTotal * (discountPresentage / 100);
                }

            }

            if (amountDiscountRadioButton.isSelected()) {
                double discountAmount = Double.parseDouble(grnDiscountFromattedTextField.getText());
                if (discountAmount > grnTotal) {
                    JOptionPane.showMessageDialog(this, "GRN discount must be less than Total Amount", "Warning", JOptionPane.WARNING_MESSAGE);
                    discountTextLabel.setText("Disount (Rs.) :");
                    grnDiscountFromattedTextField.setText("0.00");
                } else if (discountAmount < 0) {
                    JOptionPane.showMessageDialog(this, "GRN discount should not be negative", "Warning", JOptionPane.WARNING_MESSAGE);
                    discountTextLabel.setText("Disount (Rs.) :");
                    grnDiscountFromattedTextField.setText("0.00");
                } else {
                    grnDiscount = discountAmount;
                }
            }

            this.grnDiscount = grnDiscount;
            this.grnTotal = grnTotal;

            totalValueLabel.setText(Numbers.formatPrice(grnTotal));
            discountValueLabel.setText(Numbers.formatPrice(grnDiscount));
            netTotalValueLabel.setText(Numbers.formatPrice(grnTotal - grnDiscount));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Something Went Wrong", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void setGrnDiscount() {
        if (!(presentageDiscountRadioButton.isSelected() || amountDiscountRadioButton.isSelected())) {
            JOptionPane.showMessageDialog(this, "Please Select Discount Type", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (grnDiscountFromattedTextField.getText().equals("0.00")) {
            JOptionPane.showMessageDialog(this, "Please enter a discount to set.", "Warning", JOptionPane.WARNING_MESSAGE);
            grnDiscountFromattedTextField.grabFocus();
            grnDiscountFromattedTextField.selectAll();
        } else {
            calculateTotals();
        }
    }

    private void deleteSelectedRow() {
        int selectedRow = grnItemsTable.getSelectedRow();

        if (selectedRow != -1) {
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to remove row number " + grnItemsTable.getValueAt(selectedRow, 0),
                    "Clear Items",
                    JOptionPane.YES_NO_OPTION
            );
            if (result == JOptionPane.YES_OPTION) {
                //Remove Row 
                DefaultTableModel model = (DefaultTableModel) grnItemsTable.getModel();
                model.removeRow(selectedRow);

                //Update Row Number Map
                int newRowcount = grnItemsTable.getRowCount();
                rowNumberMap.clear();
                for (int i = 0; i < newRowcount; i++) {
                    String stockId = String.valueOf(grnItemsTable.getValueAt(i, 1));
                    rowNumberMap.put(stockId, i);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please Select a Row Remove", "Warning", JOptionPane.WARNING_MESSAGE);
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
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        supplierNameLabel = new javax.swing.JLabel();
        supplierNameTextField = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        supplierPhoneLabel = new javax.swing.JLabel();
        supplierPhoneTextField = new javax.swing.JTextField();
        jPanel22 = new javax.swing.JPanel();
        selectSupplierButton = new javax.swing.JButton();
        cleanSupplierButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        noteLabel = new javax.swing.JLabel();
        noteTextField = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        dateLabel = new javax.swing.JLabel();
        dateChooseTextField = new javax.swing.JTextField();
        setDateButton = new javax.swing.JButton();
        jPanel21 = new javax.swing.JPanel();
        setTimeButton = new javax.swing.JButton();
        timePickerTextField = new javax.swing.JTextField();
        timeLabel = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        confirmGrnButton = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        editDetailsButton = new javax.swing.JButton();
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
        buyingPriceTextField = new javax.swing.JTextField();
        jPanel19 = new javax.swing.JPanel();
        sellingPriceLabel = new javax.swing.JLabel();
        sellingPriceTextField = new javax.swing.JTextField();
        jPanel33 = new javax.swing.JPanel();
        quantityLabel = new javax.swing.JLabel();
        quantityTextField = new javax.swing.JTextField();
        jPanel34 = new javax.swing.JPanel();
        itemDiscountLabel = new javax.swing.JLabel();
        rowDiscountTextField = new javax.swing.JTextField();
        jPanel27 = new javax.swing.JPanel();
        addRowButton = new javax.swing.JButton();
        clearRowButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        grnDiscountLabel = new javax.swing.JLabel();
        grnSetDiscountButton = new javax.swing.JButton();
        amountDiscountRadioButton = new javax.swing.JRadioButton();
        presentageDiscountRadioButton = new javax.swing.JRadioButton();
        grnDiscountFromattedTextField = new javax.swing.JFormattedTextField();
        jPanel16 = new javax.swing.JPanel();
        totalValueLabel = new javax.swing.JLabel();
        discountValueLabel = new javax.swing.JLabel();
        netTotalValueLabel = new javax.swing.JLabel();
        netTotalTextLabel = new javax.swing.JLabel();
        discountTextLabel = new javax.swing.JLabel();
        totalTextLabel = new javax.swing.JLabel();
        saveAndPrintButton = new javax.swing.JButton();
        saveOnlyButton = new javax.swing.JButton();
        removeAllButton = new javax.swing.JButton();
        removeSelectedButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        grnItemsTable = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel9 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        currentDateTimeCheckBox = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        supplierNameShowLabel = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        dateChooser.setForeground(new java.awt.Color(0, 105, 75));
        dateChooser.setDateFormat("yyyy-MM-dd");
        dateChooser.setTextRefernce(dateChooseTextField);

        timePicker.setForeground(new java.awt.Color(0, 105, 75));
        timePicker.set24hourMode(true);
        timePicker.setDisplayText(timePickerTextField);

        jMenu1.setText("jMenu1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("New Goods Received Note");
        setMinimumSize(new java.awt.Dimension(1090, 696));

        jPanel3.setLayout(new java.awt.GridLayout(1, 0));

        jPanel6.setForeground(new java.awt.Color(255, 51, 51));

        supplierNameLabel.setText("Supplier Name");

        supplierNameTextField.setEditable(false);
        supplierNameTextField.setEnabled(false);
        supplierNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierNameTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(supplierNameTextField)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(supplierNameLabel)
                        .addGap(0, 37, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(supplierNameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(supplierNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel6);

        jPanel15.setForeground(new java.awt.Color(255, 51, 51));

        supplierPhoneLabel.setText("Supplier Phone No.");

        supplierPhoneTextField.setEditable(false);
        supplierPhoneTextField.setEnabled(false);
        supplierPhoneTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierPhoneTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(supplierPhoneTextField)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(supplierPhoneLabel)
                        .addGap(0, 13, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(supplierPhoneLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(supplierPhoneTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel15);

        jPanel22.setForeground(new java.awt.Color(255, 51, 51));

        selectSupplierButton.setText("Select");
        selectSupplierButton.setToolTipText("Select Supplier");
        selectSupplierButton.setBorder(null);
        selectSupplierButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectSupplierButtonActionPerformed(evt);
            }
        });

        cleanSupplierButton.setBackground(new java.awt.Color(102, 102, 102));
        cleanSupplierButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/clean.png"))); // NOI18N
        cleanSupplierButton.setToolTipText("Clear Supplier");
        cleanSupplierButton.setBorder(null);
        cleanSupplierButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cleanSupplierButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(selectSupplierButton, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cleanSupplierButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cleanSupplierButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(selectSupplierButton, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel3.add(jPanel22);

        jPanel4.setForeground(new java.awt.Color(255, 51, 51));

        noteLabel.setText("Notes");

        noteTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noteTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(noteTextField)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(noteLabel)
                        .addGap(0, 84, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(noteLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(noteTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel4);

        jPanel17.setForeground(new java.awt.Color(255, 51, 51));

        dateLabel.setText("Date");

        setDateButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/calendar.png"))); // NOI18N
        setDateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setDateButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(dateChooseTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(setDateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(dateLabel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dateLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateChooseTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(setDateButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel3.add(jPanel17);

        jPanel21.setForeground(new java.awt.Color(255, 51, 51));

        setTimeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/clock.png"))); // NOI18N
        setTimeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setTimeButtonActionPerformed(evt);
            }
        });

        timePickerTextField.setEditable(false);

        timeLabel.setText("Time");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(timePickerTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(setTimeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(timeLabel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(timeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(setTimeButton, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(timePickerTextField))
                .addContainerGap())
        );

        jPanel3.add(jPanel21);

        jPanel8.setForeground(new java.awt.Color(255, 51, 51));

        confirmGrnButton.setText("Confirm GRN");
        confirmGrnButton.setBorder(null);
        confirmGrnButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmGrnButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(confirmGrnButton, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(confirmGrnButton, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel8);

        jPanel10.setForeground(new java.awt.Color(255, 51, 51));

        editDetailsButton.setBackground(new java.awt.Color(102, 102, 102));
        editDetailsButton.setForeground(new java.awt.Color(255, 255, 255));
        editDetailsButton.setText("Edit");
        editDetailsButton.setBorder(null);
        editDetailsButton.setEnabled(false);
        editDetailsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editDetailsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(editDetailsButton, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(editDetailsButton, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel10);

        jPanel11.setLayout(new java.awt.GridLayout(1, 0));

        jPanel12.setForeground(new java.awt.Color(255, 51, 51));

        stockBarcodeLabel.setText("Stock Barcode");
        stockBarcodeLabel.setEnabled(false);

        addStockButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/add.png"))); // NOI18N
        addStockButton.setToolTipText("Select Product");
        addStockButton.setBorder(null);
        addStockButton.setEnabled(false);
        addStockButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStockButtonActionPerformed(evt);
            }
        });

        stockBarcodeTextField.setEnabled(false);
        stockBarcodeTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                stockBarcodeTextFieldFocusLost(evt);
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
        productLabel.setEnabled(false);

        productNameTextField.setEditable(false);
        productNameTextField.setEnabled(false);
        productNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                productNameTextFieldActionPerformed(evt);
            }
        });

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

        buyingPriceLabel.setText("Buying Price");
        buyingPriceLabel.setEnabled(false);

        buyingPriceTextField.setEditable(false);
        buyingPriceTextField.setEnabled(false);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(buyingPriceLabel)
                        .addGap(0, 67, Short.MAX_VALUE))
                    .addComponent(buyingPriceTextField))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buyingPriceLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buyingPriceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel11.add(jPanel14);

        jPanel19.setForeground(new java.awt.Color(255, 51, 51));

        sellingPriceLabel.setText("Selling Price");
        sellingPriceLabel.setEnabled(false);

        sellingPriceTextField.setEditable(false);
        sellingPriceTextField.setEnabled(false);

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
        quantityLabel.setEnabled(false);

        quantityTextField.setEnabled(false);
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

        jPanel34.setForeground(new java.awt.Color(255, 51, 51));

        itemDiscountLabel.setText("Discount (Rs.)");
        itemDiscountLabel.setEnabled(false);

        rowDiscountTextField.setEnabled(false);
        rowDiscountTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rowDiscountTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addComponent(itemDiscountLabel)
                        .addGap(0, 60, Short.MAX_VALUE))
                    .addComponent(rowDiscountTextField))
                .addContainerGap())
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(itemDiscountLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rowDiscountTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel11.add(jPanel34);

        jPanel27.setForeground(new java.awt.Color(255, 51, 51));

        addRowButton.setText("Add Row");
        addRowButton.setToolTipText("Add New Product");
        addRowButton.setBorder(null);
        addRowButton.setEnabled(false);
        addRowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addRowButtonActionPerformed(evt);
            }
        });

        clearRowButton.setBackground(new java.awt.Color(102, 102, 102));
        clearRowButton.setForeground(new java.awt.Color(255, 255, 255));
        clearRowButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/clean.png"))); // NOI18N
        clearRowButton.setBorder(null);
        clearRowButton.setEnabled(false);
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
                .addComponent(addRowButton, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clearRowButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addRowButton, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(clearRowButton, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel11.add(jPanel27);

        grnDiscountLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        grnDiscountLabel.setText("GRN Discount");
        grnDiscountLabel.setEnabled(false);

        grnSetDiscountButton.setText("Set");
        grnSetDiscountButton.setToolTipText("Add New Product");
        grnSetDiscountButton.setBorder(null);
        grnSetDiscountButton.setEnabled(false);
        grnSetDiscountButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grnSetDiscountButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(amountDiscountRadioButton);
        amountDiscountRadioButton.setText("Amount (Rs.)");
        amountDiscountRadioButton.setEnabled(false);

        buttonGroup1.add(presentageDiscountRadioButton);
        presentageDiscountRadioButton.setText("Presentage (%)");
        presentageDiscountRadioButton.setEnabled(false);
        presentageDiscountRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                presentageDiscountRadioButtonItemStateChanged(evt);
            }
        });

        grnDiscountFromattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("0.00"))));
        grnDiscountFromattedTextField.setText("0.00");
        grnDiscountFromattedTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grnDiscountFromattedTextFieldActionPerformed(evt);
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
                        .addComponent(grnDiscountFromattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(grnSetDiscountButton, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(grnDiscountLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(presentageDiscountRadioButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(amountDiscountRadioButton)
                        .addGap(12, 12, 12)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(grnDiscountLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(grnSetDiscountButton, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(grnDiscountFromattedTextField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(presentageDiscountRadioButton)
                    .addComponent(amountDiscountRadioButton))
                .addContainerGap())
        );

        totalValueLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        totalValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalValueLabel.setText("0.00");
        totalValueLabel.setEnabled(false);

        discountValueLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        discountValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        discountValueLabel.setText("0.00");
        discountValueLabel.setEnabled(false);

        netTotalValueLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        netTotalValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        netTotalValueLabel.setText("0.00");
        netTotalValueLabel.setEnabled(false);

        netTotalTextLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        netTotalTextLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        netTotalTextLabel.setText("Net Total (Rs.) :");
        netTotalTextLabel.setEnabled(false);

        discountTextLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        discountTextLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        discountTextLabel.setText("Disount (Rs.) :");
        discountTextLabel.setEnabled(false);

        totalTextLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        totalTextLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalTextLabel.setText("Total (Rs.) :");
        totalTextLabel.setEnabled(false);

        saveAndPrintButton.setText("Save & Print");
        saveAndPrintButton.setToolTipText("Add New Product");
        saveAndPrintButton.setBorder(null);
        saveAndPrintButton.setEnabled(false);

        saveOnlyButton.setText("Save Only");
        saveOnlyButton.setToolTipText("Add New Product");
        saveOnlyButton.setBorder(null);
        saveOnlyButton.setEnabled(false);
        saveOnlyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveOnlyButtonActionPerformed(evt);
            }
        });

        removeAllButton.setBackground(new java.awt.Color(102, 102, 102));
        removeAllButton.setForeground(new java.awt.Color(255, 255, 255));
        removeAllButton.setText("Remove All");
        removeAllButton.setBorder(null);
        removeAllButton.setEnabled(false);
        removeAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeAllButtonActionPerformed(evt);
            }
        });

        removeSelectedButton.setBackground(new java.awt.Color(102, 102, 102));
        removeSelectedButton.setForeground(new java.awt.Color(255, 255, 255));
        removeSelectedButton.setText("Remove Selected");
        removeSelectedButton.setBorder(null);
        removeSelectedButton.setEnabled(false);
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
                        .addComponent(saveOnlyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveAndPrintButton, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(saveAndPrintButton, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(saveOnlyButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        grnItemsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Stock Barcode", "Product Name", "Buying Price", "Selling Price", "Quantity", "Measurement Unit", "Discount", "Item Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        grnItemsTable.setEnabled(false);
        grnItemsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        grnItemsTable.getTableHeader().setReorderingAllowed(false);

        // Center-align the first column (index 0)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        grnItemsTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        grnItemsTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        grnItemsTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        grnItemsTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        grnItemsTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        grnItemsTable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        grnItemsTable.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
        grnItemsTable.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);

        // Right-align the second column (index 1)
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        grnItemsTable.getColumnModel().getColumn(8).setCellRenderer(rightRenderer);
        grnItemsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                grnItemsTableMouseClicked(evt);
            }
        });
        grnItemsTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                grnItemsTableKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(grnItemsTable);
        if (grnItemsTable.getColumnModel().getColumnCount() > 0) {
            grnItemsTable.getColumnModel().getColumn(0).setMinWidth(40);
            grnItemsTable.getColumnModel().getColumn(0).setPreferredWidth(40);
            grnItemsTable.getColumnModel().getColumn(0).setMaxWidth(40);
        }

        jSeparator1.setForeground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1018, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1018, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addComponent(jSeparator1))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 0, 0, 0));
        jPanel9.setLayout(new java.awt.GridLayout(1, 0));

        currentDateTimeCheckBox.setText("Use Current Date Time To GRN");
        currentDateTimeCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                currentDateTimeCheckBoxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(currentDateTimeCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(currentDateTimeCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void noteTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noteTextFieldActionPerformed
        if (currentDateTimeCheckBox.isSelected()) {
            confirmGrnButton.doClick();
        }
    }//GEN-LAST:event_noteTextFieldActionPerformed

    private void supplierNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierNameTextFieldActionPerformed
        supplierPhoneTextField.grabFocus();
    }//GEN-LAST:event_supplierNameTextFieldActionPerformed

    private void addStockButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStockButtonActionPerformed
        grnItemsTable.clearSelection();
        cleanRowInputs();
        SelectStock selectStock = new SelectStock(this, true, this, null);
        selectStock.setVisible(true);
    }//GEN-LAST:event_addStockButtonActionPerformed

    private void stockBarcodeTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stockBarcodeTextFieldActionPerformed
        // Remove all whitespaces (spaces, tabs, newlines)
        String barcode = stockBarcodeTextField.getText().replaceAll("\\s+", "");
        stockBarcodeTextField.setText(barcode);

        if (!barcode.isBlank()) {

            try {

                ResultSet results = MySQL.execute("SELECT * FROM `stock` "
                        + "WHERE `barcode`='" + barcode + "'");

                if (results.next()) {
                    setStock(barcode);
                    stockBarcodeTextField.setEditable(false);
                } else {
                    JOptionPane.showMessageDialog(this, "Not Found This Barcode", "Warning", JOptionPane.WARNING_MESSAGE);
                    stockBarcodeTextField.selectAll();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Enter Barcode or Select stock", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_stockBarcodeTextFieldActionPerformed

    private void stockBarcodeTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_stockBarcodeTextFieldFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_stockBarcodeTextFieldFocusLost

    private void productNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_productNameTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_productNameTextFieldActionPerformed

    private void supplierPhoneTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierPhoneTextFieldActionPerformed
        noteTextField.grabFocus();
    }//GEN-LAST:event_supplierPhoneTextFieldActionPerformed

    private void quantityTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quantityTextFieldActionPerformed
        String quantity = quantityTextField.getText();
        if (quantity.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter Quantity", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!Validation.isValidQuantity(quantity)) {
            System.out.println(quantity);
            JOptionPane.showMessageDialog(this, "<html>Quantity must be a whole number or decimal<br>(e.g., 10 or 10.5).</html>", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (Double.parseDouble(quantity) == 0) {
            JOptionPane.showMessageDialog(this, "Quantity Can't be \"0\"", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (Double.parseDouble(quantity) < 0) {
            JOptionPane.showMessageDialog(this, "Quantity Can't be Negetive", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            rowDiscountTextField.grabFocus();
            rowDiscountTextField.selectAll();
        }


    }//GEN-LAST:event_quantityTextFieldActionPerformed

    private void removeAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeAllButtonActionPerformed

        int rowCount = grnItemsTable.getRowCount();
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
                DefaultTableModel model = (DefaultTableModel) grnItemsTable.getModel();
                model.setRowCount(0);
                rowNumberMap.clear();
                JOptionPane.showMessageDialog(this, "Removed All Items Successfully", "Success", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/resource/success.png")));
            }

        }


    }//GEN-LAST:event_removeAllButtonActionPerformed

    private void setDateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setDateButtonActionPerformed
        dateChooser.showPopup();
    }//GEN-LAST:event_setDateButtonActionPerformed

    private void setTimeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setTimeButtonActionPerformed
        timePicker.showPopup(timePickerTextField, 0, 37);
    }//GEN-LAST:event_setTimeButtonActionPerformed

    private void currentDateTimeCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_currentDateTimeCheckBoxItemStateChanged
        // TODO add your handling code here:
        if (currentDateTimeCheckBox.isSelected()) {
            dateChooseTextField.setText("Today");
            timePickerTextField.setText("Now");
            setDateButton.setEnabled(false);
            setTimeButton.setEnabled(false);
            dateChooseTextField.setEnabled(false);
            timePickerTextField.setEnabled(false);
        } else {
            dateChooser.toDay();
            timePicker.now();
            timePickerTextField.setText(timePicker.getSelectedTime());
            setDateButton.setEnabled(true);
            setTimeButton.setEnabled(true);
            dateChooseTextField.setEnabled(true);
            timePickerTextField.setEnabled(true);
        }
    }//GEN-LAST:event_currentDateTimeCheckBoxItemStateChanged

    private void confirmGrnButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmGrnButtonActionPerformed
        String supplierName = supplierNameTextField.getText();
        String supplierMobile = supplierPhoneTextField.getText();
        String note = noteTextField.getText();
        String date = dateChooseTextField.getText();
        String time = timePickerTextField.getText() + ":00";
        boolean isPastDateTime = true;
        boolean isMobileEmpty = false;
        boolean isNoteEmpty = false;

        if (!currentDateTimeCheckBox.isSelected()) {
            try {
                Date dateTime = Numbers.getDateObject(date + " " + time + ":00");
                isPastDateTime = new Date().after(dateTime);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Something Went Wrong in Selected Date Time", "Something Went Wrong", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        if (supplierName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter Supplier Name", "Warning", JOptionPane.WARNING_MESSAGE);
            supplierNameTextField.grabFocus();
        } else if (supplierName.length() > 50) {
            JOptionPane.showMessageDialog(this, "Supplier name cannot exceed 50 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
            supplierNameTextField.grabFocus();
        } else if (supplierMobile.isEmpty()) {
            JOptionPane.showMessageDialog(this, "<html>Please Enter Supplier Mobile.<br/>Enter <b>\"-\"</b> if no supplier mobile.</html>", "Warning", JOptionPane.WARNING_MESSAGE);
            supplierPhoneTextField.grabFocus();
        } else if (!Validation.isValidMobileBasic(supplierMobile)) {
            JOptionPane.showMessageDialog(this, "<html>Mobile number must be <b>10 digits</b> and<br><b>only contain digits.</b><html>", "Invalid Mobile", JOptionPane.WARNING_MESSAGE);
            supplierPhoneTextField.grabFocus();
        } else if (note.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter a Note", "Warning", JOptionPane.WARNING_MESSAGE);
            noteTextField.grabFocus();
        } else if (!isPastDateTime) {
            JOptionPane.showMessageDialog(this, "Selected Date and Time cannot be in the future.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            confirmedGRN(true);
            stockBarcodeTextField.grabFocus();
        }

    }//GEN-LAST:event_confirmGrnButtonActionPerformed

    private void editDetailsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editDetailsButtonActionPerformed
        confirmedGRN(false);
    }//GEN-LAST:event_editDetailsButtonActionPerformed

    private void grnDiscountFromattedTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grnDiscountFromattedTextFieldActionPerformed
        setGrnDiscount();

    }//GEN-LAST:event_grnDiscountFromattedTextFieldActionPerformed

    private void grnSetDiscountButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grnSetDiscountButtonActionPerformed
        setGrnDiscount();
        grnSetDiscountButton.grabFocus();
    }//GEN-LAST:event_grnSetDiscountButtonActionPerformed

    private void presentageDiscountRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_presentageDiscountRadioButtonItemStateChanged
        if (presentageDiscountRadioButton.isSelected()) {
            grnDiscountLabel.setText("GRN Discount (%)");
        } else if (amountDiscountRadioButton.isSelected()) {
            grnDiscountLabel.setText("GRN Discount (Rs.)");
        } else {
            grnDiscountLabel.setText("GRN Discount");
        }
    }//GEN-LAST:event_presentageDiscountRadioButtonItemStateChanged

    private void grnItemsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_grnItemsTableMouseClicked
        if (evt.getClickCount() == 3) {
            deleteSelectedRow();
        }
    }//GEN-LAST:event_grnItemsTableMouseClicked

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        addStockButton.doClick();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void saveOnlyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveOnlyButtonActionPerformed

        if (!isConfirmed) {
            JOptionPane.showMessageDialog(this, "Please enter GRN details and confirm it first.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            int rowCount = grnItemsTable.getRowCount();

            if (rowCount < 1) {
                JOptionPane.showMessageDialog(this, "No Items in the GRN", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    //Generate GRN Barcode
                    String newBarcode = IdGenerater.generateId("grn", "barcode", "GRN");

                    //Set Date and Time
                    String dateTime = "";
                    if (currentDateTimeCheckBox.isSelected()) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        dateTime = format.format(new Date());
                    } else {
                        String date = dateChooseTextField.getText();
                        String time = timePickerTextField.getText() + ":00";
                        dateTime = date + " " + time;
                    }

                    //Instet to GRN Table 
                    String supplierName = supplierNameTextField.getText();
                    String supplierMobile = supplierPhoneTextField.getText();
                    String note = noteTextField.getText();

                    MySQL.execute("INSERT INTO `grn` (`barcode`,`supplier_name`,`supplier_mobile`,`note`,`date_time`,`amount`,`discount`) "
                            + "VALUES ('" + newBarcode + "','" + supplierName + "','" + supplierMobile + "','" + note + "','" + dateTime + "','" + this.grnTotal + "','" + this.grnDiscount + "')");

                    for (int i = 0; i < rowCount; i++) {
                        String stockBarcode = String.valueOf(grnItemsTable.getValueAt(i, 1));
                        String quantity = String.valueOf(grnItemsTable.getValueAt(i, 5));
                        String discount = String.valueOf(grnItemsTable.getValueAt(i, 7));

                        MySQL.execute("INSERT INTO `grn_item` (`grn_barcode`,`stock_barcode`,`quantity`,`discount`) "
                                + "VALUES ('" + newBarcode + "','" + stockBarcode + "','" + quantity + "','" + discount + "')");
                    }
                    grnManagement.loadGRNTable();
                    JOptionPane.showMessageDialog(this, "GRN Added Successfully", "Success", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/resource/success.png")));
                    this.dispose();
                    //new NewGRN(grnManagement).setVisible(true);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                    e.printStackTrace();
                }
            }
        }
    }//GEN-LAST:event_saveOnlyButtonActionPerformed

    private void removeSelectedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeSelectedButtonActionPerformed
        deleteSelectedRow();
    }//GEN-LAST:event_removeSelectedButtonActionPerformed

    private void grnItemsTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_grnItemsTableKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_grnItemsTableKeyReleased

    private void rowDiscountTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rowDiscountTextFieldActionPerformed
        addRowButton.doClick();
    }//GEN-LAST:event_rowDiscountTextFieldActionPerformed

    private void clearRowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearRowButtonActionPerformed
        cleanRowInputs();
    }//GEN-LAST:event_clearRowButtonActionPerformed

    private void addRowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addRowButtonActionPerformed
        try {
            String quantity = quantityTextField.getText();
            String discount = rowDiscountTextField.getText();

            if (selectedStockBarcode == null) {
                JOptionPane.showMessageDialog(this, "Please Select the Stock", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (quantity.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please Enter Quantity", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (!Validation.isValidQuantity(quantity)) {
                JOptionPane.showMessageDialog(this, "<html>Quantity must be a whole number or decimal<br>(e.g., 10 or 10.5).</html>", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (Double.parseDouble(quantity) == 0) {
                JOptionPane.showMessageDialog(this, "Quantity Can't be \"0\"", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (Double.parseDouble(quantity) < 0) {
                JOptionPane.showMessageDialog(this, "Quantity Can't be Negetive", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (!Validation.isValidPrice(discount)) {
                JOptionPane.showMessageDialog(this, "Invalid Discount", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                if (rowNumberMap.containsKey(selectedStockBarcode)) {
                    //Already Have a row from this stock
                    int rowNumber = rowNumberMap.get(selectedStockBarcode);
                    double currentQty = Double.parseDouble(String.valueOf(grnItemsTable.getValueAt(rowNumber, 5)));
                    double newQuantity = Double.parseDouble(quantity);
                    double currentDiscount = Double.parseDouble(String.valueOf(grnItemsTable.getValueAt(rowNumber, 7)));
                    double newDiscount = Double.parseDouble(discount);
                    grnItemsTable.setValueAt(Numbers.formatQuantity(currentQty + newQuantity), rowNumber, 5);
                    grnItemsTable.setValueAt(Numbers.formatPrice(currentDiscount + newDiscount), rowNumber, 7);
                    grnItemsTable.setRowSelectionInterval(rowNumber, rowNumber);
                } else {
                    //New Row
                    DefaultTableModel model = (DefaultTableModel) grnItemsTable.getModel();
                    int rowCount = grnItemsTable.getRowCount();

                    //Input To grn items Table
                    Vector v = new Vector();
                    v.add(rowCount + 1);
                    v.add(selectedStockBarcode);
                    v.add(selectedProductName);
                    v.add(selectedBuyingPrice);
                    v.add(selectedSellingPrice);
                    v.add(Numbers.formatQuantity(Double.parseDouble(quantity)));
                    v.add(selectedMeasUnit);
                    v.add(Numbers.formatPrice(Double.parseDouble(discount)));
                    model.addRow(v);

                    //Add To Row Numbers Hashmap
                    rowNumberMap.put(selectedStockBarcode, rowCount);

                    grnItemsTable.setRowSelectionInterval(rowCount, rowCount);
                }
                stockBarcodeTextField.grabFocus();
                calculateTotals();
                cleanRowInputs();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Something Went Wrong Try Again", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_addRowButtonActionPerformed

    private void selectSupplierButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectSupplierButtonActionPerformed
        SelectSupplier selectSupplier = new SelectSupplier(this, true, this);
        selectSupplier.setVisible(true);
    }//GEN-LAST:event_selectSupplierButtonActionPerformed

    private void cleanSupplierButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cleanSupplierButtonActionPerformed
        clearSupplier();
    }//GEN-LAST:event_cleanSupplierButtonActionPerformed

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
    private javax.swing.JButton addRowButton;
    private javax.swing.JButton addStockButton;
    private javax.swing.JRadioButton amountDiscountRadioButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel buyingPriceLabel;
    private javax.swing.JTextField buyingPriceTextField;
    private javax.swing.JButton cleanSupplierButton;
    private javax.swing.JButton clearRowButton;
    private javax.swing.JButton confirmGrnButton;
    private javax.swing.JCheckBox currentDateTimeCheckBox;
    private javax.swing.JTextField dateChooseTextField;
    private com.raven.datechooser.DateChooser dateChooser;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JLabel discountTextLabel;
    private javax.swing.JLabel discountValueLabel;
    private javax.swing.JButton editDetailsButton;
    private javax.swing.JFormattedTextField grnDiscountFromattedTextField;
    private javax.swing.JLabel grnDiscountLabel;
    private javax.swing.JTable grnItemsTable;
    private javax.swing.JButton grnSetDiscountButton;
    private javax.swing.JLabel itemDiscountLabel;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel netTotalTextLabel;
    private javax.swing.JLabel netTotalValueLabel;
    private javax.swing.JLabel noteLabel;
    private javax.swing.JTextField noteTextField;
    private javax.swing.JRadioButton presentageDiscountRadioButton;
    private javax.swing.JLabel productLabel;
    private javax.swing.JTextField productNameTextField;
    private javax.swing.JLabel quantityLabel;
    private javax.swing.JTextField quantityTextField;
    private javax.swing.JButton removeAllButton;
    private javax.swing.JButton removeSelectedButton;
    private javax.swing.JTextField rowDiscountTextField;
    private javax.swing.JButton saveAndPrintButton;
    private javax.swing.JButton saveOnlyButton;
    private javax.swing.JButton selectSupplierButton;
    private javax.swing.JLabel sellingPriceLabel;
    private javax.swing.JTextField sellingPriceTextField;
    private javax.swing.JButton setDateButton;
    private javax.swing.JButton setTimeButton;
    private javax.swing.JLabel stockBarcodeLabel;
    private javax.swing.JTextField stockBarcodeTextField;
    private javax.swing.JLabel supplierNameLabel;
    private javax.swing.JLabel supplierNameShowLabel;
    private javax.swing.JTextField supplierNameTextField;
    private javax.swing.JLabel supplierPhoneLabel;
    private javax.swing.JTextField supplierPhoneTextField;
    private javax.swing.JLabel timeLabel;
    private com.raven.swing.TimePicker timePicker;
    private javax.swing.JTextField timePickerTextField;
    private javax.swing.JLabel totalTextLabel;
    private javax.swing.JLabel totalValueLabel;
    // End of variables declaration//GEN-END:variables
}
