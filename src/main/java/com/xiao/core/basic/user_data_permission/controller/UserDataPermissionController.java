package com.xiao.core.basic.user_data_permission.controller;

import com.xiao.base.ResultModel;
import com.xiao.core.basic.user_data_permission.domain.UserDataPermission;
import com.xiao.core.basic.user_data_permission.service.UserDataPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/userDataPermission")
public class UserDataPermissionController {

    @Autowired
    private UserDataPermissionService userDataPermissionService;

    /**
     * 获取用户的数据访问权限列表
     * @param userId 用户ID
     * @return 权限列表
     */
    @GetMapping("/getUserDataPermissions")
    public ResultModel getUserDataPermissions(@RequestParam Integer userId) {
        try {
            List<UserDataPermission> permissions = userDataPermissionService.getUserDataPermissions(userId);
            return ResultModel.success("获取成功", permissions);
        } catch (Exception e) {
            return ResultModel.error("获取失败: " + e.getMessage());
        }
    }

    /**
     * 授予数据访问权限
     * @param request 请求参数
     * @return 操作结果
     */
    @PostMapping("/grantUserDataPermission")
    public ResultModel grantUserDataPermission(@RequestBody Map<String, Object> request) {
        try {
            Integer sourceUserId = (Integer) request.get("sourceUserId");
            Integer targetUserId = (Integer) request.get("targetUserId");
            String permissionType = (String) request.get("permissionType");

            if (sourceUserId == null || targetUserId == null || permissionType == null) {
                return ResultModel.error("参数不能为空");
            }

            boolean success = userDataPermissionService.grantUserDataPermission(sourceUserId, targetUserId, permissionType);
            if (success) {
                return ResultModel.success("授予成功");
            } else {
                return ResultModel.error("授予失败，权限已存在");
            }
        } catch (Exception e) {
            return ResultModel.error("授予失败: " + e.getMessage());
        }
    }

    /**
     * 撤销数据访问权限
     * @param request 请求参数
     * @return 操作结果
     */
    @PostMapping("/revokeUserDataPermission")
    public ResultModel revokeUserDataPermission(@RequestBody Map<String, Object> request) {
        try {
            Integer sourceUserId = (Integer) request.get("sourceUserId");
            Integer targetUserId = (Integer) request.get("targetUserId");
            String permissionType = (String) request.get("permissionType");

            if (sourceUserId == null || targetUserId == null || permissionType == null) {
                return ResultModel.error("参数不能为空");
            }

            boolean success = userDataPermissionService.revokeUserDataPermission(sourceUserId, targetUserId, permissionType);
            if (success) {
                return ResultModel.success("撤销成功");
            } else {
                return ResultModel.error("撤销失败");
            }
        } catch (Exception e) {
            return ResultModel.error("撤销失败: " + e.getMessage());
        }
    }
}
