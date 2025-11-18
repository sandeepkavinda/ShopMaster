package DTO;

import java.sql.ResultSet;

public class InvoicePaymentData {

    private double invoiceTotal;
    private double invoiceDiscount;
    private double invoiceNetTotal;
    private ResultSet returnResultset;

    public InvoicePaymentData() {

    }

    public InvoicePaymentData(double invoiceTotal, double invoiceDiscount, double invoiceNetTotal, ResultSet returnResultset) {
        this.invoiceTotal = invoiceTotal;
        this.invoiceDiscount = invoiceDiscount;
        this.invoiceNetTotal = invoiceNetTotal;
        this.returnResultset = returnResultset;
    }
    
    

    public double getInvoiceTotal() {
        return invoiceTotal;
    }

    public void setInvoiceTotal(double invoiceTotal) {
        this.invoiceTotal = invoiceTotal;
    }

    public double getInvoiceDiscount() {
        return invoiceDiscount;
    }

    public void setInvoiceDiscount(double invoiceDiscount) {
        this.invoiceDiscount = invoiceDiscount;
    }

    public double getInvoiceNetTotal() {
        return invoiceNetTotal;
    }

    public void setInvoiceNetTotal(double invoiceNetTotal) {
        this.invoiceNetTotal = invoiceNetTotal;
    }

    public ResultSet getReturnResultset() {
        return returnResultset;
    }

    public void setReturnResultset(ResultSet returnResultset) {
        this.returnResultset = returnResultset;
    }

   

    
    
}
