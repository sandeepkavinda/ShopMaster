/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author HP
 */
public class StockDataDTO {
    
    private String barcode;
    private int productId;
    private String productName;
    private String measUnitShortForm;

    public StockDataDTO() {
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getMeasUnitShortForm() {
        return measUnitShortForm;
    }

    public void setMeasUnitShortForm(String measUnitShortForm) {
        this.measUnitShortForm = measUnitShortForm;
    }

    
    
}
