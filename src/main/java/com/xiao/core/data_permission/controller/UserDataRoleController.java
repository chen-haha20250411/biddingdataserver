package com.xiao.core.data_permission.controller;

import com.xiao.base.ResultModel;
import com.xiao.core.basic.operator.domain.Operator;
import com.xiao.core.data_permission.domain.UserDataRole;
import com.xiao.core.data_permission.service.DataPermissionCheckService;
import com.xiao.core.data_permission.service.UserDataRoleService;
import com.xiao.logannotation.CurrentUser;
import com.xiao.logannotation.LoginRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/userDataRole")
public class UserDataRoleController {

    @Autowired
    private UserDataRoleService userDataRoleService;

    @Autowired
    private DataPermissionCheckService dataPermissionCheckService;

    @LoginRequired(remark="查询用户数据角色操作")
    @GetMapping("/list/{userId}")
    public ResultModel getRolesByUserId(@CurrentUser Operator currentUser, @PathVariable Integer userId) {
        try {
            if (!canOperateUser(currentUser, userId)) {
                return ResultModel.error("无权限操作该用户数据");
            }
            List<UserDataRole> userRoles = userDataRoleService.getRolesByUserId(userId);
            return ResultModel.success("查询成功", userRoles);
        } catch (Exception e) {
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }

    @LoginRequired(remark="分配用户数据角色操作")
    @PostMapping("/assign")
    public ResultModel assignRoleToUser(@CurrentUser Operator currentUser, @RequestBody Map<String, Integer> request) {
        try {
            Integer userId = request.get("userId");
            Integer roleId = request.get("roleId");

            if (userId == null || roleId == null) {
                return ResultModel.error("用户ID和角色ID不能为空");
            }

            if (!canOperateUser(currentUser, userId)) {
                return ResultModel.error("无权限操作该用户数据");
            }

            boolean success = userDataRoleService.assignRoleToUser(userId, roleId);
            if (success) {
                dataPermissionCheckService.clearUserPermissionCache(userId);
                return ResultModel.success("分配成功");
            } else {
                return ResultModel.error("分配失败");
            }
        } catch (Exception e) {
            return ResultModel.error("分配失败: " + e.getMessage());
        }
    }

    @LoginRequired(remark="撤销用户数据角色操作")
    @DeleteMapping("/revoke")
    public ResultModel revokeRoleFromUser(@CurrentUser Operator currentUser, @RequestBody Map<String, Integer> request) {
        try {
            Integer userId = request.get("userId");
            Integer roleId = request.get("roleId");

            if (userId == null || roleId == null) {
                return ResultModel.error("用户ID和角色ID不能为空");
            }

            if (!canOperateUser(currentUser, userId)) {
                return ResultModel.error("无权限操作该用户数据");
            }

            boolean success = userDataRoleService.revokeRoleFromUser(userId, roleId);
            if (success) {
                dataPermissionCheckService.clearUserPermissionCache(userId);
                return ResultModel.success("撤销成功");
            } else {
                return ResultModel.error("撤销失败");
            }
        } catch (Exception e) {
            return ResultModel.error("撤销失败: " + e.getMessage());
        }
    }

    @LoginRequired(remark="撤销用户所有数据角色操作")
    @DeleteMapping("/revokeAll/{userId}")
    public ResultModel revokeAllRolesFromUser(@CurrentUser Operator currentUser, @PathVariable Integer userId) {
        try {
            if (!canOperateUser(currentUser, userId)) {
                return ResultModel.error("无权限操作该用户数据");
            }

            boolean success = userDataRoleService.revokeAllRolesFromUser(userId);
            if (success) {
                dataPermissionCheckService.clearUserPermissionCache(userId);
                return ResultModel.success("撤销成功");
            } else {
                return ResultModel.error("撤销失败");
            }
        } catch (Exception e) {
            return ResultModel.error("撤销失败: " + e.getMessage());
        }
    }

    @LoginRequired(remark="批量分配用户数据角色操作")
    @PostMapping("/batchAssign")
    public ResultModel batchAssignRolesToUser(@CurrentUser Operator currentUser, @RequestBody Map<String, Object> request) {
        try {
            Integer userId = (Integer) request.get("userId");
            List<Integer> roleIds = (List<Integer>) request.get("roleIds");

            if (userId == null || roleIds == null || roleIds.isEmpty()) {
                return ResultModel.error("用户ID和角色ID列表不能为空");
            }

            if (!canOperateUser(currentUser, userId)) {
                return ResultModel.error("无权限操作该用户数据");
            }

            boolean success = userDataRoleService.batchAssignRolesToUser(userId, roleIds);
            if (success) {
                dataPermissionCheckService.clearUserPermissionCache(userId);
                return ResultModel.success("批量分配成功");
            } else {
                return ResultModel.error("批量分配失败");
            }
        } catch (Exception e) {
            return ResultModel.error("批量分配失败: " + e.getMessage());
        }
    }

    private boolean canOperateUser(Operator currentUser, Integer targetUserId) {
        if (currentUser == null || targetUserId == null) {
            return false;
        }
        if (currentUser.getRoleinfoId() == 1 || currentUser.getRoleinfoId() == 3) {
            return true;
        }
        return currentUser.getOperatorId().equals(targetUserId);
    }
}
