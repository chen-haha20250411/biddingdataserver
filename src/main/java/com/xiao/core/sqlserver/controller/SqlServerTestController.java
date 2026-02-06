package com.xiao.core.sqlserver.controller;

import com.xiao.base.ResultModel;
import com.xiao.core.sqlserver.service.SqlServerTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sqlserver/test")
public class SqlServerTestController {

    @Autowired
    private SqlServerTestService sqlServerTestService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @GetMapping("/connection")
    public Map<String, Object> testConnection() {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean connected = sqlServerTestService.testConnection();
            result.put("success", connected);
            result.put("message", connected ? "SQL Server 连接成功" : "SQL Server 连接失败");
            result.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "SQL Server 连接异常: " + e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
        }
        return result;
    }

    @GetMapping("/query")
    public Map<String, Object> testQuery() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> data = sqlServerTestService.testQuery();
            result.put("success", true);
            result.put("message", "查询成功");
            result.put("data", data);
            result.put("count", data.size());
            result.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败: " + e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
        }
        return result;
    }

    @PostMapping("/execute")
    public Map<String, Object> executeCustomQuery(@RequestBody Map<String, String> request) {
        Map<String, Object> result = new HashMap<>();
        String sql = request.get("sql");
        if (sql == null || sql.trim().isEmpty()) {
            result.put("success", false);
            result.put("message", "SQL 语句不能为空");
            result.put("timestamp", System.currentTimeMillis());
            return result;
        }

        try {
            List<Map<String, Object>> data = sqlServerTestService.executeCustomQuery(sql);
            result.put("success", true);
            result.put("message", "SQL 执行成功");
            result.put("data", data);
            result.put("count", data.size());
            result.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "SQL 执行失败: " + e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
        }
        return result;
    }

    @GetMapping("/salesProfitReport")
    public ResultModel getSalesProfitReport(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(required = false, defaultValue = "") String staffName) {

        LocalDate actualStartDate = startDate != null ? startDate : LocalDate.now().withDayOfMonth(1);
        LocalDate actualEndDate = endDate != null ? endDate : LocalDate.now();

        String formattedStartDate = actualStartDate.atStartOfDay().format(DATE_FORMATTER);
        String formattedEndDate = actualEndDate.atTime(LocalTime.of(23, 59, 59)).format(DATE_FORMATTER);

        String actualStaffName = (staffName == null || staffName.trim().isEmpty()) ? "" : staffName.trim();

        try {
            List<Map<String, Object>> data = sqlServerTestService.callSalesProfitReportProcedure(
                    formattedStartDate, formattedEndDate, actualStaffName);
            return ResultModel.success("查询成功", data);
        } catch (Exception e) {
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }
}
