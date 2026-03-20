package com.xiao.core.customerSales.service;

import com.xiao.core.customerSales.domain.CustomerSalesData;
import java.util.List;
import java.util.Map;

public interface CustomerSalesService {
    
    List<Map<String, Object>> getCustomerSalesData(String reportType, String startDate, String endDate, 
                                                   String customerName, String orgName, String salesPerson);
}