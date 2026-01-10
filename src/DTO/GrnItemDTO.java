/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author HP
 */
public class GrnItemDTO {

    private StockDataDTO stockData;
    private BigDecimal costPrice;
    private BigDecimal markedPrice;
    private BigDecimal quantity;
    private String expiryDate;

    public GrnItemDTO() {
    }

    public StockDataDTO getStockData() {
        return stockData;
    }

    public void setStockData(StockDataDTO stockData) {
        this.stockData = stockData;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getMarkedPrice() {
        return markedPrice;
    }

    public void setMarkedPrice(BigDecimal markedPrice) {
        this.markedPrice = markedPrice;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public BigDecimal getLineTotal() {
        return costPrice.multiply(quantity).setScale(2, RoundingMode.HALF_UP);
    }

}
