package com.xiao.core.productReport.mapper;

import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface ProductReportMapper {

    List<Map<String, Object>> callProductPurchaseReport(@Param("startDate") String startDate,
                                                         @Param("endDate") String endDate,
                                                         @Param("personName") String personName);
}
