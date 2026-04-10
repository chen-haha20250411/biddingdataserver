package com.xiao.core.productReport.domain;

import java.io.Serializable;

public class ProductReportData implements Serializable {

    private static final long serialVersionUID = 1L;

    private String productName;
    private String productCode;
    private Double purchaseAmount;
    private Double purchaseQuantity;
    private Double unitPrice;
    private String supplierName;
    private String salesPerson;
    private String purchaseDate;
    private String department;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Double getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(Double purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public Double getPurchaseQuantity() {
        return purchaseQuantity;
    }

    public void setPurchaseQuantity(Double purchaseQuantity) {
        this.purchaseQuantity = purchaseQuantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSalesPerson() {
        return salesPerson;
    }

    public void setSalesPerson(String salesPerson) {
        this.salesPerson = salesPerson;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
