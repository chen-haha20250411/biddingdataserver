package com.xiao.core.warning.service;

import com.xiao.util.RedisUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(transactionManager = "sqlServerTransactionManager")
public class WarningService {

    private final JdbcTemplate jdbcTemplate;
    private boolean configured = false;
    private static final String CACHE_KEY_PREFIX = "warning:summary:";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired(required = false)
    private RedisUtils redisUtils;

    public WarningService(@Qualifier("sqlServerDataSource") javax.sql.DataSource dataSource) {
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

    /**
     * 获取缓存Key
     */
    private String getCacheKey() {
        return CACHE_KEY_PREFIX + LocalDate.now().format(DATE_FORMATTER);
    }

    /**
     * 获取缓存过期剩余秒数（到次日0点）
     */
    private long getExpireSeconds() {
        long secondsUntilMidnight = java.time.Duration.between(java.time.LocalTime.now(), java.time.LocalTime.MAX).getSeconds();
        return secondsUntilMidnight + 1;
    }

    /**
     * 出库超期未收款预警
     * @param userName 业务员用户名，'ALL'或null表示查询所有
     * @return Map包含 data列表 和 totalStockCost汇总值
     */
    public Map<String, Object> get出库超30天预警(String userName) {
        if (!configured || jdbcTemplate == null) {
            throw new RuntimeException("SQL Server未配置或配置不完整");
        }
        try {
            Map<String, Object> result = new HashMap<>();
            List<Map<String, Object>> dataList = new ArrayList<>();
            BigDecimal totalStockCost = BigDecimal.ZERO;

            String sql = "{call satuan_p_warning_outstock_30days(?, ?, ?)}";

            Connection conn = null;
            CallableStatement cs = null;
            ResultSet rs = null;
            try {
                conn = jdbcTemplate.getDataSource().getConnection();
                cs = conn.prepareCall(sql);
                cs.setString(1, userName);
                cs.setInt(2, 1000);
                cs.registerOutParameter(3, Types.DECIMAL);
                cs.execute();

                rs = cs.getResultSet();
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("业务员", rs.getString("业务员"));
                    row.put("出库单号", rs.getString("出库单号"));
                    row.put("开票单号", rs.getString("开票单号"));
                    row.put("开票日期", rs.getString("开票日期"));
                    row.put("客户名称", rs.getString("客户名称"));
                    row.put("信用天数", rs.getInt("信用天数"));
                    row.put("出库成本", rs.getBigDecimal("出库成本"));
                    row.put("产品名称", rs.getString("产品名称"));
                    row.put("产品型号", rs.getString("产品型号"));
                    row.put("批次", rs.getString("批次"));
                    row.put("产品配置", rs.getString("产品配置"));
                    row.put("出库日期", rs.getString("出库日期"));
                    row.put("应收日期", rs.getString("应收日期"));
                    row.put("超期天数", rs.getInt("超期天数"));
                    row.put("制单人", rs.getString("制单人"));
                    row.put("总出库成本", rs.getBigDecimal("总出库成本"));
                    dataList.add(row);
                }

                totalStockCost = cs.getBigDecimal(3);
                if (totalStockCost == null) {
                    totalStockCost = BigDecimal.ZERO;
                }
            } finally {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (conn != null) conn.close();
            }

            result.put("data", dataList);
            result.put("totalStockCost", totalStockCost);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("satuan_p_warning_outstock_30days执行失败: " + e.getMessage(), e);
        }
    }

    /**
     * 已付款未到票预警
     */
    public List<Map<String, Object>> get已付款未到票预警(Integer orgId) {
        if (!configured || jdbcTemplate == null) {
            throw new RuntimeException("SQL Server未配置或配置不完整");
        }
        try {
            String sql = "{call satuan_p_warning_paid_no_invoice(?)}";
            return jdbcTemplate.queryForList(sql, orgId);
        } catch (Exception e) {
            throw new RuntimeException("satuan_p_warning_paid_no_invoice执行失败: " + e.getMessage(), e);
        }
    }

    /**
     * 超期客户押金预警
     * @param personName 业务员名称，'ALL'或null表示查询所有
     * @param isSummary 是否汇总模式，0=明细, 1=汇总
     * @return Map包含 data列表 和 totalMoney汇总值
     */
    public Map<String, Object> get超期客户押金预警(String personName, int isSummary) {
        if (!configured || jdbcTemplate == null) {
            throw new RuntimeException("SQL Server未配置或配置不完整");
        }
        try {
            Map<String, Object> result = new HashMap<>();
            List<Map<String, Object>> dataList = new ArrayList<>();
            BigDecimal totalMoney = BigDecimal.ZERO;

            String sql = "{call satuan_p_warning_overdue_deposit(?, ?, ?)}";

            Connection conn = null;
            CallableStatement cs = null;
            ResultSet rs = null;
            try {
                conn = jdbcTemplate.getDataSource().getConnection();
                cs = conn.prepareCall(sql);
                cs.setString(1, personName);
                cs.setInt(3, isSummary);
                cs.registerOutParameter(2, Types.DECIMAL);
                cs.execute();

                rs = cs.getResultSet();
                if (isSummary == 1) {
                    // 汇总模式：超期类型、汇总金额、笔数 (3列)
                    while (rs.next()) {
                        Map<String, Object> row = new HashMap<>();
                        row.put("超期类型", rs.getString(1));
                        row.put("汇总金额", rs.getBigDecimal(2));
                        row.put("笔数", rs.getInt(3));
                        dataList.add(row);
                    }
                } else {
                    // 明细模式：类型、PersonName、金额、单号、CustName、Remark、TermDate (7列)
                    while (rs.next()) {
                        Map<String, Object> row = new HashMap<>();
                        row.put("类型", rs.getString(1));
                        row.put("PersonName", rs.getString(2));
                        row.put("金额", rs.getBigDecimal(3));
                        row.put("单号", rs.getString(4));
                        row.put("CustName", rs.getString(5));
                        row.put("Remark", rs.getString(6));
                        row.put("TermDate", rs.getString(7));
                        dataList.add(row);
                    }
                }
                rs.close();

                // 必须先消耗完所有 ResultSet 才能获取 OUTPUT 参数
                while (cs.getMoreResults()) {
                    ResultSet extraRs = cs.getResultSet();
                    if (extraRs != null) {
                        while (extraRs.next()) { }
                        extraRs.close();
                    }
                }
                totalMoney = cs.getBigDecimal(2);  // 参数2: totalMoney OUTPUT
                if (totalMoney == null) {
                    totalMoney = BigDecimal.ZERO;
                }
            } finally {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (conn != null) conn.close();
            }

            result.put("data", dataList);
            result.put("totalMoney", totalMoney);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("satuan_p_warning_overdue_deposit执行失败: " + e.getMessage(), e);
        }
    }

    /**
     * 超期应收款预警
     * @param handlerName 业务员名称，'ALL'或null表示查询所有
     * @param isSummary 是否汇总模式，0=明细, 1=汇总
     * @return Map包含 data列表 和 totalLeftSum汇总值
     */
    public Map<String, Object> get超期应收款预警(String handlerName, int isSummary) {
        if (!configured || jdbcTemplate == null) {
            throw new RuntimeException("SQL Server未配置或配置不完整");
        }
        try {
            Map<String, Object> result = new HashMap<>();
            List<Map<String, Object>> dataList = new ArrayList<>();
            BigDecimal totalLeftSum = BigDecimal.ZERO;

            String sql = "{call satuan_p_warning_overdue_receivable(?, ?, ?)}";

            Connection conn = null;
            CallableStatement cs = null;
            ResultSet rs = null;
            try {
                conn = jdbcTemplate.getDataSource().getConnection();
                cs = conn.prepareCall(sql);
                cs.setString(1, handlerName);
                cs.setInt(3, isSummary);
                cs.registerOutParameter(2, Types.DECIMAL);
                cs.execute();

                rs = cs.getResultSet();
                if (isSummary == 1) {
                    // 汇总模式：超期时间分类、总应收款、单据数量、平均超期成本占比 (4列)
                    while (rs.next()) {
                        Map<String, Object> row = new HashMap<>();
                        row.put("超期时间分类", rs.getString(1));
                        row.put("总应收款", rs.getBigDecimal(2));
                        row.put("单据数量", rs.getInt(3));
                        row.put("平均超期成本占比", rs.getBigDecimal(4));
                        dataList.add(row);
                    }
                } else {
                    // 明细模式：操作人、开票日期、开票客户、开票金额、应收款、应收款占比、开票单号、发票号、业务员、备注、超期总出库成本、超期成本占比、超期时间分类 (13列)
                    while (rs.next()) {
                        Map<String, Object> row = new HashMap<>();
                        row.put("操作人", rs.getString(1));
                        row.put("开票日期", rs.getString(2));
                        row.put("开票客户", rs.getString(3));
                        row.put("开票金额", rs.getBigDecimal(4));
                        row.put("应收款", rs.getBigDecimal(5));
                        row.put("应收款占比", rs.getBigDecimal(6));
                        row.put("开票单号", rs.getString(7));
                        row.put("发票号", rs.getString(8));
                        row.put("业务员", rs.getString(9));
                        row.put("备注", rs.getString(10));
                        row.put("超期总出库成本", rs.getBigDecimal(11));
                        row.put("超期成本占比", rs.getBigDecimal(12));
                        row.put("超期时间分类", rs.getString(13));
                        dataList.add(row);
                    }
                }
                rs.close();

                // 必须先消耗完所有 ResultSet 才能获取 OUTPUT 参数
                while (cs.getMoreResults()) {
                    ResultSet extraRs = cs.getResultSet();
                    if (extraRs != null) {
                        while (extraRs.next()) { }
                        extraRs.close();
                    }
                }
                totalLeftSum = cs.getBigDecimal(2);  // 参数2: totalLeftSum OUTPUT
                if (totalLeftSum == null) {
                    totalLeftSum = BigDecimal.ZERO;
                }
            } finally {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (conn != null) conn.close();
            }

            result.put("data", dataList);
            result.put("totalLeftSum", totalLeftSum);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("satuan_p_warning_overdue_receivable执行失败: " + e.getMessage(), e);
        }
    }

    /**
     * 超期货出票未开预警
     * @param userName 业务员用户名，'ALL'或null表示查询所有
     * @return Map包含 data列表 和 totalStockCost汇总值
     */
    public Map<String, Object> get超期货出票未开预警(String userName) {
        if (!configured || jdbcTemplate == null) {
            throw new RuntimeException("SQL Server未配置或配置不完整");
        }
        try {
            Map<String, Object> result = new HashMap<>();
            List<Map<String, Object>> dataList = new ArrayList<>();
            BigDecimal totalStockCost = BigDecimal.ZERO;

            String sql = "{call satuan_p_warning_outstock_30days(?, ?, ?)}";

            Connection conn = null;
            CallableStatement cs = null;
            ResultSet rs = null;
            try {
                conn = jdbcTemplate.getDataSource().getConnection();
                cs = conn.prepareCall(sql);
                cs.setString(1, userName);
                cs.setInt(3, 0);
                cs.registerOutParameter(2, Types.DECIMAL);
                cs.execute();

                rs = cs.getResultSet();
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("SUB_ORG_ID", rs.getString(1));
                    row.put("操作人", rs.getString(2));
                    row.put("业务员", rs.getString(3));
                    row.put("业务部门", rs.getString(4));
                    row.put("出库单号", rs.getString(5));
                    row.put("出库客户", rs.getString(6));
                    row.put("出库客户信用账期", rs.getString(7));
                    row.put("批号", rs.getString(8));
                    row.put("货品名称", rs.getString(9));
                    row.put("规格型号", rs.getString(10));
                    row.put("含税成本", rs.getBigDecimal(11));
                    row.put("配置", rs.getString(12));
                    row.put("出库日期", rs.getString(13));
                    row.put("出库收款账期", rs.getString(14));
                    row.put("超期天数", rs.getInt(15));
                    row.put("超期时间分类", rs.getString(16));
                    dataList.add(row);
                }

                // 必须先消耗完所有 ResultSet 才能获取 OUTPUT 参数
                while (cs.getMoreResults()) { }
                totalStockCost = cs.getBigDecimal(2);  // 参数2: totalStockCost OUTPUT
                if (totalStockCost == null) {
                    totalStockCost = BigDecimal.ZERO;
                }
            } finally {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (conn != null) conn.close();
            }

            result.put("data", dataList);
            result.put("totalStockCost", totalStockCost);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("satuan_p_warning_outstock_30days执行失败: " + e.getMessage(), e);
        }
    }

    /**
     * 过账货品未出库预警
     */
    public List<Map<String, Object>> get过账货品未出库预警(Integer orgId) {
        if (!configured || jdbcTemplate == null) {
            throw new RuntimeException("SQL Server未配置或配置不完整");
        }
        try {
            String sql = "{call satuan_p_warning_post_no_outstock(?)}";
            return jdbcTemplate.queryForList(sql, orgId);
        } catch (Exception e) {
            throw new RuntimeException("satuan_p_warning_post_no_outstock执行失败: " + e.getMessage(), e);
        }
    }

    /**
     * 预付款货未到预警
     */
    public List<Map<String, Object>> get预付款货未到预警(String startDate, String endDate, Integer orgId) {
        if (!configured || jdbcTemplate == null) {
            throw new RuntimeException("SQL Server未配置或配置不完整");
        }
        try {
            String sql = "{call satuan_p_warning_prepay_no_goods(?, ?, ?)}";
            return jdbcTemplate.queryForList(sql, startDate, endDate, orgId);
        } catch (Exception e) {
            throw new RuntimeException("satuan_p_warning_prepay_no_goods执行失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取预警汇总数据（带缓存）
     * 优先从Redis缓存获取，当天有效
     * @param personName 业务员名称，'ALL'或null表示查询所有
     * @return Map包含三种预警的汇总金额及缓存信息
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getWarningSummary(String personName) {
        if (!configured || jdbcTemplate == null) {
            throw new RuntimeException("SQL Server未配置或配置不完整");
        }

        String cacheKey = getCacheKey();

        // 尝试从缓存获取
        if (redisUtils != null) {
            Object cached = redisUtils.get(cacheKey);
            if (cached != null) {
                Map<String, Object> result = (Map<String, Object>) cached;
                result.put("cached", true);
                return result;
            }
        }

        // 缓存未命中，查询数据库
        Map<String, Object> data = fetchWarningDataFromDB(personName);

        // 存入缓存
        if (redisUtils != null) {
            redisUtils.set(cacheKey, data, getExpireSeconds());
        }

        data.put("cached", false);
        return data;
    }

    /**
     * 强制刷新缓存
     */
    public Map<String, Object> refreshCache(String personName) {
        String cacheKey = getCacheKey();
        if (redisUtils != null) {
            redisUtils.del(cacheKey);
        }
        Map<String, Object> data = fetchWarningDataFromDB(personName);
        if (redisUtils != null) {
            redisUtils.set(cacheKey, data, getExpireSeconds());
        }
        data.put("cached", false);
        return data;
    }

    /**
     * 从数据库获取预警数据（并行执行）
     */
    private Map<String, Object> fetchWarningDataFromDB(String personName) {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        try {
            CompletableFuture<BigDecimal> f1 = CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return callOutstock30DaysProcedure(personName);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }, executor);
            CompletableFuture<BigDecimal> f2 = CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return callOverdueDepositProcedure();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }, executor);
            CompletableFuture<BigDecimal> f3 = CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return callOverdueReceivableProcedure(personName);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }, executor);

            CompletableFuture.allOf(f1, f2, f3).get(30, TimeUnit.SECONDS);

            Map<String, Object> result = new HashMap<>();
            result.put("overdueReceivable", f3.get());
            result.put("overdueDeposit", f2.get());
            result.put("outstockCost", f1.get());
            return result;
        } catch (Exception e) {
            throw new RuntimeException("获取预警汇总数据失败: " + e.getMessage(), e);
        } finally {
            executor.shutdown();
        }
    }

    /**
     * 调用存储过程并获取汇总 OUTPUT 参数
     * @param procedureName 存储过程名
     * @param param1 参数1（通常为 personName 或 "ALL"）
     * @param isSummary 是否汇总模式
     * @return 汇总金额
     */
    private BigDecimal callProcedureAndGetTotal(String procedureName, String param1, int isSummary) throws SQLException {
        BigDecimal totalAmount = BigDecimal.ZERO;
        Connection conn = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            conn = jdbcTemplate.getDataSource().getConnection();
            String sql = "{call " + procedureName + "(?, ?, ?)}";
            cs = conn.prepareCall(sql);
            // 设置输入参数
            cs.setString(1, param1);
            cs.setInt(3, isSummary);
            // 注册输出参数（参数2）
            cs.registerOutParameter(2, Types.DECIMAL);
            cs.execute();

            // 处理结果集（先消耗 ResultSet 再获取 OUTPUT 参数）
            rs = cs.getResultSet();
            if (rs != null) {
                // 消费结果集数据
                while (rs.next()) {
                    // 仅消费数据，不做处理
                }
                rs.close();
            }
            // 消耗可能的其他结果集
            while (cs.getMoreResults()) {
                ResultSet otherRs = cs.getResultSet();
                if (otherRs != null) {
                    while (otherRs.next()) {
                        // 仅消费数据
                    }
                    otherRs.close();
                }
            }

            // 获取 OUTPUT 参数（必须等所有 ResultSet 消耗完毕后）
            totalAmount = cs.getBigDecimal(2);
            if (totalAmount == null) {
                totalAmount = BigDecimal.ZERO;
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) try { rs.close(); } catch (Exception ignored) {}
            if (cs != null) try { cs.close(); } catch (Exception ignored) {}
            if (conn != null) try { conn.close(); } catch (Exception ignored) {}
        }
        return totalAmount;
    }

    /**
     * 调用超期货出票未开预警存储过程
     * 每个线程使用自己的数据库连接
     */
    private BigDecimal callOutstock30DaysProcedure(String personName) throws SQLException {
        return callProcedureAndGetTotal("satuan_p_warning_outstock_30days", personName, 1);
    }

    /**
     * 调用超期客户押金预警存储过程，汇总金额
     */
    private BigDecimal callOverdueDepositProcedure() throws SQLException {
        return callProcedureAndGetTotal("satuan_p_warning_overdue_deposit", "ALL", 1);
    }

    /**
     * 调用超期应收款预警存储过程，汇总金额
     */
    private BigDecimal callOverdueReceivableProcedure(String personName) throws SQLException {
        return callProcedureAndGetTotal("satuan_p_warning_overdue_receivable", personName, 1);
    }
}