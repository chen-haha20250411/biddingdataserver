package com.xiao.core.customerSales.mapper;

import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface CustomerSalesMapper {
    
    List<Map<String, Object>> callCustomerAndCustOrgSalesReport(@Param("reportType") String reportType,
                                                                 @Param("startDate") String startDate,
                                                                 @Param("endDate") String endDate,
                                                                 @Param("customerName") String customerName,
                                                                 @Param("orgName") String orgName,
                                                                 @Param("salesPerson") String salesPerson);
}