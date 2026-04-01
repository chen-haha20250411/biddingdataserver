package com.xiao.core.data_permission.controller;

import com.xiao.base.ResultModel;
import com.xiao.core.basic.operator.domain.Operator;
import com.xiao.core.data_permission.domain.DataRole;
import com.xiao.core.data_permission.service.DataRoleService;
import com.xiao.logannotation.CurrentUser;
import com.xiao.logannotation.LoginRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dataRole")
public class DataRoleController {

    @Autowired
    private DataRoleService dataRoleService;

    @LoginRequired(remark="查询所有数据角色操作")
    @GetMapping("/list")
    public ResultModel getAllRoles(@CurrentUser Operator currentUser) {
        try {
            List<DataRole> roles = dataRoleService.getAllRoles();
            return ResultModel.success("查询成功", roles);
        } catch (Exception e) {
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }

    @LoginRequired(remark="查询指定数据角色详情操作")
    @GetMapping("/get/{id}")
    public ResultModel getRoleById(@CurrentUser Operator currentUser, @PathVariable Integer id) {
        try {
            DataRole role = dataRoleService.getRoleById(id);
            if (role == null) {
                return ResultModel.error("角色不存在");
            }
            return ResultModel.success("查询成功", role);
        } catch (Exception e) {
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }

    @LoginRequired(remark="创建新数据角色操作")
    @PostMapping("/create")
    public ResultModel createRole(@CurrentUser Operator currentUser, @RequestBody DataRole role) {
        try {
            if (!isAdmin(currentUser)) {
                return ResultModel.error("无权限操作");
            }
            if (role.getRoleName() == null || role.getRoleName().trim().isEmpty()) {
                return ResultModel.error("角色名称不能为空");
            }
            if (role.getRoleCode() == null || role.getRoleCode().trim().isEmpty()) {
                return ResultModel.error("角色编码不能为空");
            }

            DataRole existingRole = dataRoleService.getRoleByCode(role.getRoleCode());
            if (existingRole != null) {
                return ResultModel.error("角色编码已存在");
            }

            boolean success = dataRoleService.createRole(role);
            if (success) {
                return ResultModel.success("创建成功", role);
            } else {
                return ResultModel.error("创建失败");
            }
        } catch (Exception e) {
            return ResultModel.error("创建失败: " + e.getMessage());
        }
    }

    @LoginRequired(remark="更新指定数据角色基本信息操作不含具体权限配置")
    @PutMapping("/update")
    public ResultModel updateRole(@CurrentUser Operator currentUser, @RequestBody DataRole role) {
        try {
            if (!isAdmin(currentUser)) {
                return ResultModel.error("无权限操作");
            }
            if (role.getId() == null) {
                return ResultModel.error("角色ID不能为空");
            }

            DataRole existingRole = dataRoleService.getRoleById(role.getId());
            if (existingRole == null) {
                return ResultModel.error("角色不存在");
            }

            boolean success = dataRoleService.updateRole(role);
            if (success) {
                return ResultModel.success("更新成功");
            } else {
                return ResultModel.error("更新失败");
            }
        } catch (Exception e) {
            return ResultModel.error("更新失败: " + e.getMessage());
        }
    }

    @LoginRequired(remark="删除指定数据角色操作")
    @DeleteMapping("/delete/{id}")
    public ResultModel deleteRole(@CurrentUser Operator currentUser, @PathVariable Integer id) {
        try {
            if (!isAdmin(currentUser)) {
                return ResultModel.error("无权限操作");
            }
            DataRole existingRole = dataRoleService.getRoleById(id);
            if (existingRole == null) {
                return ResultModel.error("角色不存在");
            }

            boolean success = dataRoleService.deleteRole(id);
            if (success) {
                return ResultModel.success("删除成功");
            } else {
                return ResultModel.error("删除失败");
            }
        } catch (Exception e) {
            return ResultModel.error("删除失败: " + e.getMessage());
        }
    }

    private boolean isAdmin(Operator currentUser) {
        if (currentUser == null) {
            return false;
        }
        return currentUser.getRoleinfoId() == 1 || currentUser.getRoleinfoId() == 3;
    }
}
