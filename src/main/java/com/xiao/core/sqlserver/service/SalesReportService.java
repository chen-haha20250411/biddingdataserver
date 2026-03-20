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
public class SalesReportService {

    private final JdbcTemplate jdbcTemplate;
    private boolean configured = false;

    public SalesReportService(@Qualifier("sqlServerDataSource") javax.sql.DataSource dataSource) {
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

    public List<Map<String, Object>> callSalesProfitReportProcedure(String startDate, String endDate, String staffName,
                                                                   String parentDepartment, String department,
                                                                   String businessLine, String branch) {
        if (!configured || jdbcTemplate == null) {
            throw new RuntimeException("SQL Server未配置或配置不完整");
        }
        try {
            String sql = "{call saturn_p_GetSalesProfitReport18_condition(?, ?, ?, ?, ?, ?, ?)}";
            System.out.println("========== 销售利润报表存储过程执行 ==========");
            System.out.println("参数 - startDate: " + startDate);
            System.out.println("参数 - endDate: " + endDate);
            System.out.println("参数 - 员工: " + staffName);
            System.out.println("参数 - 父级部门: " + parentDepartment);
            System.out.println("参数 - 部门: " + department);
            System.out.println("参数 - 业务线: " + businessLine);
            System.out.println("参数 - 分支机构: " + branch);
            System.out.println("执行SQL: " + sql);
            System.out.println("============================================");

            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, startDate, endDate, staffName,
                                                                         parentDepartment, department, businessLine, branch);

            System.out.println("返回记录数: " + result.size());
            System.out.println("============================================");

            return result;
        } catch (Exception e) {
            System.err.println("存储过程执行失败: " + e.getMessage());
            throw new RuntimeException("存储过程执行失败: " + e.getMessage(), e);
        }
    }

    public List<Map<String, Object>> callSalesTargetProcedure(int year, String groupName, String groupCondition) {
        if (!configured || jdbcTemplate == null) {
            throw new RuntimeException("SQL Server未配置或配置不完整");
        }
        try {
            String sql = "{call sp_GetSalesData_2026_v5(?, ?, ?)}";
            System.out.println("========== 销售额目标存储过程执行 ==========");
            System.out.println("参数 - year: " + year);
            System.out.println("参数 - groupName: " + groupName);
            System.out.println("参数 - groupCondition: " + groupCondition);
            System.out.println("执行SQL: " + sql);
            System.out.println("============================================");

            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, year, groupName, groupCondition);

            System.out.println("返回记录数: " + result.size());
            System.out.println("============================================");

            return result;
        } catch (Exception e) {
            System.err.println("存储过程执行失败: " + e.getMessage());
            throw new RuntimeException("存储过程执行失败: " + e.getMessage(), e);
        }
    }

    public List<Map<String, Object>> callValueAddedBusinessProcedure(String startDate, String endDate, String personNameList,
                                                                   String deptGroupList, String orgNameList,
                                                                   String subOrgIdList, String sGroupList) {
        if (!configured || jdbcTemplate == null) {
            throw new RuntimeException("SQL Server未配置或配置不完整");
        }
        try {
            String sql = "{call saturn_SalesAnalysis_ZZYWReport_Optimized_SUM(?, ?, ?, ?, ?, ?, ?)}";
            System.out.println("========== 增值业务数据存储过程执行 ==========");
            System.out.println("参数 - START_DATE: " + startDate);
            System.out.println("参数 - END_DATE: " + endDate);
            System.out.println("参数 - PERSON_NAME_LIST: " + personNameList);
            System.out.println("参数 - DEPT_GROUP_LIST: " + deptGroupList);
            System.out.println("参数 - ORG_NAME_LIST: " + orgNameList);
            System.out.println("参数 - SUB_ORG_ID_LIST: " + subOrgIdList);
            System.out.println("参数 - S_GROUP_LIST: " + sGroupList);
            System.out.println("执行SQL: " + sql);
            System.out.println("===========================================");

            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, startDate, endDate, personNameList,
                                                                         deptGroupList, orgNameList, subOrgIdList, sGroupList);

            System.out.println("返回记录数: " + result.size());
            System.out.println("===========================================");

            return result;
        } catch (Exception e) {
            System.err.println("存储过程执行失败: " + e.getMessage());
            throw new RuntimeException("存储过程执行失败: " + e.getMessage(), e);
        }
    }
}