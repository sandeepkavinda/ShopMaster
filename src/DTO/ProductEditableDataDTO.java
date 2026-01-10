/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author HP
 */
public class ProductEditableDataDTO {
    private String productName;
    private int categoryId;
    private String category;
    private int measurementUnitId;

    public ProductEditableDataDTO(String productName, int categoryId, String category, int measurementUnitId) {
        this.productName = productName;
        this.categoryId = categoryId;
        this.category = category;
        this.measurementUnitId = measurementUnitId;
    }

    public ProductEditableDataDTO() {
    }

    public String getProductName() {
        return productName;
    }

    public void setName(String getProductName) {
        this.productName = productName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getMeasurementUnitId() {
        return measurementUnitId;
    }

    public void setMeasurementUnitId(int measurementUnitId) {
        this.measurementUnitId = measurementUnitId;
    }

}
