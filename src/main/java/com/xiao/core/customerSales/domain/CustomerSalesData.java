package com.xiao.core.customerSales.domain;

import java.io.Serializable;

public class CustomerSalesData implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String customerName;
    private String customerCode;
    private Double salesAmount;
    private Double profitAmount;
    private Double profitRate;
    private String region;
    private String salesPerson;
    private String department;
    private String businessLine;
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getCustomerCode() {
        return customerCode;
    }
    
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }
    
    public Double getSalesAmount() {
        return salesAmount;
    }
    
    public void setSalesAmount(Double salesAmount) {
        this.salesAmount = salesAmount;
    }
    
    public Double getProfitAmount() {
        return profitAmount;
    }
    
    public void setProfitAmount(Double profitAmount) {
        this.profitAmount = profitAmount;
    }
    
    public Double getProfitRate() {
        return profitRate;
    }
    
    public void setProfitRate(Double profitRate) {
        this.profitRate = profitRate;
    }
    
    public String getRegion() {
        return region;
    }
    
    public void setRegion(String region) {
        this.region = region;
    }
    
    public String getSalesPerson() {
        return salesPerson;
    }
    
    public void setSalesPerson(String salesPerson) {
        this.salesPerson = salesPerson;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getBusinessLine() {
        return businessLine;
    }
    
    public void setBusinessLine(String businessLine) {
        this.businessLine = businessLine;
    }
}