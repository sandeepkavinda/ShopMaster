/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package SubGUI;

import DTO.InvoicePaymentData;
import model.Numbers;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Sandeep
 */
public class InvoicePayment extends javax.swing.JDialog {

    private InvoicePaymentData invoicePaymentData;
    private NewInvoice newInvoice;
    private String returnVoucherId;
    private double returnedAmount;
    private double usedReturnedAmount;
    private double payableAmount;
    // 1- Cash, 2- Card
    private String paymentMethodId = "1";

    public InvoicePayment(java.awt.Frame parent, boolean modal, InvoicePaymentData invoicePaymentData, NewInvoice newInvoice) {
        super(parent, modal);
        this.invoicePaymentData = invoicePaymentData;
        this.newInvoice = newInvoice;
        initComponents();
        subTotalValueLabel.setText(Numbers.formatPriceWithCurrencyCode(invoicePaymentData.getInvoiceTotal()));
        discountLabel.setText(Numbers.formatPriceWithCurrencyCode(invoicePaymentData.getInvoiceDiscount()));
        netTotalValueLabel.setText(Numbers.formatPriceWithCurrencyCode(invoicePaymentData.getInvoiceNetTotal()));

        if (invoicePaymentData.getReturnResultset() != null) {

            ResultSet returnResultSet = invoicePaymentData.getReturnResultset();

            try {
                returnVoucherId = returnResultSet.getString("id");
                returnedAmount = returnResultSet.getDouble("available_amount");
            } catch (SQLException ex) {
                Logger.getLogger(InvoicePayment.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }

        }

        setReturnedAmount();
        setPayableAmount();

    }

    private void setReturnedAmount() {
        usedReturnedAmount = Math.min(invoicePaymentData.getInvoiceNetTotal(), returnedAmount);
        returnedValueLabel.setText(Numbers.formatPriceWithCurrencyCode(usedReturnedAmount));
    }

    private void setPayableAmount() {
        payableAmount = invoicePaymentData.getInvoiceNetTotal() - usedReturnedAmount;
        payableTextLabel.setText("Payable :  " + Numbers.formatPriceWithCurrencyCode(payableAmount) + " ");
    }

    private void payToInvoice() {

        String paidAmountInput = paidAmountTextField.getText();

        if (paidAmountInput.isEmpty()) {
            paidAmountTextField.setText("0.00");
            paidAmountInput = "0.00";
        }

        try {
            double paidAmount = Double.parseDouble(paidAmountInput);

            if (paidAmount < payableAmount) {
                JOptionPane.showMessageDialog(this, "Paid amount does not cover the payable amount.", "Payment Error", JOptionPane.ERROR_MESSAGE);
            } else {

                paymentMethodId = cardRadioButton.isSelected() ? "2" : "1";

                String invoiceId = newInvoice.saveInvoice(returnVoucherId, returnedAmount, paidAmount, paymentMethodId, usedReturnedAmount);

                if (invoiceId != null) {
                    JOptionPane.showMessageDialog(this, "Paid Successfully", "Success", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/resource/success.png")));
                    openInvoiceSummery(invoiceId);
                }

            }

        } catch (NumberFormatException e) {
            // invalid double
            JOptionPane.showMessageDialog(this, "Invalid Paid Amount", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void openInvoiceSummery(String invoiceId) {
        this.dispose();
        InvoiceSummery invoiceSummery = new InvoiceSummery(newInvoice ,true, invoiceId, newInvoice);
        invoiceSummery.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        payableTextLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        netTotalTextLabel1 = new javax.swing.JLabel();
        paidAmountTextField = new javax.swing.JTextField();
        cashRadioButton = new javax.swing.JRadioButton();
        cardRadioButton = new javax.swing.JRadioButton();
        jPanel6 = new javax.swing.JPanel();
        clearSearchButton = new javax.swing.JButton();
        saveAndPrintButton = new javax.swing.JButton();
        saveAndPrintButton2 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel7 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        totalTextLabel3 = new javax.swing.JLabel();
        subTotalValueLabel = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        totalTextLabel4 = new javax.swing.JLabel();
        discountLabel = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        totalTextLabel5 = new javax.swing.JLabel();
        netTotalValueLabel = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        totalTextLabel2 = new javax.swing.JLabel();
        returnedValueLabel = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        cashMenuItem = new javax.swing.JMenuItem();
        cardMenuItem = new javax.swing.JMenuItem();

        jFormattedTextField1.setText("jFormattedTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Invoice Payment");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 105, 75));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/logo-extra-sm.png"))); // NOI18N
        jLabel2.setIconTextGap(20);

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 105, 75));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Invoice Payment");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

        jSeparator1.setForeground(new java.awt.Color(204, 204, 204));

        payableTextLabel.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        payableTextLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        payableTextLabel.setText("Payable :  Rs. 25,000.00 ");

        netTotalTextLabel1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        netTotalTextLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        netTotalTextLabel1.setText("Paid Amount");

        paidAmountTextField.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        paidAmountTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        paidAmountTextField.setText("0.00");
        paidAmountTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paidAmountTextFieldActionPerformed(evt);
            }
        });

        buttonGroup1.add(cashRadioButton);
        cashRadioButton.setSelected(true);
        cashRadioButton.setText("Cash (F1)");

        buttonGroup1.add(cardRadioButton);
        cardRadioButton.setText("Card (F2)");

        jPanel6.setLayout(new java.awt.GridLayout(1, 0, 7, 0));

        clearSearchButton.setBackground(new java.awt.Color(102, 102, 102));
        clearSearchButton.setForeground(new java.awt.Color(255, 255, 255));
        clearSearchButton.setText("Cancel Payment");
        clearSearchButton.setBorder(null);
        clearSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearSearchButtonActionPerformed(evt);
            }
        });
        jPanel6.add(clearSearchButton);

        saveAndPrintButton.setText("Save");
        saveAndPrintButton.setToolTipText("Add New Product");
        saveAndPrintButton.setBorder(null);
        saveAndPrintButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAndPrintButtonActionPerformed(evt);
            }
        });
        jPanel6.add(saveAndPrintButton);

        saveAndPrintButton2.setText("Save & Print");
        saveAndPrintButton2.setToolTipText("Add New Product");
        saveAndPrintButton2.setBorder(null);
        jPanel6.add(saveAndPrintButton2);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(netTotalTextLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(64, 64, 64)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(63, 63, 63)
                                        .addComponent(cashRadioButton)
                                        .addGap(33, 33, 33)
                                        .addComponent(cardRadioButton))
                                    .addComponent(paidAmountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(19, 19, 19))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(netTotalTextLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(paidAmountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cashRadioButton)
                    .addComponent(cardRadioButton))
                .addGap(27, 27, 27)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSeparator3.setForeground(new java.awt.Color(204, 204, 204));

        jPanel7.setLayout(new java.awt.GridLayout(1, 0));

        totalTextLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalTextLabel3.setText("Sub Total");

        subTotalValueLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        subTotalValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        subTotalValueLabel.setText("Rs.25,000");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalTextLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                    .addComponent(subTotalValueLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(totalTextLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subTotalValueLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.add(jPanel9);

        totalTextLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalTextLabel4.setText("Discount");

        discountLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        discountLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        discountLabel.setText("Rs.0.00");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalTextLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                    .addComponent(discountLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(totalTextLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(discountLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.add(jPanel11);

        totalTextLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalTextLabel5.setText("Net Total");

        netTotalValueLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        netTotalValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        netTotalValueLabel.setText("Rs.25,000.00");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalTextLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                    .addComponent(netTotalValueLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(totalTextLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(netTotalValueLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.add(jPanel12);

        totalTextLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalTextLabel2.setText("Returned");

        returnedValueLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        returnedValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        returnedValueLabel.setText("Rs.0.00");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalTextLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                    .addComponent(returnedValueLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(totalTextLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(returnedValueLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.add(jPanel8);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator3)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(payableTextLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(payableTextLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        jMenu1.setText("Method");

        cashMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        cashMenuItem.setText("Cash");
        cashMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cashMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(cashMenuItem);

        cardMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
        cardMenuItem.setText("Card");
        cardMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cardMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(cardMenuItem);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cashMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashMenuItemActionPerformed
        cashRadioButton.setSelected(true);
    }//GEN-LAST:event_cashMenuItemActionPerformed

    private void cardMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cardMenuItemActionPerformed
        cardRadioButton.setSelected(true);
    }//GEN-LAST:event_cardMenuItemActionPerformed

    private void clearSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearSearchButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_clearSearchButtonActionPerformed

    private void paidAmountTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paidAmountTextFieldActionPerformed

    }//GEN-LAST:event_paidAmountTextFieldActionPerformed

    private void saveAndPrintButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAndPrintButtonActionPerformed
        payToInvoice();
    }//GEN-LAST:event_saveAndPrintButtonActionPerformed

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
            java.util.logging.Logger.getLogger(InvoicePayment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InvoicePayment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InvoicePayment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InvoicePayment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JMenuItem cardMenuItem;
    private javax.swing.JRadioButton cardRadioButton;
    private javax.swing.JMenuItem cashMenuItem;
    private javax.swing.JRadioButton cashRadioButton;
    private javax.swing.JButton clearSearchButton;
    private javax.swing.JLabel discountLabel;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel netTotalTextLabel1;
    private javax.swing.JLabel netTotalValueLabel;
    private javax.swing.JTextField paidAmountTextField;
    private javax.swing.JLabel payableTextLabel;
    private javax.swing.JLabel returnedValueLabel;
    private javax.swing.JButton saveAndPrintButton;
    private javax.swing.JButton saveAndPrintButton2;
    private javax.swing.JLabel subTotalValueLabel;
    private javax.swing.JLabel totalTextLabel2;
    private javax.swing.JLabel totalTextLabel3;
    private javax.swing.JLabel totalTextLabel4;
    private javax.swing.JLabel totalTextLabel5;
    // End of variables declaration//GEN-END:variables
}
