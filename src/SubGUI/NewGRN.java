/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package SubGUI;

import DTO.GrnItemDTO;
import DTO.GrnTotalsDTO;
import DTO.StockDataDTO;
import com.raven.datechooser.SelectedDate;
import enums.DiscountType;
import java.awt.Toolkit;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.BigDecimalFormatter;
import model.IdGenerater;
import java.sql.Timestamp;
import java.sql.SQLException;

import model.MySQL;
import model.Numbers;
import model.Validation;
import panels.GrnManagement;
import utils.ToastUtils;
import java.sql.PreparedStatement;
import javax.swing.ImageIcon;

/**
 *
 * @author Sandeep
 */
public class NewGRN extends javax.swing.JFrame {

    //Inisilize Varables
    private HashMap<String, Integer> rowNumberMap = new HashMap<String, Integer>();
    private List<GrnItemDTO> grnItemList = new ArrayList<>();

    private String supplierId;
    private StockDataDTO selectedStockData;

    private boolean isConfirmed;

    private GrnTotalsDTO totalsDTO = new GrnTotalsDTO();
    private DiscountType discountType;
    private BigDecimal discountValue = BigDecimal.ZERO;
    private BigDecimal taxPercentage = BigDecimal.ZERO;

    private GrnManagement grnManagement;

    public NewGRN(GrnManagement grnManagement) {
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resource/icon.png")));
        initComponents();
        this.grnManagement = grnManagement;
        selectSupplierButton.grabFocus();
        currentDateTimeCheckBox.setSelected(true);
        timePickerTextField.setText("Now");
        expDateChooseTextField.setText("No Exp Date");

        // Table Alighment
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

        grnItemsTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        grnItemsTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        grnItemsTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        grnItemsTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        grnItemsTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        grnItemsTable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        grnItemsTable.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
        grnItemsTable.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);

        confirmedGRN(false);
        this.setVisible(true);

    }

    private void openSelectSupplier() {
        SelectSupplier selectSupplier = new SelectSupplier(this, true, this);
        selectSupplier.setVisible(true);
    }

    public void setSupplier(String id) {
        try {
            ResultSet results = MySQL.execute("SELECT * FROM supplier "
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
        selectSupplierButton.grabFocus();

        ToastUtils.showBottomToast(this, "Supplier cleared", 1500);
    }

    private void useCurrentDateTimeToggle() {

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
    }

    //    Change Designs Confimed GRNS or Not
    private void confirmedGRN(boolean isConfirmed) {
        if (isConfirmed) {
            supplierNameLabel.setEnabled(false);
            supplierPhoneLabel.setEnabled(false);
            selectSupplierButton.setEnabled(false);
            cleanSupplierButton.setEnabled(false);
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
            markedPriceLabel.setEnabled(true);
            quantityLabel.setEnabled(true);
            expDateCheckBox.setEnabled(true);

            stockBarcodeTextField.setEnabled(true);
            addStockButton.setEnabled(true);
            productNameTextField.setEnabled(true);
            costPriceFormattedTextField.setEnabled(true);
            markedPriceFormattedTextField.setEnabled(true);
            quantityFormattedTextField.setEnabled(true);
            addRowButton.setEnabled(true);
            clearRowButton.setEnabled(true);
            grnItemsTable.setEnabled(true);

            grnDiscountLabel.setEnabled(true);
            grnDiscountFromattedTextField.setEnabled(true);
            setDiscountButton.setEnabled(true);
            clearDiscountButton.setEnabled(true);
            presentageDiscountRadioButton.setEnabled(true);
            amountDiscountRadioButton.setEnabled(true);

            grnTaxLabel.setEnabled(true);
            taxFromattedTextField.setEnabled(true);
            setTaxButton.setEnabled(true);
            clearTaxButton.setEnabled(true);

            totalTextLabel.setEnabled(true);
            totalValueLabel.setEnabled(true);
            discountTextLabel.setEnabled(true);
            discountValueLabel.setEnabled(true);
            netTotalTextLabel.setEnabled(true);
            netTotalValueLabel.setEnabled(true);
            taxTextLabel.setEnabled(true);
            taxValueLabel.setEnabled(true);
            grnTotalTextLabel.setEnabled(true);
            grnTotalValueLabel.setEnabled(true);

            removeSelectedButton.setEnabled(true);
            removeAllButton.setEnabled(true);
            saveButton.setEnabled(true);
            confirmGrnButton.setText("Confirmed");

        } else {
            supplierNameLabel.setEnabled(true);
            supplierPhoneLabel.setEnabled(true);
            selectSupplierButton.setEnabled(true);
            cleanSupplierButton.setEnabled(true);
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
            markedPriceLabel.setEnabled(false);
            quantityLabel.setEnabled(false);
            expDateCheckBox.setEnabled(false);

            stockBarcodeTextField.setEnabled(false);
            addStockButton.setEnabled(false);
            productNameTextField.setEnabled(false);
            costPriceFormattedTextField.setEnabled(false);
            markedPriceFormattedTextField.setEnabled(false);
            quantityFormattedTextField.setEnabled(false);
            addRowButton.setEnabled(false);
            clearRowButton.setEnabled(false);
            grnItemsTable.setEnabled(false);

            grnDiscountLabel.setEnabled(false);
            grnDiscountFromattedTextField.setEnabled(false);
            setDiscountButton.setEnabled(false);
            clearDiscountButton.setEnabled(false);
            presentageDiscountRadioButton.setEnabled(false);
            amountDiscountRadioButton.setEnabled(false);

            grnTaxLabel.setEnabled(false);
            taxFromattedTextField.setEnabled(false);
            setTaxButton.setEnabled(false);
            clearTaxButton.setEnabled(false);

            totalTextLabel.setEnabled(false);
            totalValueLabel.setEnabled(false);
            discountTextLabel.setEnabled(false);
            discountValueLabel.setEnabled(false);
            netTotalTextLabel.setEnabled(false);
            netTotalValueLabel.setEnabled(false);
            taxTextLabel.setEnabled(false);
            taxValueLabel.setEnabled(false);
            grnTotalTextLabel.setEnabled(false);
            grnTotalValueLabel.setEnabled(false);

            removeSelectedButton.setEnabled(false);
            removeAllButton.setEnabled(false);
            saveButton.setEnabled(false);
            confirmGrnButton.setText("Confirm GRN");
        }
        this.isConfirmed = isConfirmed;
    }

    public void setStock(String stockBarcode) {
        try {
            ResultSet results = MySQL.execute("SELECT * FROM stock s "
                    + "INNER JOIN product p ON s.product_id=p.id "
                    + "INNER JOIN measurement_unit mu ON p.measurement_unit_id = mu.id "
                    + "WHERE s.barcode='" + stockBarcode + "'");

            if (results.next()) {

                stockBarcodeTextField.setText(results.getString("s.barcode"));
                productNameTextField.setText(results.getString("p.name"));
                quantityLabel.setText("Quantity in " + results.getString("mu.name"));
                costPriceFormattedTextField.grabFocus();
                costPriceFormattedTextField.selectAll();

                selectedStockData = new StockDataDTO();
                selectedStockData.setBarcode(results.getString("s.barcode"));
                selectedStockData.setProductId(results.getInt("p.id"));
                selectedStockData.setProductName(results.getString("p.name"));
                selectedStockData.setMeasUnitShortForm(results.getString("mu.short_form"));

                stockBarcodeTextField.setEditable(false);
                addStockButton.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(this, "Something Went Wrong", "Unexpected Error", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Something Went Wrong", "Unexpected Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }

    }

    private void cleanGrnItemInputs() {
        stockBarcodeTextField.setText("");
        stockBarcodeTextField.setEditable(true);
        addStockButton.setEnabled(true);
        productNameTextField.setText("");
        costPriceFormattedTextField.setText("");
        markedPriceFormattedTextField.setText("");
        quantityFormattedTextField.setText("");
        quantityLabel.setText("Quantity");
        expDateCheckBox.setSelected(false);
        selectedStockData = null;

        stockBarcodeTextField.grabFocus();
    }

    private void addGrnItem() {

        String costPrice = costPriceFormattedTextField.getText().replace(",", "");
        String markedPrice = markedPriceFormattedTextField.getText().replace(",", "");
        String quantity = quantityFormattedTextField.getText().replace(",", "");

        if (selectedStockData == null) {
            JOptionPane.showMessageDialog(this, "Please Select a Stock", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!Validation.isValidBigDecimal(costPrice)) {
            JOptionPane.showMessageDialog(null, "Invalid Cost Price", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (new BigDecimal(costPrice).compareTo(BigDecimal.ZERO) < 0) {
            JOptionPane.showMessageDialog(null, "Cost Price cannot be negative.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!Validation.isValidBigDecimal(markedPrice)) {
            JOptionPane.showMessageDialog(null, "Invalid Marked Price", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (new BigDecimal(markedPrice).compareTo(BigDecimal.ZERO) < 0) {
            JOptionPane.showMessageDialog(null, "Marked Price cannot be negative.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!Validation.isValidBigDecimal(quantity)) {
            JOptionPane.showMessageDialog(null, "Invalid Quantity", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (new BigDecimal(quantity).compareTo(BigDecimal.ZERO) < 0) {
            JOptionPane.showMessageDialog(null, "Quantity must be greater than zero.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            String expiryDate = null;

            if (expDateCheckBox.isSelected()) {
                SelectedDate selectedDate = expDateChooser.getSelectedDate();
                LocalDate inputDate = LocalDate.of(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDay());

                if (!inputDate.isAfter(LocalDate.now())) {
                    JOptionPane.showMessageDialog(null, "Expiry date must be a future date.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                } else {
                    expiryDate = inputDate.toString();
                }
            }

            try {
                GrnItemDTO newGrnItem = new GrnItemDTO();
                newGrnItem.setStockData(selectedStockData);
                newGrnItem.setCostPrice(new BigDecimal(costPrice).setScale(2, RoundingMode.HALF_UP));
                newGrnItem.setMarkedPrice(new BigDecimal(markedPrice).setScale(2, RoundingMode.HALF_UP));
                newGrnItem.setQuantity(new BigDecimal(quantity).setScale(3, RoundingMode.HALF_UP));
                newGrnItem.setExpiryDate(expiryDate);

                for (GrnItemDTO item : grnItemList) {

                    if (item.getStockData().getBarcode().equals(newGrnItem.getStockData().getBarcode())
                            && item.getCostPrice().compareTo(newGrnItem.getCostPrice()) == 0
                            && item.getMarkedPrice().compareTo(newGrnItem.getMarkedPrice()) == 0
                            && Objects.equals(item.getExpiryDate(), newGrnItem.getExpiryDate())) {
                        // Duplicate found
                        item.setQuantity(item.getQuantity().add(newGrnItem.getQuantity()).setScale(3, RoundingMode.HALF_UP));
                        loadGrnItemTable();
                        cleanGrnItemInputs();
                        return;
                    }

                }

                grnItemList.add(newGrnItem);

                loadGrnItemTable();
                cleanGrnItemInputs();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "An unexpected error has occurred. Please try again later or contact support if the issue persists.", "Unexpected Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

    }

    private void loadGrnItemTable() {
        DefaultTableModel model = (DefaultTableModel) grnItemsTable.getModel();
        model.setRowCount(0);

        int rowNum = 1;

        for (GrnItemDTO item : grnItemList) {

            Vector v = new Vector();
            v.add(rowNum++);
            v.add(item.getStockData().getBarcode());
            v.add(item.getStockData().getProductName());
            v.add(BigDecimalFormatter.formatPrice(item.getCostPrice()));
            v.add(BigDecimalFormatter.formatPrice(item.getMarkedPrice()));
            v.add(BigDecimalFormatter.formatQuantity(item.getQuantity()) + " " + item.getStockData().getMeasUnitShortForm());
            v.add(item.getExpiryDate() != null ? item.getExpiryDate() : "-");
            v.add(BigDecimalFormatter.formatPrice(item.getLineTotal()));
            model.addRow(v);
        }

        calculateTotals();
    }

    private void calculateTotals() {
        BigDecimal subTotal = BigDecimal.ZERO;
        BigDecimal discount = BigDecimal.ZERO;
        BigDecimal tax = BigDecimal.ZERO;

        // ---------- Sub Total ----------
        for (GrnItemDTO item : grnItemList) {
            subTotal = subTotal.add(item.getLineTotal());
        }

        // ---------- Discount ----------
        if (discountType == DiscountType.PERCENTAGE) {
            discount = subTotal
                    .multiply(discountValue)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            discountTextLabel.setText(discountValue + "% Discount :");

        } else if (discountType == DiscountType.AMOUNT) {
            discount = discountValue;
            discountTextLabel.setText("Discount :");
        } else {
            discountTextLabel.setText("Discount :");
        }

        if (discount.compareTo(subTotal) > 0) {
            discount = subTotal;
        }

        // ---------- Net Total ----------
        BigDecimal netTotal = subTotal.subtract(discount);

        // ---------- Tax ----------
        tax = netTotal
                .multiply(taxPercentage)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        if (tax.compareTo(BigDecimal.ZERO) != 0) {
            taxTextLabel.setText(taxPercentage + "% Tax :");
        } else {
            taxTextLabel.setText("Tax :");
        }

        // ---------- GRN Total ----------
        BigDecimal grnTotal = netTotal.add(tax);

        totalValueLabel.setText(BigDecimalFormatter.formatPrice(subTotal));
        discountValueLabel.setText("- " + BigDecimalFormatter.formatPrice(discount));
        netTotalValueLabel.setText(BigDecimalFormatter.formatPrice(netTotal));
        taxValueLabel.setText(BigDecimalFormatter.formatPrice(tax));
        grnTotalValueLabel.setText(BigDecimalFormatter.formatPrice(grnTotal));

        totalsDTO.setSubTotal(subTotal);
        totalsDTO.setDiscount(discount);
        totalsDTO.setNetTotal(netTotal);
        totalsDTO.setTax(tax);
        totalsDTO.setGrnTotal(grnTotal);
    }

    private void changeDiscountLabel() {
        if (presentageDiscountRadioButton.isSelected()) {
            grnDiscountLabel.setText("Discount (%)");
        } else if (amountDiscountRadioButton.isSelected()) {
            grnDiscountLabel.setText("Discount (Rs.)");
        } else {
            grnDiscountLabel.setText("Discount");
        }
    }

    private void setDiscount() {
        String discountText = grnDiscountFromattedTextField.getText().replace("%", "").replace(",", "").trim();

        if (discountText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a discount value.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        BigDecimal value;

        try {
            value = new BigDecimal(discountText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid discount value.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Handle discount type
        if (presentageDiscountRadioButton.isSelected()) {
            if (value.compareTo(BigDecimal.ZERO) <= 0 || value.compareTo(new BigDecimal("100")) > 0) {
                JOptionPane.showMessageDialog(this, "Percentage discount must be greater than 0 and not exceed 100.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            discountType = DiscountType.PERCENTAGE;
            discountValue = value;

        } else if (amountDiscountRadioButton.isSelected()) {
            if (value.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "Amount discount must be greater than 0.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            discountType = DiscountType.AMOUNT;
            discountValue = value;
        } else {
            JOptionPane.showMessageDialog(this, "Please select a discount type.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        calculateTotals();

    }

    private void setTax() {
        String taxText = taxFromattedTextField.getText().replace("%", "").replace(",", "").trim();

        if (taxText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a tax percentage.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        BigDecimal value;

        try {
            value = new BigDecimal(taxText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid tax percentage.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            JOptionPane.showMessageDialog(this, "Tax percentage must be greater than 0", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        taxPercentage = value;
        calculateTotals();

    }

    private void clearDiscount() {
        discountType = null;
        discountValue = BigDecimal.ZERO;

        grnDiscountFromattedTextField.setValue(BigDecimal.ZERO);
        discountButtonGroup.clearSelection();

        calculateTotals();

        grnDiscountFromattedTextField.grabFocus();
        grnDiscountFromattedTextField.selectAll();
    }

    private void clearTax() {
        taxPercentage = BigDecimal.ZERO;
        taxFromattedTextField.setValue(BigDecimal.ZERO);
        calculateTotals();

        taxFromattedTextField.grabFocus();
        taxFromattedTextField.selectAll();

    }

    private void removeSelectedItem() {
        int selectedRow = grnItemsTable.getSelectedRow();

        if (selectedRow != -1) {
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to remove selected row",
                    "Clear Items",
                    JOptionPane.YES_NO_OPTION
            );
            if (result == JOptionPane.YES_OPTION) {
                grnItemList.remove(selectedRow);
                loadGrnItemTable();
                ToastUtils.showBottomToast(this, "Item Removed", 1500);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please Select a Row Remove", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void removeAllItems() {
        int rowCount = grnItemList.size();
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
                grnItemList = new ArrayList<>();
                loadGrnItemTable();
                ToastUtils.showBottomToast(this, "Items Removed", 1500);
            }

        }
    }

    private void saveGRN() {

        String date = dateChooseTextField.getText();
        String time = timePickerTextField.getText() + ":00";

        boolean isPastDateTime = true;

        if (!currentDateTimeCheckBox.isSelected()) {
            try {
                Date dateTime = Numbers.getDateObject(date + " " + time);
                isPastDateTime = new Date().after(dateTime);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Something Went Wrong in Selected Date Time", "Something Went Wrong", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        if (supplierId == null) {
            JOptionPane.showMessageDialog(this, "Please Select Supplier", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!isPastDateTime) {
            JOptionPane.showMessageDialog(this, "Selected Date and Time cannot be in the future.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (grnItemList.size() < 1) {
            JOptionPane.showMessageDialog(this, "No Items in the GRN", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            try {
                //Generate GRN Barcode
                String grnBarcode = IdGenerater.generateId("grn", "barcode", "GRN");

                //Note
                String note = noteTextField.getText();

                if (note.isEmpty() || note.isBlank()) {
                    note = null;
                }

                //Set Date and Time
                Timestamp timestamp;
                if (currentDateTimeCheckBox.isSelected()) {
                    timestamp = new Timestamp(System.currentTimeMillis());
                } else {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date parsedDate = format.parse(date + " " + time);
                    timestamp = new Timestamp(parsedDate.getTime());
                }

                MySQL.beginTransaction();

                //Instet to GRN Table 
                MySQL.executeUpdate("INSERT INTO grn (barcode, supplier_id, note, date_time, sub_total, discount, tax, item_count) "
                        + "VALUES (?,?,?,?,?,?,?,?)",
                        grnBarcode,
                        this.supplierId,
                        note,
                        timestamp,
                        totalsDTO.getSubTotal(),
                        totalsDTO.getDiscount(),
                        totalsDTO.getTax(),
                        grnItemList.size()
                );

                // GRN Items
                for (GrnItemDTO item : grnItemList) {

                    int batchId = 0;

                    ResultSet resultSet = MySQL.executeQuery("SELECT id FROM batch WHERE "
                            + "stock_barcode=? AND "
                            + "cost_price=? AND "
                            + "marked_price=? AND "
                            + "((expiry_date = ?) OR (expiry_date IS NULL AND ? IS NULL))",
                            item.getStockData().getBarcode(),
                            item.getCostPrice(),
                            item.getMarkedPrice(),
                            item.getExpiryDate(),
                            item.getExpiryDate()
                    );

                    if (resultSet.next()) {
                        //Batch Already Have
                        MySQL.executeUpdate("UPDATE batch SET quantity = quantity + ? WHERE id = ?",
                                item.getQuantity(),
                                resultSet.getString("id")
                        );

                        batchId = resultSet.getInt("id");

                    } else {
                        //New Batch
                        PreparedStatement ps = MySQL.prepare("INSERT INTO batch (stock_barcode, cost_price, marked_price, quantity, expiry_date) "
                                + "VALUES (?,?,?,?,?)");

                        MySQL.setParameters(ps,
                                item.getStockData().getBarcode(),
                                item.getCostPrice(),
                                item.getMarkedPrice(),
                                item.getQuantity(),
                                item.getExpiryDate()
                        );

                        ps.executeUpdate();
                        ResultSet batchKeysRs = ps.getGeneratedKeys();

                        if (batchKeysRs.next()) {
                            batchId = batchKeysRs.getInt(1);
                        }

                    }

                    if (batchId == 0) {
                        throw new SQLException("Failed to create or find batch");
                    }

                    // Add To GRN Items
                    MySQL.executeUpdate("INSERT INTO grn_item (grn_barcode, batch_id, quantity) "
                            + "VALUES (?,?,?)",
                            grnBarcode,
                            batchId,
                            item.getQuantity()
                    );

                }

                MySQL.commit();
                grnManagement.loadGRNTable();

                ToastUtils.showCenterToast(this, "GRN Saved Successfully", 1200);

                javax.swing.Timer timer = new javax.swing.Timer(1200, e -> {
                    this.dispose();
                    new GRNPayment(grnBarcode);
                });

                timer.setRepeats(false);
                timer.start();

                //JOptionPane.showMessageDialog(this, "GRN Saved Successfully", "Success", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/resource/success.png")));
            } catch (Exception e) {
                MySQL.rollback();
                JOptionPane.showMessageDialog(this, "Something Went Wrong", "Unexpected Error", JOptionPane.ERROR_MESSAGE);
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

        dateChooser = new com.raven.datechooser.DateChooser();
        discountButtonGroup = new javax.swing.ButtonGroup();
        timePicker = new com.raven.swing.TimePicker();
        jMenu1 = new javax.swing.JMenu();
        expDateChooser = new com.raven.datechooser.DateChooser();
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
        costPriceFormattedTextField = new javax.swing.JFormattedTextField();
        jPanel19 = new javax.swing.JPanel();
        markedPriceLabel = new javax.swing.JLabel();
        markedPriceFormattedTextField = new javax.swing.JFormattedTextField();
        jPanel33 = new javax.swing.JPanel();
        quantityLabel = new javax.swing.JLabel();
        quantityFormattedTextField = new javax.swing.JFormattedTextField();
        jPanel34 = new javax.swing.JPanel();
        expDateChooseTextField = new javax.swing.JTextField();
        setExpDateButton = new javax.swing.JButton();
        expDateCheckBox = new javax.swing.JCheckBox();
        jPanel27 = new javax.swing.JPanel();
        addRowButton = new javax.swing.JButton();
        clearRowButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        grnDiscountLabel = new javax.swing.JLabel();
        setDiscountButton = new javax.swing.JButton();
        amountDiscountRadioButton = new javax.swing.JRadioButton();
        presentageDiscountRadioButton = new javax.swing.JRadioButton();
        grnDiscountFromattedTextField = new javax.swing.JFormattedTextField();
        clearDiscountButton = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        totalValueLabel = new javax.swing.JLabel();
        discountValueLabel = new javax.swing.JLabel();
        netTotalValueLabel = new javax.swing.JLabel();
        netTotalTextLabel = new javax.swing.JLabel();
        discountTextLabel = new javax.swing.JLabel();
        totalTextLabel = new javax.swing.JLabel();
        saveButton = new javax.swing.JButton();
        removeAllButton = new javax.swing.JButton();
        removeSelectedButton = new javax.swing.JButton();
        taxTextLabel = new javax.swing.JLabel();
        taxValueLabel = new javax.swing.JLabel();
        grnTotalTextLabel = new javax.swing.JLabel();
        grnTotalValueLabel = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel23 = new javax.swing.JPanel();
        grnTaxLabel = new javax.swing.JLabel();
        setTaxButton = new javax.swing.JButton();
        taxFromattedTextField = new javax.swing.JFormattedTextField();
        clearTaxButton = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        grnItemsTable = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel9 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        supplierNameShowLabel = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        currentDateTimeCheckBox = new javax.swing.JCheckBox();
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

        expDateChooser.setForeground(new java.awt.Color(0, 105, 75));
        expDateChooser.setDateFormat("yyyy-MM-dd");
        expDateChooser.setTextRefernce(expDateChooseTextField);

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

        selectSupplierButton.setBackground(new java.awt.Color(102, 102, 102));
        selectSupplierButton.setForeground(new java.awt.Color(255, 255, 255));
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
        cleanSupplierButton.setEnabled(false);
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

        noteLabel.setText("Notes (Optional)");

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
                        .addGap(0, 27, Short.MAX_VALUE)))
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
        addStockButton.setToolTipText("Select Stock");
        addStockButton.setBorder(null);
        addStockButton.setEnabled(false);
        addStockButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStockButtonActionPerformed(evt);
            }
        });

        stockBarcodeTextField.setEnabled(false);
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

        jPanel13.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 1, new java.awt.Color(204, 204, 204)));
        jPanel13.setForeground(new java.awt.Color(255, 51, 51));

        productLabel.setText("Product");
        productLabel.setEnabled(false);

        productNameTextField.setEditable(false);
        productNameTextField.setEnabled(false);

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
                        .addGap(0, 84, Short.MAX_VALUE)))
                .addGap(12, 12, 12))
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

        buyingPriceLabel.setText("Cost Price (Rs.)");
        buyingPriceLabel.setEnabled(false);

        costPriceFormattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.##"))));
        costPriceFormattedTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                costPriceFormattedTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(costPriceFormattedTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(buyingPriceLabel)
                        .addGap(0, 48, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buyingPriceLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(costPriceFormattedTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel11.add(jPanel14);

        jPanel19.setForeground(new java.awt.Color(255, 51, 51));

        markedPriceLabel.setText("Marked Price (Rs.)");
        markedPriceLabel.setEnabled(false);

        markedPriceFormattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.##"))));
        markedPriceFormattedTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                markedPriceFormattedTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(markedPriceFormattedTextField)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(markedPriceLabel)
                        .addGap(0, 38, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(markedPriceLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(markedPriceFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel11.add(jPanel19);

        jPanel33.setForeground(new java.awt.Color(255, 51, 51));

        quantityLabel.setText("Quantity");
        quantityLabel.setEnabled(false);

        quantityFormattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.###"))));
        quantityFormattedTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quantityFormattedTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(quantityFormattedTextField)
                    .addGroup(jPanel33Layout.createSequentialGroup()
                        .addComponent(quantityLabel)
                        .addGap(0, 87, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(quantityLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(quantityFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel11.add(jPanel33);

        jPanel34.setForeground(new java.awt.Color(255, 51, 51));

        expDateChooseTextField.setEnabled(false);

        setExpDateButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/calendar.png"))); // NOI18N
        setExpDateButton.setEnabled(false);
        setExpDateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setExpDateButtonActionPerformed(evt);
            }
        });

        expDateCheckBox.setText("Exp Date");
        expDateCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                expDateCheckBoxItemStateChanged(evt);
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
                        .addComponent(expDateChooseTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(setExpDateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addComponent(expDateCheckBox)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(expDateCheckBox)
                .addGap(2, 2, 2)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(expDateChooseTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(setExpDateButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel11.add(jPanel34);

        jPanel27.setForeground(new java.awt.Color(255, 51, 51));

        addRowButton.setText("Add Item");
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
        grnDiscountLabel.setText("Discount");
        grnDiscountLabel.setEnabled(false);

        setDiscountButton.setText("Set");
        setDiscountButton.setToolTipText("Set Discount");
        setDiscountButton.setBorder(null);
        setDiscountButton.setEnabled(false);
        setDiscountButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setDiscountButtonActionPerformed(evt);
            }
        });

        discountButtonGroup.add(amountDiscountRadioButton);
        amountDiscountRadioButton.setText("Amount (Rs.)");
        amountDiscountRadioButton.setEnabled(false);
        amountDiscountRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                amountDiscountRadioButtonItemStateChanged(evt);
            }
        });

        discountButtonGroup.add(presentageDiscountRadioButton);
        presentageDiscountRadioButton.setText("Presentage (%)");
        presentageDiscountRadioButton.setEnabled(false);
        presentageDiscountRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                presentageDiscountRadioButtonItemStateChanged(evt);
            }
        });

        grnDiscountFromattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.##"))));
        grnDiscountFromattedTextField.setText("0");
        grnDiscountFromattedTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grnDiscountFromattedTextFieldActionPerformed(evt);
            }
        });

        clearDiscountButton.setBackground(new java.awt.Color(102, 102, 102));
        clearDiscountButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/clean.png"))); // NOI18N
        clearDiscountButton.setToolTipText("Clear Supplier");
        clearDiscountButton.setBorder(null);
        clearDiscountButton.setEnabled(false);
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
                        .addComponent(presentageDiscountRadioButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(amountDiscountRadioButton)
                        .addGap(12, 12, 12))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(grnDiscountLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(grnDiscountFromattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(setDiscountButton, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(clearDiscountButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(grnDiscountLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(grnDiscountFromattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(setDiscountButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(clearDiscountButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(presentageDiscountRadioButton)
                    .addComponent(amountDiscountRadioButton))
                .addContainerGap())
        );

        totalValueLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        totalValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalValueLabel.setText("0.00");
        totalValueLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));
        totalValueLabel.setEnabled(false);

        discountValueLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        discountValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        discountValueLabel.setText("- 0.00");
        discountValueLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));
        discountValueLabel.setEnabled(false);

        netTotalValueLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        netTotalValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        netTotalValueLabel.setText("0.00");
        netTotalValueLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));
        netTotalValueLabel.setEnabled(false);

        netTotalTextLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        netTotalTextLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        netTotalTextLabel.setText("Net Total :");
        netTotalTextLabel.setEnabled(false);

        discountTextLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        discountTextLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        discountTextLabel.setText("Discount :");
        discountTextLabel.setEnabled(false);

        totalTextLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        totalTextLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalTextLabel.setText("Sub Total :");
        totalTextLabel.setEnabled(false);

        saveButton.setText("Save GRN");
        saveButton.setToolTipText("Add New Product");
        saveButton.setBorder(null);
        saveButton.setEnabled(false);
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
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
        removeSelectedButton.setText("Remove");
        removeSelectedButton.setBorder(null);
        removeSelectedButton.setEnabled(false);
        removeSelectedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeSelectedButtonActionPerformed(evt);
            }
        });

        taxTextLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        taxTextLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        taxTextLabel.setText("Tax :");
        taxTextLabel.setEnabled(false);

        taxValueLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        taxValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        taxValueLabel.setText("0.00");
        taxValueLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));
        taxValueLabel.setEnabled(false);

        grnTotalTextLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        grnTotalTextLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        grnTotalTextLabel.setText("GRN Total :");
        grnTotalTextLabel.setEnabled(false);

        grnTotalValueLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        grnTotalValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        grnTotalValueLabel.setText("0.00");
        grnTotalValueLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));
        grnTotalValueLabel.setEnabled(false);

        jSeparator2.setForeground(new java.awt.Color(51, 51, 51));

        jSeparator3.setForeground(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                        .addComponent(discountTextLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(discountValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                        .addComponent(totalTextLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                        .addComponent(netTotalTextLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(netTotalValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(taxTextLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(taxValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(grnTotalTextLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(grnTotalValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                                .addComponent(removeSelectedButton, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(removeAllButton, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)))))
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
                    .addComponent(discountTextLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(discountValueLabel))
                .addGap(0, 0, 0)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(netTotalTextLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(netTotalValueLabel))
                .addGap(0, 0, 0)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(taxTextLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(taxValueLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(grnTotalTextLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(grnTotalValueLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(saveButton, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(removeAllButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(removeSelectedButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        grnTaxLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        grnTaxLabel.setText("Tax Percentage (%)");
        grnTaxLabel.setEnabled(false);

        setTaxButton.setText("Set");
        setTaxButton.setToolTipText("Set Tax Percentage");
        setTaxButton.setBorder(null);
        setTaxButton.setEnabled(false);
        setTaxButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setTaxButtonActionPerformed(evt);
            }
        });

        taxFromattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.##"))));
        taxFromattedTextField.setText("0");
        taxFromattedTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                taxFromattedTextFieldActionPerformed(evt);
            }
        });

        clearTaxButton.setBackground(new java.awt.Color(102, 102, 102));
        clearTaxButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/clean.png"))); // NOI18N
        clearTaxButton.setToolTipText("Clear Supplier");
        clearTaxButton.setBorder(null);
        clearTaxButton.setEnabled(false);
        clearTaxButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearTaxButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(grnTaxLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(taxFromattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(setTaxButton, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearTaxButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(grnTaxLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(taxFromattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(setTaxButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(clearTaxButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        grnItemsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Stock Barcode", "Product Name", "Cost Price (Rs.)", "Marked Price (Rs.)", "Quantity", "Expiry Date", "Amount (Rs.)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        grnItemsTable.setEnabled(false);
        grnItemsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
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
                    .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
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

        jSeparator5.setForeground(new java.awt.Color(204, 204, 204));

        jSeparator6.setForeground(new java.awt.Color(204, 204, 204));

        jLabel3.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setText("New GRN");
        jLabel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 0));

        currentDateTimeCheckBox.setText("Use Current Date Time To GRN");
        currentDateTimeCheckBox.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        currentDateTimeCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                currentDateTimeCheckBoxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator6)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 699, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(currentDateTimeCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 6, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(290, 290, 290)
                    .addComponent(jSeparator5)
                    .addGap(290, 290, 290)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(currentDateTimeCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(336, 336, 336)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(306, Short.MAX_VALUE)))
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
        grnItemsTable.clearSelection();
        cleanGrnItemInputs();
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

    private void removeAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeAllButtonActionPerformed
        removeAllItems();
    }//GEN-LAST:event_removeAllButtonActionPerformed

    private void setDateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setDateButtonActionPerformed
        dateChooser.showPopup();
    }//GEN-LAST:event_setDateButtonActionPerformed

    private void setTimeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setTimeButtonActionPerformed
        timePicker.showPopup(timePickerTextField, 0, 37);
    }//GEN-LAST:event_setTimeButtonActionPerformed

    private void currentDateTimeCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_currentDateTimeCheckBoxItemStateChanged
        useCurrentDateTimeToggle();
    }//GEN-LAST:event_currentDateTimeCheckBoxItemStateChanged

    private void editDetailsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editDetailsButtonActionPerformed
        confirmedGRN(false);
    }//GEN-LAST:event_editDetailsButtonActionPerformed

    private void grnDiscountFromattedTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grnDiscountFromattedTextFieldActionPerformed
        setDiscount();
    }//GEN-LAST:event_grnDiscountFromattedTextFieldActionPerformed

    private void setDiscountButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setDiscountButtonActionPerformed
        setDiscount();
    }//GEN-LAST:event_setDiscountButtonActionPerformed

    private void presentageDiscountRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_presentageDiscountRadioButtonItemStateChanged
        changeDiscountLabel();
    }//GEN-LAST:event_presentageDiscountRadioButtonItemStateChanged

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed

        int option = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to save this GRN?\nStock quantities will be updated.",
                "Confirm GRN Save",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (option == JOptionPane.YES_OPTION) {
            saveButton.setEnabled(false);
            saveGRN();
        } else {
            saveButton.setEnabled(true);
        }

    }//GEN-LAST:event_saveButtonActionPerformed

    private void removeSelectedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeSelectedButtonActionPerformed
        removeSelectedItem();
    }//GEN-LAST:event_removeSelectedButtonActionPerformed

    private void grnItemsTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_grnItemsTableKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_grnItemsTableKeyReleased

    private void clearRowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearRowButtonActionPerformed
        cleanGrnItemInputs();
    }//GEN-LAST:event_clearRowButtonActionPerformed

    private void addRowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addRowButtonActionPerformed
        addGrnItem();
    }//GEN-LAST:event_addRowButtonActionPerformed

    private void selectSupplierButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectSupplierButtonActionPerformed
        openSelectSupplier();
    }//GEN-LAST:event_selectSupplierButtonActionPerformed

    private void costPriceFormattedTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_costPriceFormattedTextFieldActionPerformed
        markedPriceFormattedTextField.grabFocus();
    }//GEN-LAST:event_costPriceFormattedTextFieldActionPerformed

    private void markedPriceFormattedTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_markedPriceFormattedTextFieldActionPerformed
        quantityFormattedTextField.grabFocus();
    }//GEN-LAST:event_markedPriceFormattedTextFieldActionPerformed

    private void quantityFormattedTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quantityFormattedTextFieldActionPerformed
        addRowButton.grabFocus();
    }//GEN-LAST:event_quantityFormattedTextFieldActionPerformed

    private void setExpDateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setExpDateButtonActionPerformed
        expDateChooser.showPopup();
    }//GEN-LAST:event_setExpDateButtonActionPerformed

    private void expDateCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_expDateCheckBoxItemStateChanged
        if (expDateCheckBox.isSelected()) {
            expDateChooser.toDay();
            expDateChooseTextField.setEnabled(true);
            setExpDateButton.setEnabled(true);
        } else {

            expDateChooseTextField.setText("No EXP Date");
            expDateChooseTextField.setEnabled(false);
            setExpDateButton.setEnabled(false);

        }
    }//GEN-LAST:event_expDateCheckBoxItemStateChanged

    private void clearDiscountButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearDiscountButtonActionPerformed
        clearDiscount();
    }//GEN-LAST:event_clearDiscountButtonActionPerformed

    private void setTaxButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setTaxButtonActionPerformed
        setTax();
    }//GEN-LAST:event_setTaxButtonActionPerformed

    private void taxFromattedTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_taxFromattedTextFieldActionPerformed
        setTax();
    }//GEN-LAST:event_taxFromattedTextFieldActionPerformed

    private void clearTaxButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearTaxButtonActionPerformed
        clearTax();
    }//GEN-LAST:event_clearTaxButtonActionPerformed

    private void amountDiscountRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_amountDiscountRadioButtonItemStateChanged
        changeDiscountLabel();
    }//GEN-LAST:event_amountDiscountRadioButtonItemStateChanged

    private void supplierNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierNameTextFieldActionPerformed
        supplierPhoneTextField.grabFocus();
    }//GEN-LAST:event_supplierNameTextFieldActionPerformed

    private void supplierPhoneTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierPhoneTextFieldActionPerformed
        noteTextField.grabFocus();
    }//GEN-LAST:event_supplierPhoneTextFieldActionPerformed

    private void cleanSupplierButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cleanSupplierButtonActionPerformed
        clearSupplier();
    }//GEN-LAST:event_cleanSupplierButtonActionPerformed

    private void noteTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noteTextFieldActionPerformed
        if (currentDateTimeCheckBox.isSelected()) {
            confirmGrnButton.doClick();
        }
    }//GEN-LAST:event_noteTextFieldActionPerformed

    private void confirmGrnButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmGrnButtonActionPerformed
        String date = dateChooseTextField.getText();
        String time = timePickerTextField.getText() + ":00";

        boolean isPastDateTime = true;

        if (!currentDateTimeCheckBox.isSelected()) {
            try {
                Date dateTime = Numbers.getDateObject(date + " " + time + ":00");
                isPastDateTime = new Date().after(dateTime);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Something Went Wrong in Selected Date Time", "Something Went Wrong", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        if (supplierId == null) {
            JOptionPane.showMessageDialog(this, "Please Select Supplier", "Warning", JOptionPane.WARNING_MESSAGE);
            supplierNameTextField.grabFocus();
        } else if (!isPastDateTime) {
            JOptionPane.showMessageDialog(this, "Selected Date and Time cannot be in the future.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            confirmedGRN(true);
            stockBarcodeTextField.grabFocus();
        }
    }//GEN-LAST:event_confirmGrnButtonActionPerformed

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
    private javax.swing.JLabel buyingPriceLabel;
    private javax.swing.JButton cleanSupplierButton;
    private javax.swing.JButton clearDiscountButton;
    private javax.swing.JButton clearRowButton;
    private javax.swing.JButton clearTaxButton;
    private javax.swing.JButton confirmGrnButton;
    private javax.swing.JFormattedTextField costPriceFormattedTextField;
    private javax.swing.JCheckBox currentDateTimeCheckBox;
    private javax.swing.JTextField dateChooseTextField;
    private com.raven.datechooser.DateChooser dateChooser;
    private javax.swing.JLabel dateLabel;
    private javax.swing.ButtonGroup discountButtonGroup;
    private javax.swing.JLabel discountTextLabel;
    private javax.swing.JLabel discountValueLabel;
    private javax.swing.JButton editDetailsButton;
    private javax.swing.JCheckBox expDateCheckBox;
    private javax.swing.JTextField expDateChooseTextField;
    private com.raven.datechooser.DateChooser expDateChooser;
    private javax.swing.JFormattedTextField grnDiscountFromattedTextField;
    private javax.swing.JLabel grnDiscountLabel;
    private javax.swing.JTable grnItemsTable;
    private javax.swing.JLabel grnTaxLabel;
    private javax.swing.JLabel grnTotalTextLabel;
    private javax.swing.JLabel grnTotalValueLabel;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
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
    private javax.swing.JPanel jPanel23;
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
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JFormattedTextField markedPriceFormattedTextField;
    private javax.swing.JLabel markedPriceLabel;
    private javax.swing.JLabel netTotalTextLabel;
    private javax.swing.JLabel netTotalValueLabel;
    private javax.swing.JLabel noteLabel;
    private javax.swing.JTextField noteTextField;
    private javax.swing.JRadioButton presentageDiscountRadioButton;
    private javax.swing.JLabel productLabel;
    private javax.swing.JTextField productNameTextField;
    private javax.swing.JFormattedTextField quantityFormattedTextField;
    private javax.swing.JLabel quantityLabel;
    private javax.swing.JButton removeAllButton;
    private javax.swing.JButton removeSelectedButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton selectSupplierButton;
    private javax.swing.JButton setDateButton;
    private javax.swing.JButton setDiscountButton;
    private javax.swing.JButton setExpDateButton;
    private javax.swing.JButton setTaxButton;
    private javax.swing.JButton setTimeButton;
    private javax.swing.JLabel stockBarcodeLabel;
    private javax.swing.JTextField stockBarcodeTextField;
    private javax.swing.JLabel supplierNameLabel;
    private javax.swing.JLabel supplierNameShowLabel;
    private javax.swing.JTextField supplierNameTextField;
    private javax.swing.JLabel supplierPhoneLabel;
    private javax.swing.JTextField supplierPhoneTextField;
    private javax.swing.JFormattedTextField taxFromattedTextField;
    private javax.swing.JLabel taxTextLabel;
    private javax.swing.JLabel taxValueLabel;
    private javax.swing.JLabel timeLabel;
    private com.raven.swing.TimePicker timePicker;
    private javax.swing.JTextField timePickerTextField;
    private javax.swing.JLabel totalTextLabel;
    private javax.swing.JLabel totalValueLabel;
    // End of variables declaration//GEN-END:variables
}
