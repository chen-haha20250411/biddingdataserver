package com.xiao.core.sqlserver.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Service
@Transactional(transactionManager = "sqlServerTransactionManager")
public class CustomerReportService {

    private final JdbcTemplate jdbcTemplate;
    private boolean configured = false;

    public CustomerReportService(@Qualifier("sqlServerDataSource") javax.sql.DataSource dataSource) {
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

    public List<Map<String, Object>> callCustomerSalesReportProcedure(String reportType, String startDate, String endDate,
                                                                      String customerName, String orgName, String salesPerson) {
        if (!configured || jdbcTemplate == null) {
            throw new RuntimeException("SQL Server未配置或配置不完整");
        }
        try {
            String sql = "{call saturn_p_CustomerAndCustOrgSalesReport(?, ?, ?, ?, ?, ?)}";
            System.out.println("========== 客户销售数据存储过程执行 ==========");
            System.out.println("参数 - reportType: " + reportType);
            System.out.println("参数 - startDate: " + startDate);
            System.out.println("参数 - endDate: " + endDate);
            System.out.println("参数 - customerName: " + customerName);
            System.out.println("参数 - orgName: " + orgName);
            System.out.println("参数 - salesPerson: " + salesPerson);
            System.out.println("执行SQL: " + sql);
            System.out.println("============================================");

            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, reportType, startDate, endDate,
                                                                         customerName, orgName, salesPerson);

            System.out.println("返回记录数: " + result.size());
            System.out.println("============================================");

            return result;
        } catch (Exception e) {
            System.err.println("存储过程执行失败: " + e.getMessage());
            throw new RuntimeException("存储过程执行失败: " + e.getMessage(), e);
        }
    }
}