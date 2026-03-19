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
public class SqlServerTestService {

    private final JdbcTemplate jdbcTemplate;
    private boolean configured = false;

    public SqlServerTestService(@Qualifier("sqlServerDataSource") javax.sql.DataSource dataSource) {
        if (dataSource != null) {
            this.jdbcTemplate = new JdbcTemplate(dataSource);
            this.configured = true;
        } else {
            this.jdbcTemplate = null;
            this.configured = false;
        }
    }

    @PostConstruct
    public void init() {
        System.out.println("\n==========================================");
        System.out.println("  SQL Server 2008 数据库连接测试");
        System.out.println("==========================================");
        
        if (!configured) {
            System.out.println("ℹ️  SQL Server未配置或配置不完整");
            System.out.println("   请在MySQL的sys_dict表中添加以下配置项:");
            System.out.println("   - param_name='sqlserver.url', dict_name='jdbc:sqlserver://10.1.1.10:1433;databaseName=kpm68099;...'");
            System.out.println("   - param_name='sqlserver.username', dict_name=''");
            System.out.println("   - param_name='sqlserver.password', dict_name=''");
            System.out.println("   - dict_type='system_config', dict_stat='1'");
            System.out.println("==========================================\n");
            return;
        }
        
        try {
            // 测试基本连接
            boolean connected = testConnection();
            if (connected) {
                System.out.println("✅ 基础连接测试: 成功");
                
                // 执行用户指定的查询
                System.out.println("🔍 执行测试查询: SELECT TOP 4  * staff_name,staff_code, STAFF_SEX FROM common_staffinfo WHERE is_deleted = 0");
                try {
                    List<Map<String, Object>> results = testQuery();
                    System.out.println("✅ 查询测试: 成功，返回 " + results.size() + " 条记录");
                    
                    // 打印表结构（第一条记录的字段名）
                    if (!results.isEmpty()) {
                        Map<String, Object> firstRow = results.get(0);
                        System.out.println("📊 表结构字段: " + String.join(", ", firstRow.keySet()));
                        
                        // 打印前3条记录
                        System.out.println("📋 示例数据 (前3条):");
                        int limit = Math.min(3, results.size());
                        for (int i = 0; i < limit; i++) {
                            Map<String, Object> row = results.get(i);
                            System.out.println("  行 " + (i+1) + ": " + row.toString());
                        }
                    }
                } catch (Exception queryEx) {
                    System.err.println("⚠️  查询测试失败: " + queryEx.getMessage());
                    System.err.println("   提示: common_staffinfo 表可能不存在或无权限访问");
                }
            } else {
                System.err.println("❌ 基础连接测试: 失败");
                System.err.println("   请检查:");
                System.err.println("   1. SQL Server 服务是否运行");
                System.err.println("   2. 数据库中的连接URL是否正确");
                System.err.println("   3. 用户名密码是否正确");
                System.err.println("   4. 防火墙设置");
                System.err.println("   5. TLS 1.0 支持 (SQL Server 2008 需要)");
            }
        } catch (Exception e) {
            System.err.println("❌ 连接测试异常: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("==========================================\n");
    }

    public boolean testConnection() {
        if (!configured || jdbcTemplate == null) {
            return false;
        }
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Map<String, Object>> testQuery() {
        if (!configured || jdbcTemplate == null) {
            throw new RuntimeException("SQL Server未配置或配置不完整");
        }
        try {
            // 使用用户指定的查询语句
            String sql = "SELECT TOP 4  staff_name,staff_id, STAFF_SEX FROM common_staffinfo WHERE is_deleted = 0 order by staff_id desc ";
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            throw new RuntimeException("SQL Server 查询失败: " + e.getMessage(), e);
        }
    }

    public List<Map<String, Object>> executeCustomQuery(String sql) {
        if (!configured || jdbcTemplate == null) {
            throw new RuntimeException("SQL Server未配置或配置不完整");
        }
        try {
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            throw new RuntimeException("SQL 执行失败: " );
        }
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