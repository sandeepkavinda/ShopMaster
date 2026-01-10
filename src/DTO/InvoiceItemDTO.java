/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author HP
 */
public class InvoiceItemDTO {
    
    private String stockBarcode;
    private String productName;
    private String markedPrice;
    private String sellingDiscount;
    private String sellingPrice;
    private String measurementUnit;
    private double avalibleQuantity;

    public String getStockBarcode() {
        return stockBarcode;
    }

    public void setStockBarcode(String stockBarcode) {
        this.stockBarcode = stockBarcode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getMarkedPrice() {
        return markedPrice;
    }

    public void setMarkedPrice(String markedPrice) {
        this.markedPrice = markedPrice;
    }

    public String getSellingDiscount() {
        return sellingDiscount;
    }

    public void setSellingDiscount(String sellingDiscount) {
        this.sellingDiscount = sellingDiscount;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getMeasurementUnit() {
        return measurementUnit;
    }

    public void setMeasurementUnit(String measurementUnit) {
        this.measurementUnit = measurementUnit;
    }

    public double getAvalibleQuantity() {
        return avalibleQuantity;
    }

    public void setAvalibleQuantity(double avalibleQuantity) {
        this.avalibleQuantity = avalibleQuantity;
    }   
    
}
