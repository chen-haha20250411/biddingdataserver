package com.xiao.core.data_permission.controller;

import com.xiao.base.ResultModel;
import com.xiao.core.basic.operator.domain.Operator;
import com.xiao.core.basic.operator.service.OperatorService;
import com.xiao.core.data_permission.domain.DataPermission;
import com.xiao.core.data_permission.service.DataPermissionCheckService;
import com.xiao.core.data_permission.service.DataPermissionService;
import com.xiao.core.data_permission.service.UserDataRoleService;
import com.xiao.logannotation.CurrentUser;
import com.xiao.logannotation.LoginRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dataPermission")
public class DataPermissionController {

    @Autowired
    private DataPermissionService dataPermissionService;

    @Autowired
    private UserDataRoleService userDataRoleService;

    @Autowired
    private DataPermissionCheckService dataPermissionCheckService;
    
    @Autowired
    private OperatorService operService;

    @LoginRequired(remark="查询指定角色有哪些数据权限操作")
    @GetMapping("/list/{roleId}")
    public ResultModel getPermissionsByRoleId(@CurrentUser Operator currentUser, @PathVariable Integer roleId) {
        try {
            List<DataPermission> permissions = dataPermissionService.getPermissionsByRoleId(roleId);
            return ResultModel.success("查询成功", permissions);
        } catch (Exception e) {
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }

    @LoginRequired(remark="给角色分配数据权限操作")
    @PostMapping("/assign")
    public ResultModel assignPermission(@CurrentUser Operator currentUser, @RequestBody DataPermission permission) {
        try {
            if (!isAdmin(currentUser)) {
                return ResultModel.error("无权限操作");
            }
            if (permission.getRoleId() == null) {
                return ResultModel.error("角色ID不能为空");
            }
            if (permission.getPermissionType() == null || permission.getPermissionType().trim().isEmpty()) {
                return ResultModel.error("权限类型不能为空");
            }
            if (permission.getPermissionValue() == null || permission.getPermissionValue().trim().isEmpty()) {
                return ResultModel.error("权限值不能为空");
            }

            boolean success = dataPermissionService.assignPermission(permission);
            if (success) {
                clearRoleUsersCache(permission.getRoleId());
                return ResultModel.success("分配成功", permission);
            } else {
                return ResultModel.error("分配失败");
            }
        } catch (Exception e) {
            return ResultModel.error("分配失败: " + e.getMessage());
        }
    }

    @LoginRequired(remark="更新角色数据权限操作")
    @PutMapping("/update")
    public ResultModel updatePermission(@CurrentUser Operator currentUser, @RequestBody DataPermission permission) {
        try {
            if (!isAdmin(currentUser)) {
                return ResultModel.error("无权限操作");
            }
            if (permission.getId() == null) {
                return ResultModel.error("权限ID不能为空");
            }

            boolean success = dataPermissionService.updatePermission(permission);
            if (success) {
                return ResultModel.success("更新成功");
            } else {
                return ResultModel.error("更新失败");
            }
        } catch (Exception e) {
            return ResultModel.error("更新失败: " + e.getMessage());
        }
    }

    @LoginRequired(remark="删除角色数据权限操作")
    @DeleteMapping("/delete/{id}")
    public ResultModel deletePermission(@CurrentUser Operator currentUser, @PathVariable Integer id) {
        try {
            if (!isAdmin(currentUser)) {
                return ResultModel.error("无权限操作");
            }
            boolean success = dataPermissionService.deletePermission(id);
            if (success) {
                return ResultModel.success("删除成功");
            } else {
                return ResultModel.error("删除失败");
            }
        } catch (Exception e) {
            return ResultModel.error("删除失败: " + e.getMessage());
        }
    }

    @LoginRequired(remark="检查用户是否有角色数据权限操作")
    @PostMapping("/check")
    public ResultModel checkPermission(@CurrentUser Operator currentUser, @RequestBody Map<String, Object> request) {
        try {
            Integer userId = (Integer) request.get("userId");
            String permissionType = (String) request.get("permissionType");
            String permissionValue = (String) request.get("permissionValue");

            if (userId == null || permissionType == null || permissionValue == null) {
                return ResultModel.error("参数不能为空");
            }

            boolean hasPermission = dataPermissionCheckService.checkPermission(userId, permissionType, permissionValue);
            return ResultModel.success("查询成功", hasPermission);
        } catch (Exception e) {
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }

    @LoginRequired(remark="获取用户角色数据权限值操作")
    @GetMapping("/userPermissions/{userId}/{permissionType}")
    public ResultModel getUserPermissionValues(@CurrentUser Operator currentUser, @PathVariable Integer userId, @PathVariable String permissionType) {
        try {
            if (!canOperateUser(currentUser, userId)) {
                return ResultModel.error("无权限操作该用户数据");
            }
            List<String> permissionValues = dataPermissionCheckService.getUserPermissionValues(userId, permissionType);
            return ResultModel.success("查询成功", permissionValues);
        } catch (Exception e) {
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }

    @LoginRequired(remark="清除用户角色数据权限缓存操作")
    @PostMapping("/clearCache/{userId}")
    public ResultModel clearUserPermissionCache(@CurrentUser Operator currentUser, @PathVariable Integer userId) {
        try {
            if (!canOperateUser(currentUser, userId)) {
                return ResultModel.error("无权限操作该用户数据");
            }
            dataPermissionCheckService.clearUserPermissionCache(userId);
            return ResultModel.success("缓存清除成功");
        } catch (Exception e) {
            return ResultModel.error("缓存清除失败: " + e.getMessage());
        }
    }

    private void clearRoleUsersCache(Integer roleId) {
        try {
            List<com.xiao.core.data_permission.domain.UserDataRole> userRoles = userDataRoleService.getUsersByRoleId(roleId);
            if (userRoles != null && !userRoles.isEmpty()) {
                for (com.xiao.core.data_permission.domain.UserDataRole userRole : userRoles) {
                    dataPermissionCheckService.clearUserPermissionCache(userRole.getUserId());
                }
            }
        } catch (Exception e) {
            System.err.println("清除角色用户缓存失败: " + e.getMessage());
        }
    }

    private boolean isAdmin(Operator currentUser) {
        if (currentUser == null) {
            return false;
        }
        return currentUser.getRoleinfoId() == 1 || currentUser.getRoleinfoId() == 3;
    }

    private boolean canOperateUser(Operator currentUser, Integer targetUserId) {
        if (currentUser == null || targetUserId == null) {
            return false;
        }
        if (isAdmin(currentUser)) {
            return true;
        }
        return currentUser.getOperatorId().equals(targetUserId);
    }
    
    @LoginRequired(remark="获取全部用户信息操作")
    @GetMapping("/allUsers")
    public ResultModel getAllUsers(@CurrentUser Operator currentUser) {
        try {
            if (!isAdmin(currentUser)) {
                return ResultModel.error("无权限操作");
            }
            
            // 获取全部用户信息
            List<Operator> users = operService.queryByAll();
            if (users != null && !users.isEmpty()) {
                // 转换为包含realName的DTO列表
                List<Map<String, Object>> userList = new ArrayList<>();
                for (Operator user : users) {
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("userId", user.getOperatorId());
                    userMap.put("realName", user.getRealName());
                    userMap.put("loginName", user.getLoginName());
                    userList.add(userMap);
                }
                return ResultModel.success("查询成功", userList);
            }
            return ResultModel.success("查询成功", new ArrayList<>());
        } catch (Exception e) {
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }

    @LoginRequired(remark="获取所有权限类型操作")
    @GetMapping("/permissionTypes")
    public ResultModel getPermissionTypes(@CurrentUser Operator currentUser) {
        try {
            // 这里可以从数据库或配置中获取所有权限类型
            // 暂时返回一些常见的权限类型作为示例
            List<String> permissionTypes = new ArrayList<>();
            permissionTypes.add("department");
            permissionTypes.add("project");
            permissionTypes.add("region");
            permissionTypes.add("status");
            return ResultModel.success("查询成功", permissionTypes);
        } catch (Exception e) {
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }

    @LoginRequired(remark="批量分配权限操作")
    @PostMapping("/batchAssign")
    public ResultModel batchAssignPermission(@CurrentUser Operator currentUser, @RequestBody Map<String, Object> request) {
        try {
            if (!isAdmin(currentUser)) {
                return ResultModel.error("无权限操作");
            }
            
            Integer roleId = (Integer) request.get("roleId");
            List<DataPermission> permissions = (List<DataPermission>) request.get("permissions");
            
            if (roleId == null || permissions == null || permissions.isEmpty()) {
                return ResultModel.error("参数不能为空");
            }
            
            // 先删除该角色的所有权限
            dataPermissionService.deletePermissionsByRoleId(roleId);
            
            // 批量分配新权限
            boolean allSuccess = true;
            for (DataPermission permission : permissions) {
                permission.setRoleId(roleId);
                if (!dataPermissionService.assignPermission(permission)) {
                    allSuccess = false;
                }
            }
            
            if (allSuccess) {
                clearRoleUsersCache(roleId);
                return ResultModel.success("批量分配成功");
            } else {
                return ResultModel.error("部分权限分配失败");
            }
        } catch (Exception e) {
            return ResultModel.error("批量分配失败: " + e.getMessage());
        }
    }

    @LoginRequired(remark="批量删除权限操作")
    @DeleteMapping("/batchDelete")
    public ResultModel batchDeletePermission(@CurrentUser Operator currentUser, @RequestBody List<Integer> ids) {
        try {
            if (!isAdmin(currentUser)) {
                return ResultModel.error("无权限操作");
            }
            
            if (ids == null || ids.isEmpty()) {
                return ResultModel.error("参数不能为空");
            }
            
            boolean allSuccess = true;
            for (Integer id : ids) {
                if (!dataPermissionService.deletePermission(id)) {
                    allSuccess = false;
                }
            }
            
            if (allSuccess) {
                return ResultModel.success("批量删除成功");
            } else {
                return ResultModel.error("部分权限删除失败");
            }
        } catch (Exception e) {
            return ResultModel.error("批量删除失败: " + e.getMessage());
        }
    }

    @LoginRequired(remark="获取权限详情操作")
    @GetMapping("/detail/{id}")
    public ResultModel getPermissionDetail(@CurrentUser Operator currentUser, @PathVariable Integer id) {
        try {
            DataPermission permission = dataPermissionService.queryById(String.valueOf(id));
            if (permission != null) {
                return ResultModel.success("查询成功", permission);
            } else {
                return ResultModel.error("权限不存在");
            }
        } catch (Exception e) {
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }

    @LoginRequired(remark="检查角色是否有特定权限操作")
    @PostMapping("/checkRole")
    public ResultModel checkRolePermission(@CurrentUser Operator currentUser, @RequestBody Map<String, Object> request) {
        try {
            Integer roleId = (Integer) request.get("roleId");
            String permissionType = (String) request.get("permissionType");
            String permissionValue = (String) request.get("permissionValue");
            
            if (roleId == null || permissionType == null || permissionValue == null) {
                return ResultModel.error("参数不能为空");
            }
            
            List<DataPermission> permissions = dataPermissionService.getPermissionsByRoleId(roleId);
            boolean hasPermission = false;
            for (DataPermission permission : permissions) {
                if (permission.getPermissionType().equals(permissionType) && 
                    permission.getPermissionValue().contains(permissionValue)) {
                    hasPermission = true;
                    break;
                }
            }
            
            return ResultModel.success("查询成功", hasPermission);
        } catch (Exception e) {
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }
}
