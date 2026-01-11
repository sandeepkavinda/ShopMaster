/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package SubGUI;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.BigDecimalFormatter;
import model.MySQL;
import panels.GrnManagement;

/**
 *
 * @author Sandeep
 */
public class GrnPaymentHistory extends javax.swing.JDialog {

    private String grnBarcode;
    private GrnManagement grnManagement;

    public GrnPaymentHistory(java.awt.Frame parent, boolean modal, String grnBarcode, GrnManagement grnManagement) {
        super(parent, modal);
        this.grnBarcode = grnBarcode;
        this.grnManagement = grnManagement;
        initComponents();

        //Table Data Alignment
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

        grnPaymentsTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        grnPaymentsTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        grnPaymentsTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        grnPaymentsTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        grnPaymentsTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        loadData();

    }

    private void loadData() {
        try {
            ResultSet resultset = MySQL.executeQuery("SELECT * FROM grn WHERE barcode=?",
                    grnBarcode
            );

            if (resultset.next()) {

                grnBarcodeLabel.setText(resultset.getString("barcode"));

                subTotalValueLabel.setText(BigDecimalFormatter.formatPrice(resultset.getBigDecimal("sub_total")));
                discountValueLabel.setText(BigDecimalFormatter.formatPrice(resultset.getBigDecimal("discount")));
                netTotalValueLabel.setText(BigDecimalFormatter.formatPrice(resultset.getBigDecimal("net_total")));
                taxValueLabel.setText(BigDecimalFormatter.formatPrice(resultset.getBigDecimal("tax")));
                grnTotalValueLabel.setText(BigDecimalFormatter.formatPrice(resultset.getBigDecimal("grn_total")));
                paidValueLabel.setText(BigDecimalFormatter.formatPrice(resultset.getBigDecimal("paid_amount")));
                balanceValueLabel.setText(BigDecimalFormatter.formatPrice(resultset.getBigDecimal("balance")));

                paidAmountSummeryLabel.setText("Paid Amount : Rs. "+BigDecimalFormatter.formatPrice(resultset.getBigDecimal("paid_amount")));
                
                if (resultset.getBigDecimal("balance").compareTo(BigDecimal.ZERO) <= 0) {

                    payButton.setText("Paid");
                    payButton.setEnabled(false);
                } else {
                    payButton.setText("Pay");
                    payButton.setEnabled(true);
                }

                loadPaymentHistoryTable();
                this.setVisible(true);

            } else {
                JOptionPane.showMessageDialog(this, "Invalid GRN Barcode", "Unexpected Error", JOptionPane.ERROR_MESSAGE);

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An unexpected error has occurred. Please try again later or contact support if the issue persists.", "Unexpected Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }

    public void loadPaymentHistoryTable() {
        try {

            DefaultTableModel model = (DefaultTableModel) grnPaymentsTable.getModel();
            model.setRowCount(0);

            ResultSet resultSet = MySQL.executeQuery("SELECT * FROM grn_payment gp "
                    + "INNER JOIN grn_payment_method gpm ON gp.grn_payment_method_id = gpm.id "
                    + "WHERE gp.grn_barcode = ? "
                    + "ORDER BY gp.date_time DESC",
                    grnBarcode
            );

            int rowNo = 1;

            while (resultSet.next()) {
                Vector v = new Vector();
                v.add(rowNo++);
                v.add(BigDecimalFormatter.formatPrice(resultSet.getBigDecimal("gp.paid_amount")));
                v.add(resultSet.getString("gpm.name"));
                v.add(resultSet.getString("gp.reference_no") == null ? "-" : resultSet.getString("gp.reference_no"));
                v.add(resultSet.getString("gp.date_time"));
                model.addRow(v);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An unexpected error has occurred. Please try again later or contact support if the issue persists.", "Unexpected Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void viewGrnPayment() {
        if (grnManagement != null) {
            this.dispose();
            new GRNPayment(grnBarcode, grnManagement);
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        totalTextLabel = new javax.swing.JLabel();
        discountTextLabel = new javax.swing.JLabel();
        netTotalTextLabel = new javax.swing.JLabel();
        taxTextLabel = new javax.swing.JLabel();
        subTotalValueLabel = new javax.swing.JLabel();
        discountValueLabel = new javax.swing.JLabel();
        netTotalValueLabel = new javax.swing.JLabel();
        taxValueLabel = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        grnTotalValueLabel = new javax.swing.JLabel();
        grnTotalTextLabel = new javax.swing.JLabel();
        taxTextLabel1 = new javax.swing.JLabel();
        paidValueLabel = new javax.swing.JLabel();
        grnTotalTextLabel1 = new javax.swing.JLabel();
        balanceValueLabel = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        payButton = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        grnPaymentsTable = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        paidAmountSummeryLabel = new javax.swing.JLabel();
        grnBarcodeLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Payment History");
        setResizable(false);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 105, 75));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/logo-extra-sm.png"))); // NOI18N
        jLabel2.setIconTextGap(20);

        jLabel3.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 105, 75));
        jLabel3.setText("GRN Payment History");
        jLabel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));

        jSeparator1.setForeground(new java.awt.Color(204, 204, 204));

        jSeparator2.setForeground(new java.awt.Color(51, 51, 51));

        totalTextLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        totalTextLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalTextLabel.setText("Sub Total :");

        discountTextLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        discountTextLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        discountTextLabel.setText("Discount :");

        netTotalTextLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        netTotalTextLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        netTotalTextLabel.setText("Net Total :");

        taxTextLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        taxTextLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        taxTextLabel.setText("Tax :");

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

        taxValueLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        taxValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        taxValueLabel.setText("0.00");
        taxValueLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));

        jSeparator4.setForeground(new java.awt.Color(51, 51, 51));

        grnTotalValueLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        grnTotalValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        grnTotalValueLabel.setText("0.00");
        grnTotalValueLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));

        grnTotalTextLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        grnTotalTextLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        grnTotalTextLabel.setText("GRN Total :");

        taxTextLabel1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        taxTextLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        taxTextLabel1.setText("Paid Amount :");

        paidValueLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        paidValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        paidValueLabel.setText("- 0.00");
        paidValueLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));

        grnTotalTextLabel1.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        grnTotalTextLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        grnTotalTextLabel1.setText("Balance :");

        balanceValueLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        balanceValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        balanceValueLabel.setText("0.00");
        balanceValueLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));

        jSeparator5.setForeground(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(totalTextLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(subTotalValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(discountTextLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(discountValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(netTotalTextLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(netTotalValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(taxTextLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(taxValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(grnTotalTextLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(balanceValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(grnTotalTextLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(grnTotalValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(taxTextLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(paidValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalTextLabel)
                    .addComponent(subTotalValueLabel))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(discountTextLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(discountValueLabel))
                .addGap(0, 0, 0)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(netTotalTextLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(netTotalValueLabel))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(taxTextLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(taxValueLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(grnTotalTextLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(grnTotalValueLabel))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(taxTextLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(paidValueLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(grnTotalTextLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(balanceValueLabel))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        jLabel5.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel5.setText("Summery");
        jLabel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

        payButton.setText("Pay");
        payButton.setToolTipText("Add New Product");
        payButton.setBorder(null);
        payButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(payButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(payButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        grnPaymentsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "Paid Amount", "Payment Method", "Reference No.", "Paid Date Time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        grnPaymentsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        grnPaymentsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                grnPaymentsTableMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                grnPaymentsTableMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(grnPaymentsTable);
        if (grnPaymentsTable.getColumnModel().getColumnCount() > 0) {
            grnPaymentsTable.getColumnModel().getColumn(0).setMaxWidth(50);
            grnPaymentsTable.getColumnModel().getColumn(4).setMinWidth(100);
        }

        jLabel6.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel6.setText("Payments");
        jLabel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 0));

        paidAmountSummeryLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        paidAmountSummeryLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        paidAmountSummeryLabel.setText("Paid Amount : ");
        paidAmountSummeryLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(paidAmountSummeryLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(paidAmountSummeryLabel)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(84, 84, 84))
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        grnBarcodeLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        grnBarcodeLabel.setForeground(new java.awt.Color(0, 105, 75));
        grnBarcodeLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        grnBarcodeLabel.setText("GRN0000049");
        grnBarcodeLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 10));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(grnBarcodeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(grnBarcodeLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
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

    private void payButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payButtonActionPerformed
        viewGrnPayment();
    }//GEN-LAST:event_payButtonActionPerformed

    private void grnPaymentsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_grnPaymentsTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_grnPaymentsTableMouseClicked

    private void grnPaymentsTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_grnPaymentsTableMouseReleased

    }//GEN-LAST:event_grnPaymentsTableMouseReleased

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
            java.util.logging.Logger.getLogger(GrnPaymentHistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GrnPaymentHistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GrnPaymentHistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GrnPaymentHistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel balanceValueLabel;
    private javax.swing.JLabel discountTextLabel;
    private javax.swing.JLabel discountValueLabel;
    private javax.swing.JLabel grnBarcodeLabel;
    private javax.swing.JTable grnPaymentsTable;
    private javax.swing.JLabel grnTotalTextLabel;
    private javax.swing.JLabel grnTotalTextLabel1;
    private javax.swing.JLabel grnTotalValueLabel;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JLabel netTotalTextLabel;
    private javax.swing.JLabel netTotalValueLabel;
    private javax.swing.JLabel paidAmountSummeryLabel;
    private javax.swing.JLabel paidValueLabel;
    private javax.swing.JButton payButton;
    private javax.swing.JLabel subTotalValueLabel;
    private javax.swing.JLabel taxTextLabel;
    private javax.swing.JLabel taxTextLabel1;
    private javax.swing.JLabel taxValueLabel;
    private javax.swing.JLabel totalTextLabel;
    // End of variables declaration//GEN-END:variables
}
