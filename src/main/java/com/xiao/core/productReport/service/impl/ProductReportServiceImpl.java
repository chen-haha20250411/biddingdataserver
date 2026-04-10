package com.xiao.core.productReport.service.impl;

import com.xiao.core.productReport.service.ProductReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("productReportService")
@Transactional(transactionManager = "sqlServerTransactionManager")
public class ProductReportServiceImpl implements ProductReportService {

    private final JdbcTemplate jdbcTemplate;
    private boolean configured = false;

    public ProductReportServiceImpl(@Qualifier("sqlServerDataSource") javax.sql.DataSource dataSource) {
        if (dataSource != null) {
            this.jdbcTemplate = new JdbcTemplate(dataSource);
            this.configured = true;
        } else {
            this.jdbcTemplate = null;
            this.configured = false;
        }
    }

    public boolean isConfigured() {
        return configured;
    }

    @Override
    public List<Map<String, Object>> getProductPurchaseReport(String startDate, String endDate, String personName) {
        if (!configured || jdbcTemplate == null) {
            throw new RuntimeException("SQL Server未配置或配置不完整");
        }
        try {
            String sql = "{call saturn_P_Purchase_Report_YearTarget(?, ?, ?)}";
            System.out.println("========== 产品采购报表存储过程执行 ==========");
            System.out.println("参数 - startDate: " + startDate);
            System.out.println("参数 - endDate: " + endDate);
            System.out.println("参数 - personName: " + personName);
            System.out.println("执行SQL: " + sql);
            System.out.println("============================================");

            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, startDate, endDate, personName);

            System.out.println("返回记录数: " + result.size());
            System.out.println("============================================");

            return result;
        } catch (Exception e) {
            System.err.println("存储过程执行失败: " + e.getMessage());
            throw new RuntimeException("存储过程执行失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getProductPurchaseReportForTYCP(String startDate, String endDate, String purchaseMan, String statDimension) {
        if (!configured || jdbcTemplate == null) {
            throw new RuntimeException("SQL Server未配置或配置不完整");
        }
        try {
            String sql = "{call saturn_P_TYCP_Purchase_Report_YearTarget(?, ?, ?, ?)}";
            System.out.println("========== 通用产品采购报表存储过程执行 ==========");
            System.out.println("参数 - startDate: " + startDate);
            System.out.println("参数 - endDate: " + endDate);
            System.out.println("参数 - purchaseMan: " + purchaseMan);
            System.out.println("参数 - statDimension: " + statDimension);
            System.out.println("执行SQL: " + sql);
            System.out.println("============================================");

            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, startDate, endDate, purchaseMan, statDimension);

            System.out.println("返回记录数: " + result.size());
            System.out.println("============================================");

            return result;
        } catch (Exception e) {
            System.err.println("存储过程执行失败: " + e.getMessage());
            throw new RuntimeException("存储过程执行失败: " + e.getMessage(), e);
        }
    }
}
