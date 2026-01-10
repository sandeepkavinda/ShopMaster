package SubGUI;

import DTO.GrnTotalsDTO;
import java.awt.Color;
import java.awt.Toolkit;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import model.BigDecimalFormatter;
import model.MySQL;
import model.Validation;
import panels.GrnManagement;

/**
 *
 * @author Sandeep
 */
public class GRNPayment extends javax.swing.JFrame {

    private String grnBarcode;
    private GrnManagement grnManagement;
    private GrnTotalsDTO grnTotalsDTO;
    private HashMap<Integer, Integer> paymentMethodIdMap = new HashMap<>();

    public GRNPayment(String grnBarcode, GrnManagement grnManagement) {
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resource/icon.png")));
        this.grnBarcode = grnBarcode;
        this.grnManagement = grnManagement;
        initComponents();
        loadPaymentMethods();

        loadGrnData();
    }

    private void loadGrnData() {
        try {
            ResultSet resultset = MySQL.executeQuery("SELECT * FROM grn WHERE barcode=?",
                    grnBarcode
            );

            if (resultset.next()) {
                grnTotalsDTO = new GrnTotalsDTO();
                grnTotalsDTO.setSubTotal(resultset.getBigDecimal("sub_total"));
                grnTotalsDTO.setDiscount(resultset.getBigDecimal("discount"));
                grnTotalsDTO.setNetTotal(resultset.getBigDecimal("net_total"));
                grnTotalsDTO.setTax(resultset.getBigDecimal("tax"));
                grnTotalsDTO.setGrnTotal(resultset.getBigDecimal("grn_total"));
                grnTotalsDTO.setPaidAmount(resultset.getBigDecimal("paid_amount"));
                grnTotalsDTO.setBalance(resultset.getBigDecimal("balance"));

                grnBarcodeLabel.setText(resultset.getString("barcode"));
                createdAtLabel.setText(resultset.getString("created_at"));

                subTotalValueLabel.setText(BigDecimalFormatter.formatPrice(grnTotalsDTO.getSubTotal()));
                discountValueLabel.setText(BigDecimalFormatter.formatPrice(grnTotalsDTO.getDiscount()));
                netTotalValueLabel.setText(BigDecimalFormatter.formatPrice(grnTotalsDTO.getNetTotal()));
                taxValueLabel.setText(BigDecimalFormatter.formatPrice(grnTotalsDTO.getTax()));
                grnTotalValueLabel.setText(BigDecimalFormatter.formatPrice(grnTotalsDTO.getGrnTotal()));
                paidValueLabel.setText(BigDecimalFormatter.formatPrice(grnTotalsDTO.getPaidAmount()));
                balanceValueLabel.setText(BigDecimalFormatter.formatPrice(grnTotalsDTO.getBalance()));

                if (grnTotalsDTO.getBalance().compareTo(BigDecimal.ZERO) <= 0) {
                    paymentTextLabel.setText("Paid");
                    paymentTextLabel.setForeground(Color.decode("#00694B"));
                    payningAmountLabel.setEnabled(false);
                    payingAmountFormattedTextField.setEnabled(false);
                    paymentMethodLabel.setEnabled(false);
                    paymentMethodComboBox.setEnabled(false);
                    refNoLabel.setEnabled(false);
                    referenceNoTextField.setEnabled(false);
                    payLaterButton.setEnabled(false);
                    payButton.setEnabled(false);
                    payAndPrintButton.setEnabled(false);
                } else {
                    paymentTextLabel.setText("Payment");
                    paymentTextLabel.setForeground(Color.decode("#000000"));
                    payningAmountLabel.setEnabled(true);
                    payingAmountFormattedTextField.setEnabled(true);
                    paymentMethodLabel.setEnabled(true);
                    paymentMethodComboBox.setEnabled(true);
                    refNoLabel.setEnabled(true);
                    referenceNoTextField.setEnabled(true);
                    payLaterButton.setEnabled(true);
                    payButton.setEnabled(true);
                    payAndPrintButton.setEnabled(true);
                }

                this.setVisible(true);

            } else {
                JOptionPane.showMessageDialog(this, "Invalid GRN Barcode", "Unexpected Error", JOptionPane.ERROR_MESSAGE);

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An unexpected error has occurred. Please try again later or contact support if the issue persists.", "Unexpected Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }

    private void loadPaymentMethods() {
        try {
            ResultSet resultSet = MySQL.executeQuery("SELECT * FROM grn_payment_method");
            Vector v = new Vector();
            v.add("Select");

            int comboBoxIndex = 1;

            while (resultSet.next()) {
                v.add(resultSet.getString("name"));
                paymentMethodIdMap.put(comboBoxIndex, resultSet.getInt("id"));
                comboBoxIndex++;
            }

            DefaultComboBoxModel model = new DefaultComboBoxModel(v);
            paymentMethodComboBox.setModel(model);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An unexpected error has occurred. Please try again later or contact support if the issue persists.", "Unexpected Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }

    private void clearData() {
        payingAmountFormattedTextField.setText("0");
        paymentMethodComboBox.setSelectedIndex(0);
        referenceNoTextField.setText("");
    }

    private void pay() {

        String payingAmountText = payingAmountFormattedTextField.getText().replace(",", "");
        int selectedPaymentMethod = paymentMethodComboBox.getSelectedIndex();
        String referenceNo = referenceNoTextField.getText();

        if (!Validation.isValidBigDecimal(payingAmountText)) {
            JOptionPane.showMessageDialog(null, "Invalid paying amount", "Warning", JOptionPane.WARNING_MESSAGE);
            payingAmountFormattedTextField.grabFocus();
            payingAmountFormattedTextField.selectAll();
        } else if (new BigDecimal(payingAmountText).compareTo(BigDecimal.ZERO) <= 0) {
            JOptionPane.showMessageDialog(null, "Paying amount must be greater than 0", "Warning", JOptionPane.WARNING_MESSAGE);
            payingAmountFormattedTextField.grabFocus();
            payingAmountFormattedTextField.selectAll();
        } else if (new BigDecimal(payingAmountText).compareTo(grnTotalsDTO.getBalance()) > 0) {
            JOptionPane.showMessageDialog(null, "Cannot pay more than balance", "Warning", JOptionPane.WARNING_MESSAGE);
            payingAmountFormattedTextField.grabFocus();
            payingAmountFormattedTextField.selectAll();
        } else if (selectedPaymentMethod == 0) {
            JOptionPane.showMessageDialog(null, "Please Select Payment Method", "Warning", JOptionPane.WARNING_MESSAGE);
            paymentMethodComboBox.grabFocus();
        } else if (referenceNo.length() > 50) {
            JOptionPane.showMessageDialog(null, "Reference Number cannot exceed 50 characters", "Warning", JOptionPane.WARNING_MESSAGE);
            referenceNoTextField.grabFocus();
            referenceNoTextField.selectAll();
        } else {

            int selectedPaymentMethodId = paymentMethodIdMap.get(selectedPaymentMethod);
            BigDecimal payingAmount = new BigDecimal(payingAmountText);

            // Reference no. to null when it's blank
            if (referenceNo.isBlank()) {
                referenceNo = null;
            }

            try {
                MySQL.beginTransaction();

                //Add to payments table
                MySQL.executeUpdate("INSERT INTO grn_payment (grn_barcode, paid_amount, grn_payment_method_id, reference_no) "
                        + "VALUES (?,?,?,?)",
                        this.grnBarcode,
                        payingAmount,
                        selectedPaymentMethodId,
                        referenceNo
                );

                //Update Grn Paid Amount
                MySQL.executeUpdate("UPDATE grn SET paid_amount = paid_amount + ? WHERE barcode = ?",
                        payingAmount,
                        this.grnBarcode
                );

                //Double Check Paid Amount greater than Grn Total
                ResultSet resultSet = MySQL.executeQuery("SELECT * FROM grn WHERE barcode=? AND grn_total < paid_amount",
                        this.grnBarcode
                );
                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(null, "Another payment was processed before you", "Warning", JOptionPane.WARNING_MESSAGE);
                    MySQL.rollback();
                } else {
                    MySQL.commit();
                    clearData();
                    loadGrnData();
                    JOptionPane.showMessageDialog(this, "Paid Successfully", "Success", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/resource/success.png")));

                    if (grnManagement != null) {
                        grnManagement.loadGRNTable();
                    }

                }

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
        buttonGroup1 = new javax.swing.ButtonGroup();
        timePicker = new com.raven.swing.TimePicker();
        jMenu1 = new javax.swing.JMenu();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        paymentTextLabel = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        payLaterButton = new javax.swing.JButton();
        payButton = new javax.swing.JButton();
        payAndPrintButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        payingAmountFormattedTextField = new javax.swing.JFormattedTextField();
        payningAmountLabel = new javax.swing.JLabel();
        paymentMethodLabel = new javax.swing.JLabel();
        paymentMethodComboBox = new javax.swing.JComboBox<>();
        refNoLabel = new javax.swing.JLabel();
        referenceNoTextField = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        grnBarcodeLabel = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
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
        createdAtLabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        dateChooser.setForeground(new java.awt.Color(0, 105, 75));
        dateChooser.setDateFormat("yyyy-MM-dd");

        timePicker.setForeground(new java.awt.Color(0, 105, 75));
        timePicker.set24hourMode(true);

        jMenu1.setText("jMenu1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("GRN Payment");
        setPreferredSize(new java.awt.Dimension(486, 759));
        setResizable(false);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 105, 75));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/logo-extra-sm.png"))); // NOI18N
        jLabel2.setIconTextGap(20);

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 105, 75));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("GRN Payment");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

        jSeparator1.setForeground(new java.awt.Color(204, 204, 204));

        paymentTextLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        paymentTextLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        paymentTextLabel.setText("Payment");

        jPanel6.setLayout(new java.awt.GridLayout(1, 0, 7, 0));

        payLaterButton.setBackground(new java.awt.Color(102, 102, 102));
        payLaterButton.setForeground(new java.awt.Color(255, 255, 255));
        payLaterButton.setText("Pay Later");
        payLaterButton.setBorder(null);
        payLaterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payLaterButtonActionPerformed(evt);
            }
        });
        jPanel6.add(payLaterButton);

        payButton.setText("Pay");
        payButton.setToolTipText("Add New Product");
        payButton.setBorder(null);
        payButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payButtonActionPerformed(evt);
            }
        });
        jPanel6.add(payButton);

        payAndPrintButton.setText("Pay & Print");
        payAndPrintButton.setToolTipText("Add New Product");
        payAndPrintButton.setBorder(null);
        jPanel6.add(payAndPrintButton);

        payingAmountFormattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.##"))));
        payingAmountFormattedTextField.setText("0");
        payingAmountFormattedTextField.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        payingAmountFormattedTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payingAmountFormattedTextFieldActionPerformed(evt);
            }
        });

        payningAmountLabel.setText("Paying Amount");

        paymentMethodLabel.setText("Payment Method");

        paymentMethodComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All" }));
        paymentMethodComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paymentMethodComboBoxActionPerformed(evt);
            }
        });

        refNoLabel.setText("Reference No. (Optional)");

        referenceNoTextField.setToolTipText("You can search using Invoice ID and issued date and time.");
        referenceNoTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                referenceNoTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(referenceNoTextField)
                    .addComponent(payingAmountFormattedTextField)
                    .addComponent(paymentMethodComboBox, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(paymentMethodLabel)
                            .addComponent(payningAmountLabel)
                            .addComponent(refNoLabel))
                        .addGap(0, 117, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(payningAmountLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(payingAmountFormattedTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(paymentMethodLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(paymentMethodComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(refNoLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(referenceNoTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(98, 98, 98)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(paymentTextLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(paymentTextLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSeparator3.setForeground(new java.awt.Color(204, 204, 204));

        grnBarcodeLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        grnBarcodeLabel.setForeground(new java.awt.Color(51, 51, 51));
        grnBarcodeLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        grnBarcodeLabel.setText("GRN0000001");
        grnBarcodeLabel.setToolTipText("GRN Id");
        grnBarcodeLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 50, 0, 0));

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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(totalTextLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(subTotalValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(discountTextLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(discountValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(netTotalTextLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(netTotalValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(taxTextLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(taxValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(grnTotalTextLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(grnTotalValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(taxTextLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(paidValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(grnTotalTextLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(balanceValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalTextLabel)
                    .addComponent(subTotalValueLabel))
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(discountTextLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(discountValueLabel))
                .addGap(0, 0, 0)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(netTotalTextLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(netTotalValueLabel))
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(taxTextLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(taxValueLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(grnTotalTextLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(grnTotalValueLabel))
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(taxTextLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(paidValueLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(grnTotalTextLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(balanceValueLabel))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        createdAtLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        createdAtLabel.setForeground(new java.awt.Color(51, 51, 51));
        createdAtLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        createdAtLabel.setText("2025-12-31 12:25:59");
        createdAtLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 50));

        jLabel5.setForeground(new java.awt.Color(51, 51, 51));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("GRN ID");
        jLabel5.setToolTipText("GRN Id");
        jLabel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 50, 0, 0));

        jLabel6.setForeground(new java.awt.Color(51, 51, 51));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Created Date Time");
        jLabel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 50));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(grnBarcodeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(createdAtLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(grnBarcodeLabel)
                    .addComponent(createdAtLabel))
                .addGap(21, 21, 21)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void payLaterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payLaterButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_payLaterButtonActionPerformed

    private void payButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payButtonActionPerformed
        pay();
    }//GEN-LAST:event_payButtonActionPerformed

    private void payingAmountFormattedTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payingAmountFormattedTextFieldActionPerformed
        paymentMethodComboBox.grabFocus();
    }//GEN-LAST:event_payingAmountFormattedTextFieldActionPerformed

    private void paymentMethodComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paymentMethodComboBoxActionPerformed
        referenceNoTextField.grabFocus();
    }//GEN-LAST:event_paymentMethodComboBoxActionPerformed

    private void referenceNoTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_referenceNoTextFieldActionPerformed
        pay();
    }//GEN-LAST:event_referenceNoTextFieldActionPerformed

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
    private javax.swing.JLabel balanceValueLabel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel createdAtLabel;
    private com.raven.datechooser.DateChooser dateChooser;
    private javax.swing.JLabel discountTextLabel;
    private javax.swing.JLabel discountValueLabel;
    private javax.swing.JLabel grnBarcodeLabel;
    private javax.swing.JLabel grnTotalTextLabel;
    private javax.swing.JLabel grnTotalTextLabel1;
    private javax.swing.JLabel grnTotalValueLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JLabel netTotalTextLabel;
    private javax.swing.JLabel netTotalValueLabel;
    private javax.swing.JLabel paidValueLabel;
    private javax.swing.JButton payAndPrintButton;
    private javax.swing.JButton payButton;
    private javax.swing.JButton payLaterButton;
    private javax.swing.JFormattedTextField payingAmountFormattedTextField;
    private javax.swing.JComboBox<String> paymentMethodComboBox;
    private javax.swing.JLabel paymentMethodLabel;
    private javax.swing.JLabel paymentTextLabel;
    private javax.swing.JLabel payningAmountLabel;
    private javax.swing.JLabel refNoLabel;
    private javax.swing.JTextField referenceNoTextField;
    private javax.swing.JLabel subTotalValueLabel;
    private javax.swing.JLabel taxTextLabel;
    private javax.swing.JLabel taxTextLabel1;
    private javax.swing.JLabel taxValueLabel;
    private com.raven.swing.TimePicker timePicker;
    private javax.swing.JLabel totalTextLabel;
    // End of variables declaration//GEN-END:variables
}
