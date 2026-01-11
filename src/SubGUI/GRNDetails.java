/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package SubGUI;

import DTO.GrnItemDTO;
import java.awt.Toolkit;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.BigDecimalFormatter;

import model.MySQL;
import panels.GrnManagement;
import service.PaymentStatusService;

/**
 *
 * @author Sandeep
 */
public class GRNDetails extends javax.swing.JFrame {

    //Inisilize Varables
    private HashMap<String, Integer> rowNumberMap = new HashMap<String, Integer>();
    private List<GrnItemDTO> grnItemList = new ArrayList<>();

    private String grnBarcode;

    private GrnManagement grnManagement;

    public GRNDetails(String grnBarcode, GrnManagement grnManagement) {

        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resource/icon.png")));
        initComponents();
        this.grnBarcode = grnBarcode;
        this.grnManagement = grnManagement;

        // Table Alighment
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

        grnItemsTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        grnItemsTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        grnItemsTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        grnItemsTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        grnItemsTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        grnItemsTable.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
        loadData();
        this.setVisible(true);
    }

    private void loadData() {

        try {
            ResultSet resultSet = MySQL.executeQuery("SELECT * FROM grn g "
                    + "INNER JOIN supplier s ON g.supplier_id = s.id "
                    + "WHERE barcode=?",
                    grnBarcode
            );

            if (resultSet.next()) {

                topGrnIdLabel.setText(resultSet.getString("g.barcode"));

                supplierNameLabel.setText(resultSet.getString("s.name"));
                dateTimeLabel.setText(resultSet.getString("g.date_time"));

                BigDecimal grnTotal = resultSet.getBigDecimal("g.grn_total");
                BigDecimal paidAmount = resultSet.getBigDecimal("g.paid_amount");

                paymentStatusLabel.setText(PaymentStatusService.getPaymentStatus(grnTotal, paidAmount));
                balanceDueTopLabel.setText("Rs. " + BigDecimalFormatter.formatPrice(resultSet.getBigDecimal("balance")));

                subTotalValueLabel.setText(BigDecimalFormatter.formatPrice(resultSet.getBigDecimal("sub_total")));
                discountValueLabel.setText("- " + BigDecimalFormatter.formatPrice(resultSet.getBigDecimal("discount")));
                netTotalValueLabel.setText(BigDecimalFormatter.formatPrice(resultSet.getBigDecimal("net_total")));
                taxValueLabel.setText(BigDecimalFormatter.formatPrice(resultSet.getBigDecimal("tax")));
                grnTotalValueLabel.setText(BigDecimalFormatter.formatPrice(resultSet.getBigDecimal("grn_total")));
                paidValueLabel.setText(BigDecimalFormatter.formatPrice(resultSet.getBigDecimal("paid_amount")));
                balanceValueLabel.setText(BigDecimalFormatter.formatPrice(resultSet.getBigDecimal("balance")));

                if (resultSet.getBigDecimal("balance").compareTo(BigDecimal.ZERO) <= 0) {
                    proceedToPaymentButton.setEnabled(false);
                } else {
                    proceedToPaymentButton.setEnabled(true);
                }

                loadGrnItemsTable();
                this.setVisible(true);

            } else {
                JOptionPane.showMessageDialog(this, "Invalid GRN Barcode", "Unexpected Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An unexpected error has occurred. Please try again later or contact support if the issue persists.", "Unexpected Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }

    public void loadGrnItemsTable() {
        try {

            DefaultTableModel model = (DefaultTableModel) grnItemsTable.getModel();
            model.setRowCount(0);

            ResultSet resultSet = MySQL.executeQuery("SELECT * FROM grn_item gi "
                    + "INNER JOIN batch b ON gi.batch_id = b.id "
                    + "INNER JOIN stock s ON b.stock_barcode = s.barcode "
                    + "INNER JOIN product p ON s.product_id = p.id "
                    + "WHERE gi.grn_barcode = ? "
                    + "ORDER BY gi.id ASC",
                    grnBarcode
            );

            int rowNo = 1;

            while (resultSet.next()) {
                Vector v = new Vector();
                v.add(rowNo++);
                v.add(resultSet.getString("s.barcode"));
                v.add(resultSet.getString("p.name"));
                v.add(BigDecimalFormatter.formatPrice(resultSet.getBigDecimal("b.cost_price")));
                v.add(BigDecimalFormatter.formatQuantity(resultSet.getBigDecimal("gi.quantity")));
                v.add(BigDecimalFormatter.formatQuantity(resultSet.getBigDecimal("b.cost_price").multiply(resultSet.getBigDecimal("gi.quantity"))));
                model.addRow(v);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An unexpected error has occurred. Please try again later or contact support if the issue persists.", "Unexpected Error", JOptionPane.ERROR_MESSAGE);
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

        dateChooser = new com.raven.datechooser.DateChooser();
        discountButtonGroup = new javax.swing.ButtonGroup();
        timePicker = new com.raven.swing.TimePicker();
        jMenu1 = new javax.swing.JMenu();
        expDateChooser = new com.raven.datechooser.DateChooser();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        invoiceDataLabel4 = new javax.swing.JLabel();
        supplierNameLabel = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        invoiceDataLabel7 = new javax.swing.JLabel();
        dateTimeLabel = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        invoiceDataLabel10 = new javax.swing.JLabel();
        grnIdLabel = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        invoiceDataLabel14 = new javax.swing.JLabel();
        paymentStatusLabel = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        invoiceDataLabel12 = new javax.swing.JLabel();
        balanceDueTopLabel = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        subTotalValueLabel = new javax.swing.JLabel();
        discountValueLabel = new javax.swing.JLabel();
        netTotalValueLabel = new javax.swing.JLabel();
        netTotalTextLabel = new javax.swing.JLabel();
        discountTextLabel = new javax.swing.JLabel();
        totalTextLabel = new javax.swing.JLabel();
        taxTextLabel = new javax.swing.JLabel();
        taxValueLabel = new javax.swing.JLabel();
        grnTotalTextLabel = new javax.swing.JLabel();
        grnTotalValueLabel = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        grnTotalTextLabel1 = new javax.swing.JLabel();
        paidValueLabel = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        grnTotalTextLabel2 = new javax.swing.JLabel();
        balanceValueLabel = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        removeSelectedButton = new javax.swing.JButton();
        proceedToPaymentButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        grnItemsTable = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel9 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        topGrnIdLabel = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();

        dateChooser.setForeground(new java.awt.Color(0, 105, 75));
        dateChooser.setDateFormat("yyyy-MM-dd");

        timePicker.setForeground(new java.awt.Color(0, 105, 75));
        timePicker.set24hourMode(true);

        jMenu1.setText("jMenu1");

        expDateChooser.setForeground(new java.awt.Color(0, 105, 75));
        expDateChooser.setDateFormat("yyyy-MM-dd");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Good Received Note");
        setMinimumSize(new java.awt.Dimension(1090, 696));

        jPanel3.setLayout(new java.awt.GridLayout(1, 0, 10, 0));

        invoiceDataLabel4.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        invoiceDataLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        invoiceDataLabel4.setText("Supplier");

        supplierNameLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        supplierNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        supplierNameLabel.setText("Sandeep Kavinda");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(invoiceDataLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(supplierNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(invoiceDataLabel4)
                .addGap(0, 0, 0)
                .addComponent(supplierNameLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel6);

        invoiceDataLabel7.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        invoiceDataLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        invoiceDataLabel7.setText("GRN Date Time");

        dateTimeLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        dateTimeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dateTimeLabel.setText("2025-12-10");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(invoiceDataLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                    .addComponent(dateTimeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(invoiceDataLabel7)
                .addGap(0, 0, 0)
                .addComponent(dateTimeLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel7);

        invoiceDataLabel10.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        invoiceDataLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        invoiceDataLabel10.setText("GRN ID");

        grnIdLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        grnIdLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        grnIdLabel.setText("GRN0000012");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(invoiceDataLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(grnIdLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(invoiceDataLabel10)
                .addGap(0, 0, 0)
                .addComponent(grnIdLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel10);

        invoiceDataLabel14.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        invoiceDataLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        invoiceDataLabel14.setText("Payment Status");

        paymentStatusLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        paymentStatusLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        paymentStatusLabel.setText("Paid");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(invoiceDataLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                    .addComponent(paymentStatusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(invoiceDataLabel14)
                .addGap(0, 0, 0)
                .addComponent(paymentStatusLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel12);

        invoiceDataLabel12.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        invoiceDataLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        invoiceDataLabel12.setText("Due Payment");

        balanceDueTopLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        balanceDueTopLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        balanceDueTopLabel.setText("Rs. 25000.00");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(invoiceDataLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                    .addComponent(balanceDueTopLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(invoiceDataLabel12)
                .addGap(0, 0, 0)
                .addComponent(balanceDueTopLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel11);

        subTotalValueLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        subTotalValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        subTotalValueLabel.setText("0.00");
        subTotalValueLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));

        discountValueLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        discountValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        discountValueLabel.setText("- 0.00");
        discountValueLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));

        netTotalValueLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        netTotalValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        netTotalValueLabel.setText("0.00");
        netTotalValueLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));

        netTotalTextLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        netTotalTextLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        netTotalTextLabel.setText("Net Total :");

        discountTextLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        discountTextLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        discountTextLabel.setText("Discount :");

        totalTextLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        totalTextLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalTextLabel.setText("Sub Total :");

        taxTextLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        taxTextLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        taxTextLabel.setText("Tax :");

        taxValueLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        taxValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        taxValueLabel.setText("0.00");
        taxValueLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));

        grnTotalTextLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        grnTotalTextLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        grnTotalTextLabel.setText("GRN Total :");

        grnTotalValueLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        grnTotalValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        grnTotalValueLabel.setText("0.00");
        grnTotalValueLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));

        jSeparator2.setForeground(new java.awt.Color(51, 51, 51));

        jSeparator3.setForeground(new java.awt.Color(51, 51, 51));

        grnTotalTextLabel1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        grnTotalTextLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        grnTotalTextLabel1.setText("Paid Amount :");

        paidValueLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        paidValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        paidValueLabel.setText("0.00");
        paidValueLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));

        jSeparator4.setForeground(new java.awt.Color(51, 51, 51));

        grnTotalTextLabel2.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        grnTotalTextLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        grnTotalTextLabel2.setText("Due Payment :");

        balanceValueLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        balanceValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        balanceValueLabel.setText("0.00");
        balanceValueLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));

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
                        .addComponent(subTotalValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                        .addComponent(grnTotalTextLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(paidValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(grnTotalTextLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(balanceValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalTextLabel)
                    .addComponent(subTotalValueLabel))
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
                .addGap(0, 0, 0)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(grnTotalTextLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(paidValueLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(grnTotalTextLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(balanceValueLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        removeSelectedButton.setBackground(new java.awt.Color(102, 102, 102));
        removeSelectedButton.setForeground(new java.awt.Color(255, 255, 255));
        removeSelectedButton.setText("Close");
        removeSelectedButton.setBorder(null);
        removeSelectedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeSelectedButtonActionPerformed(evt);
            }
        });

        proceedToPaymentButton.setBackground(new java.awt.Color(102, 102, 102));
        proceedToPaymentButton.setForeground(new java.awt.Color(255, 255, 255));
        proceedToPaymentButton.setText("Proceed To Payment");
        proceedToPaymentButton.setBorder(null);
        proceedToPaymentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                proceedToPaymentButtonActionPerformed(evt);
            }
        });

        saveButton.setText("Print");
        saveButton.setToolTipText("Add New Product");
        saveButton.setBorder(null);
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(removeSelectedButton, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(proceedToPaymentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(saveButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(proceedToPaymentButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(removeSelectedButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        grnItemsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Stock Barcode", "Product Name", "Cost Price (Rs.)", "Quantity", "Amount (Rs.)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        grnItemsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        grnItemsTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                grnItemsTableKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(grnItemsTable);
        if (grnItemsTable.getColumnModel().getColumnCount() > 0) {
            grnItemsTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        }

        jSeparator1.setForeground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addComponent(jSeparator1))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 0, 0, 0));
        jPanel9.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 353, Short.MAX_VALUE)
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 34, Short.MAX_VALUE)
        );

        jPanel9.add(jPanel20);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 105, 75));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/logo-extra-sm.png"))); // NOI18N
        jLabel2.setIconTextGap(20);
        jPanel9.add(jLabel2);

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 353, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 34, Short.MAX_VALUE)
        );

        jPanel9.add(jPanel18);

        jSeparator5.setForeground(new java.awt.Color(204, 204, 204));

        jSeparator6.setForeground(new java.awt.Color(204, 204, 204));

        jLabel3.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel3.setText("Good Received Note");
        jLabel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));

        topGrnIdLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        topGrnIdLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        topGrnIdLabel.setText("GRN0000000");
        topGrnIdLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 30));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(topGrnIdLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jSeparator6, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(290, 290, 290)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
                    .addGap(290, 290, 290)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(topGrnIdLabel)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(336, 336, 336)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(308, Short.MAX_VALUE)))
        );

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void proceedToPaymentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_proceedToPaymentButtonActionPerformed
        this.dispose();
        new GRNPayment(grnBarcode, grnManagement);
    }//GEN-LAST:event_proceedToPaymentButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed


    }//GEN-LAST:event_saveButtonActionPerformed

    private void removeSelectedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeSelectedButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_removeSelectedButtonActionPerformed

    private void grnItemsTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_grnItemsTableKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_grnItemsTableKeyReleased

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
    private javax.swing.JLabel balanceDueTopLabel;
    private javax.swing.JLabel balanceValueLabel;
    private com.raven.datechooser.DateChooser dateChooser;
    private javax.swing.JLabel dateTimeLabel;
    private javax.swing.ButtonGroup discountButtonGroup;
    private javax.swing.JLabel discountTextLabel;
    private javax.swing.JLabel discountValueLabel;
    private com.raven.datechooser.DateChooser expDateChooser;
    private javax.swing.JLabel grnIdLabel;
    private javax.swing.JTable grnItemsTable;
    private javax.swing.JLabel grnTotalTextLabel;
    private javax.swing.JLabel grnTotalTextLabel1;
    private javax.swing.JLabel grnTotalTextLabel2;
    private javax.swing.JLabel grnTotalValueLabel;
    private javax.swing.JLabel invoiceDataLabel10;
    private javax.swing.JLabel invoiceDataLabel12;
    private javax.swing.JLabel invoiceDataLabel14;
    private javax.swing.JLabel invoiceDataLabel4;
    private javax.swing.JLabel invoiceDataLabel7;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
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
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel53;
    private javax.swing.JPanel jPanel54;
    private javax.swing.JPanel jPanel55;
    private javax.swing.JPanel jPanel56;
    private javax.swing.JPanel jPanel57;
    private javax.swing.JPanel jPanel58;
    private javax.swing.JPanel jPanel59;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel60;
    private javax.swing.JPanel jPanel61;
    private javax.swing.JPanel jPanel62;
    private javax.swing.JPanel jPanel63;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JLabel netTotalTextLabel;
    private javax.swing.JLabel netTotalValueLabel;
    private javax.swing.JLabel paidValueLabel;
    private javax.swing.JLabel paymentStatusLabel;
    private javax.swing.JButton proceedToPaymentButton;
    private javax.swing.JButton removeSelectedButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JLabel subTotalValueLabel;
    private javax.swing.JLabel supplierNameLabel;
    private javax.swing.JLabel taxTextLabel;
    private javax.swing.JLabel taxValueLabel;
    private com.raven.swing.TimePicker timePicker;
    private javax.swing.JLabel topGrnIdLabel;
    private javax.swing.JLabel totalTextLabel;
    // End of variables declaration//GEN-END:variables
}
