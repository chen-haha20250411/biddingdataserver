package com.xiao.core.sqlserver.controller;

import com.xiao.base.ResultModel;
import com.xiao.core.basic.user_data_permission.service.UserDataPermissionService;
import com.xiao.core.data_permission.constants.PermissionTypeConstants;
import com.xiao.core.data_permission.service.DataPermissionCheckService;
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

    @Autowired
    private UserDataPermissionService userDataPermissionService;

    @Autowired
    private DataPermissionCheckService dataPermissionCheckService;

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
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Integer sourceUserId,
            @RequestParam(required = false) Integer targetUserId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(required = false, defaultValue = "") String staffName,
            @RequestParam(required = false, defaultValue = "") String parentDepartment,
            @RequestParam(required = false, defaultValue = "") String department,
           
            @RequestParam(required = false, defaultValue = "") String businessLine,
            @RequestParam(required = false, defaultValue = "") String branch) {

        // 新的数据权限检查（使用userId参数）
        if (userId != null) {
            // 检查用户是否有访问所有数据的权限
            if (dataPermissionCheckService.hasAllPermission(userId)) {
                // 用户有访问所有数据的权限，跳过权限检查
            } else {
                // 检查用户是否有访问特定部门、分支机构、员工的权限
                if (!parentDepartment.isEmpty()) {
                    List<Integer> userDeptIds = dataPermissionCheckService.getUserDepartmentIds(userId);
                    // 这里可以添加更详细的权限检查逻辑
                }
                if (!branch.isEmpty()) {
                    List<Integer> userBranchIds = dataPermissionCheckService.getUserBranchIds(userId);
                    // 这里可以添加更详细的权限检查逻辑
                }
                if (!staffName.isEmpty()) {
                    List<Integer> userEmployeeIds = dataPermissionCheckService.getUserEmployeeIds(userId);
                    // 这里可以添加更详细的权限检查逻辑
                }
            }
        }

        // 兼容旧的权限检查（只有当sourceUserId和targetUserId都存在时才进行权限检查）
        if (sourceUserId != null && targetUserId != null) {
            boolean hasPermission = userDataPermissionService.checkUserDataPermission(sourceUserId, targetUserId, "SALES_PROFIT_REPORT");
            if (!hasPermission) {
                return ResultModel.error("无权限访问目标用户的数据");
            }
        }

        LocalDate actualStartDate = startDate != null ? startDate : LocalDate.now().withDayOfMonth(1);
        LocalDate actualEndDate = endDate != null ? endDate : LocalDate.now();

        String formattedStartDate = actualStartDate.atStartOfDay().format(DATE_FORMATTER);
        String formattedEndDate = actualEndDate.atTime(LocalTime.of(23, 59, 59)).format(DATE_FORMATTER);

        String actualStaffName = (staffName == null || staffName.trim().isEmpty()) ? "" : staffName.trim();
        String actualDepartment = (department == null || department.trim().isEmpty()) ? "" : department.trim();
        String actualParentDepartment = (parentDepartment == null || parentDepartment.trim().isEmpty()) ? "" : parentDepartment.trim();
        String actualBusinessLine = (businessLine == null || businessLine.trim().isEmpty()) ? "" : businessLine.trim();
        String actualBranch = (branch == null || branch.trim().isEmpty()) ? "" : branch.trim();

        try {
            List<Map<String, Object>> data = sqlServerTestService.callSalesProfitReportProcedure(
                    formattedStartDate, formattedEndDate, actualStaffName, 
                    actualParentDepartment, actualDepartment, actualBusinessLine, actualBranch);
            return ResultModel.success("查询成功", data);
        } catch (Exception e) {
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/getSalesTarget")
    public ResultModel getSalesTarget(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Integer sourceUserId,
            @RequestParam(required = false) Integer targetUserId,
            @RequestParam(required = false, defaultValue = "") Integer year,
            @RequestParam(required = false, defaultValue = "PERSON") String groupName,
            @RequestParam(required = false) String groupCondition) {

        // 新的数据权限检查（使用userId参数）
        if (userId != null) {
            // 检查用户是否有访问所有数据的权限
            if (dataPermissionCheckService.hasAllPermission(userId)) {
                // 用户有访问所有数据的权限，跳过权限检查
            } else {
                // 检查用户是否有访问特定部门、分支机构的权限
                List<Integer> userDeptIds = dataPermissionCheckService.getUserDepartmentIds(userId);
                List<Integer> userBranchIds = dataPermissionCheckService.getUserBranchIds(userId);
                // 这里可以添加更详细的权限检查逻辑
            }
        }

        // 兼容旧的权限检查（只有当sourceUserId和targetUserId都存在时才进行权限检查）
        if (sourceUserId != null && targetUserId != null) {
            boolean hasPermission = userDataPermissionService.checkUserDataPermission(sourceUserId, targetUserId, "SALES_TARGET");
            if (!hasPermission) {
                return ResultModel.error("无权限访问目标用户的数据");
            }
        }

        int actualYear = year != null ? year : LocalDate.now().getYear();
        String actualGroupName = (groupName == null || groupName.trim().isEmpty()) ? "PERSON" : groupName.trim();
        String actualGroupCondition = groupCondition;

        try {
            List<Map<String, Object>> data = sqlServerTestService.callSalesTargetProcedure(
                    actualYear, actualGroupName, actualGroupCondition);
            return ResultModel.success("查询成功", data);
        } catch (Exception e) {
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/getValueAddedBusiness")
    public ResultModel getValueAddedBusiness(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Integer sourceUserId,
            @RequestParam(required = false) Integer targetUserId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(required = false, defaultValue = "") String personNameList,
            @RequestParam(required = false, defaultValue = "") String deptGroupList,
            @RequestParam(required = false, defaultValue = "") String orgNameList,
            @RequestParam(required = false, defaultValue = "") String subOrgIdList,
            @RequestParam(required = false, defaultValue = "") String sGroupList) {

        // 新的数据权限检查（使用userId参数）
        if (userId != null) {
            // 检查用户是否有访问所有数据的权限
            if (dataPermissionCheckService.hasAllPermission(userId)) {
                // 用户有访问所有数据的权限，跳过权限检查
            } else {
                // 检查用户是否有访问特定部门、分支机构的权限
                List<Integer> userDeptIds = dataPermissionCheckService.getUserDepartmentIds(userId);
                List<Integer> userBranchIds = dataPermissionCheckService.getUserBranchIds(userId);
                // 这里可以添加更详细的权限检查逻辑
            }
        }

        // 兼容旧的权限检查（只有当sourceUserId和targetUserId都存在时才进行权限检查）
        if (sourceUserId != null && targetUserId != null) {
            boolean hasPermission = userDataPermissionService.checkUserDataPermission(sourceUserId, targetUserId, "VALUE_ADDED_BUSINESS");
            if (!hasPermission) {
                return ResultModel.error("无权限访问目标用户的数据");
            }
        }

        LocalDate actualStartDate = startDate != null ? startDate : LocalDate.now().withDayOfMonth(1);
        LocalDate actualEndDate = endDate != null ? endDate : LocalDate.now();

        String formattedStartDate = actualStartDate.atStartOfDay().format(DATE_FORMATTER);
        String formattedEndDate = actualEndDate.atTime(LocalTime.of(23, 59, 59)).format(DATE_FORMATTER);

        String actualPersonNameList = (personNameList == null || personNameList.trim().isEmpty()) ? "" : personNameList.trim();
        String actualDeptGroupList = (deptGroupList == null || deptGroupList.trim().isEmpty()) ? "" : deptGroupList.trim();
        String actualOrgNameList = (orgNameList == null || orgNameList.trim().isEmpty()) ? "" : orgNameList.trim();
        String actualSubOrgIdList = (subOrgIdList == null || subOrgIdList.trim().isEmpty()) ? "" : subOrgIdList.trim();
        String actualSGroupList = (sGroupList == null || sGroupList.trim().isEmpty()) ? "" : sGroupList.trim();

        try {
            List<Map<String, Object>> data = sqlServerTestService.callValueAddedBusinessProcedure(
                    formattedStartDate, formattedEndDate, actualPersonNameList, 
                    actualDeptGroupList, actualOrgNameList, actualSubOrgIdList, actualSGroupList);
            return ResultModel.success("查询成功", data);
        } catch (Exception e) {
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }
}
