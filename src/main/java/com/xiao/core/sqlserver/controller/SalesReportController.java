package com.xiao.core.sqlserver.controller;

import com.xiao.base.ResultModel;
import com.xiao.core.basic.operator.domain.Operator;
import com.xiao.core.basic.user_data_permission.service.UserDataPermissionService;
import com.xiao.core.data_permission.service.DataPermissionCheckService;
import com.xiao.core.sqlserver.service.SalesReportService;
import com.xiao.logannotation.CurrentUser;
import com.xiao.logannotation.LoginRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sales/report")
public class SalesReportController {

    @Autowired
    private SalesReportService salesReportService;

    @Autowired
    private UserDataPermissionService userDataPermissionService;

    @Autowired
    private DataPermissionCheckService dataPermissionCheckService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @LoginRequired(remark="查询销售利润报表")
    @GetMapping("/salesProfitReport")
    public ResultModel getSalesProfitReport(@CurrentUser Operator oper,
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

        if (userId != null) {
            if (!dataPermissionCheckService.hasAllPermission(userId)) {
                if (!parentDepartment.isEmpty()) {
                    dataPermissionCheckService.getUserDepartmentIds(userId);
                }
                if (!branch.isEmpty()) {
                    dataPermissionCheckService.getUserBranchIds(userId);
                }
                if (!staffName.isEmpty()) {
                    dataPermissionCheckService.getUserEmployeeIds(userId);
                }
            }
        }

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
            List<Map<String, Object>> data = salesReportService.callSalesProfitReportProcedure(
                    formattedStartDate, formattedEndDate, actualStaffName,
                    actualParentDepartment, actualDepartment, actualBusinessLine, actualBranch);
            return ResultModel.success("查询成功", data);
        } catch (Exception e) {
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }

    @LoginRequired(remark="查询销售目标")
    @GetMapping("/getSalesTarget")
    public ResultModel getSalesTarget(@CurrentUser Operator oper,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Integer sourceUserId,
            @RequestParam(required = false) Integer targetUserId,
            @RequestParam(required = false, defaultValue = "") Integer year,
            @RequestParam(required = false, defaultValue = "PERSON") String groupName,
            @RequestParam(required = false) String groupCondition) {

        if (userId != null) {
            if (!dataPermissionCheckService.hasAllPermission(userId)) {
                dataPermissionCheckService.getUserDepartmentIds(userId);
                dataPermissionCheckService.getUserBranchIds(userId);
            }
        }

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
            List<Map<String, Object>> data = salesReportService.callSalesTargetProcedure(
                    actualYear, actualGroupName, actualGroupCondition);
            return ResultModel.success("查询成功", data);
        } catch (Exception e) {
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }

    @LoginRequired(remark="查询增值业务")
    @GetMapping("/getValueAddedBusiness")
    public ResultModel getValueAddedBusiness(@CurrentUser Operator oper,
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

        if (userId != null) {
            if (!dataPermissionCheckService.hasAllPermission(userId)) {
                dataPermissionCheckService.getUserDepartmentIds(userId);
                dataPermissionCheckService.getUserBranchIds(userId);
            }
        }

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
            List<Map<String, Object>> data = salesReportService.callValueAddedBusinessProcedure(
                    formattedStartDate, formattedEndDate, actualPersonNameList,
                    actualDeptGroupList, actualOrgNameList, actualSubOrgIdList, actualSGroupList);
            return ResultModel.success("查询成功", data);
        } catch (Exception e) {
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }
}