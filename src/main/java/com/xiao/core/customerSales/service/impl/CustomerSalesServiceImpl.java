package com.xiao.core.customerSales.service.impl;

import com.xiao.core.customerSales.service.CustomerSalesService;
import com.xiao.core.sqlserver.service.CustomerReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("customerSalesService")
public class CustomerSalesServiceImpl implements CustomerSalesService {

    @Autowired
    private CustomerReportService customerReportService;

    @Override
    public List<Map<String, Object>> getCustomerSalesData(String reportType, String startDate, String endDate,
                                                          String customerName, String orgName, String salesPerson) {
        return customerReportService.callCustomerSalesReportProcedure(reportType, startDate, endDate,
                                                                      customerName, orgName, salesPerson);
    }
}