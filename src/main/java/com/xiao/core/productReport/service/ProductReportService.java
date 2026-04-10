package com.xiao.core.productReport.service;

import java.util.List;
import java.util.Map;

public interface ProductReportService {

    List<Map<String, Object>> getProductPurchaseReport(String startDate, String endDate, String personName);

    List<Map<String, Object>> getProductPurchaseReportForTYCP(String startDate, String endDate, String purchaseMan, String statDimension);
}
