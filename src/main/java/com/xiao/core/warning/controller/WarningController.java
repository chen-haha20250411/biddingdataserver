package com.xiao.core.warning.controller;

import com.xiao.base.ResultModel;
import com.xiao.core.basic.operator.domain.Operator;
import com.xiao.core.data_permission.service.DataPermissionCheckService;
import com.xiao.core.warning.service.WarningService;
import com.xiao.logannotation.CurrentUser;
import com.xiao.logannotation.LoginRequired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/warning")
public class WarningController {
    private static final Logger log = LoggerFactory.getLogger(WarningController.class);

    @Autowired
    private WarningService warningService;

    @Autowired
    private DataPermissionCheckService dataPermissionCheckService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 获取当前用户可访问的员工业员名列表
     * @param oper 当前登录用户
     * @return 员工名称列表（用于存储过程查询），"ALL"表示全部权限
     */
    private String resolvePersonName(Operator oper) {
        if (oper == null) {
            return "ALL";
        }
        // 管理员拥有所有权限
        if (dataPermissionCheckService.hasAllPermission(oper.getOperatorId())) {
            return "ALL";
        }
        // 获取当前用户可访问的员工ID列表
        List<Integer> employeeIds = dataPermissionCheckService.getUserEmployeeIds(oper.getOperatorId());
        if (employeeIds == null || employeeIds.isEmpty()) {
            // 无权限，返回空字符串（会导致存储过程返回空结果）
            return "";
        }
        // 多个员工用逗号分隔，存储过程通过 COM_F_Split 解析
        return String.join(",", employeeIds.stream().map(String::valueOf).collect(java.util.stream.Collectors.toList()));
    }

    /**
     * 出库超期未收款预警
     * @param userName 业务员用户名，'ALL'或null表示查询所有
     */
    @LoginRequired(remark="出库超期预警")
    @GetMapping("/出库超30天")
    public ResultModel get出库超30天预警(
            @CurrentUser Operator oper,
            @RequestParam(required = false, defaultValue = "ALL") String userName) {
        try {
            // 数据权限控制：优先使用用户权限过滤
            String actualUserName = "ALL".equalsIgnoreCase(userName) ? resolvePersonName(oper) : userName;
            Map<String, Object> result = warningService.get出库超30天预警(actualUserName);
            return ResultModel.success("查询成功", result);
        } catch (Exception e) {
            log.error("出库超30天预警查询失败", e);
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 已付款未到票预警
     */
    @LoginRequired(remark="已付款未到票预警")
    @GetMapping("/已付款未到票")
    public ResultModel get已付款未到票预警(
            @RequestParam(required = false) Integer orgId) {
        try {
            List<Map<String, Object>> data = warningService.get已付款未到票预警(orgId);
            return ResultModel.success("查询成功", data);
        } catch (Exception e) {
            log.error("已付款未到票预警查询失败", e);
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 超期客户押金预警
     * @param userName 业务员用户名，'ALL'或null表示查询所有
     * @param isSummary 是否汇总模式，0=明细, 1=汇总
     */
    @LoginRequired(remark="超期客户押金预警")
    @GetMapping("/超期客户押金")
    public ResultModel get超期客户押金预警(
            @CurrentUser Operator oper,
            @RequestParam(required = false, defaultValue = "ALL") String userName,
            @RequestParam(required = false, defaultValue = "0") Integer isSummary) {
        try {
            // 数据权限控制：优先使用用户权限过滤
            String actualUserName = "ALL".equalsIgnoreCase(userName) ? resolvePersonName(oper) : userName;
            Map<String, Object> data = warningService.get超期客户押金预警(actualUserName, isSummary);
            return ResultModel.success("查询成功", data);
        } catch (Exception e) {
            log.error("超期客户押金预警查询失败", e);
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 超期应收款预警
     * @param userName 业务员用户名，'ALL'或null表示查询所有
     * @param isSummary 是否汇总模式，0=明细, 1=汇总
     */
    @LoginRequired(remark="超期应收款预警")
    @GetMapping("/超期应收款")
    public ResultModel get超期应收款预警(
            @CurrentUser Operator oper,
            @RequestParam(required = false, defaultValue = "ALL") String userName,
            @RequestParam(required = false, defaultValue = "0") Integer isSummary) {
        try {
            // 数据权限控制：优先使用用户权限过滤
            String actualUserName = "ALL".equalsIgnoreCase(userName) ? resolvePersonName(oper) : userName;
            Map<String, Object> data = warningService.get超期应收款预警(actualUserName, isSummary);
            return ResultModel.success("查询成功", data);
        } catch (Exception e) {
            log.error("超期应收款预警查询失败", e);
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 超期货出票未开预警
     * @param userName 业务员用户名，'ALL'或null表示查询所有
     */
    @LoginRequired(remark="超期货出票未开预警")
    @GetMapping("/超期货出票未开")
    public ResultModel get超期货出票未开预警(
            @CurrentUser Operator oper,
            @RequestParam(required = false, defaultValue = "ALL") String userName) {
        try {
            // 数据权限控制：优先使用用户权限过滤
            String actualUserName = "ALL".equalsIgnoreCase(userName) ? resolvePersonName(oper) : userName;
            Map<String, Object> data = warningService.get超期货出票未开预警(actualUserName);
            return ResultModel.success("查询成功", data);
        } catch (Exception e) {
            log.error("超期货出票未开预警查询失败", e);
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 过账货品未出库预警
     */
    @LoginRequired(remark="过账货品未出库预警")
    @GetMapping("/过账货品未出库")
    public ResultModel get过账货品未出库预警(
            @RequestParam(required = false) Integer orgId) {
        try {
            List<Map<String, Object>> data = warningService.get过账货品未出库预警(orgId);
            return ResultModel.success("查询成功", data);
        } catch (Exception e) {
            log.error("过账货品未出库预警查询失败", e);
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 预付款货未到预警
     */
    @LoginRequired(remark="预付款货未到预警")
    @GetMapping("/预付款货未到")
    public ResultModel get预付款货未到预警(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(required = false) Integer orgId) {
        try {
            String start = startDate != null ? startDate.format(DATE_FORMATTER) : null;
            String end = endDate != null ? endDate.format(DATE_FORMATTER) : null;
            List<Map<String, Object>> data = warningService.get预付款货未到预警(start, end, orgId);
            return ResultModel.success("查询成功", data);
        } catch (Exception e) {
            log.error("预付款货未到预警查询失败", e);
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取预警汇总数据
     * 从Redis缓存获取，当天有效
     */
    @LoginRequired(remark="预警汇总数据")
    @GetMapping("/summary")
    public ResultModel getWarningSummary(
            @CurrentUser Operator oper,
            @RequestParam(required = false, defaultValue = "ALL") String personName) {
        try {
            // 数据权限控制：优先使用用户权限过滤
            String actualPersonName = "ALL".equalsIgnoreCase(personName) ? resolvePersonName(oper) : personName;
            Map<String, Object> data = warningService.getWarningSummary(actualPersonName);
            return ResultModel.success("查询成功", data);
        } catch (Exception e) {
            log.error("预警汇总数据查询失败", e);
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 强制刷新预警汇总缓存
     */
    @LoginRequired(remark="刷新预警汇总缓存")
    @PostMapping("/refresh")
    public ResultModel refreshWarningCache(
            @CurrentUser Operator oper,
            @RequestParam(required = false, defaultValue = "ALL") String personName) {
        try {
            // 数据权限控制：优先使用用户权限过滤
            String actualPersonName = "ALL".equalsIgnoreCase(personName) ? resolvePersonName(oper) : personName;
            Map<String, Object> data = warningService.refreshCache(actualPersonName);
            return ResultModel.success("刷新成功", data);
        } catch (Exception e) {
            log.error("刷新预警汇总缓存失败", e);
            return ResultModel.error("刷新失败: " + e.getMessage());
        }
    }
}
