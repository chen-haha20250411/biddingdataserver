package com.xiao.core.sqlserver.controller;

import com.xiao.core.sqlserver.service.SqlServerTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sqlserver/test")
public class SqlServerTestController {

    @Autowired
    private SqlServerTestService sqlServerTestService;

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
}
