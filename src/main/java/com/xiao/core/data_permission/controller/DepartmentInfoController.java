package com.xiao.core.data_permission.controller;

import com.xiao.base.ResultModel;
import com.xiao.core.basic.operator.domain.Operator;
import com.xiao.core.data_permission.domain.DepartmentInfo;
import com.xiao.core.data_permission.service.DepartmentInfoService;
import com.xiao.logannotation.CurrentUser;
import com.xiao.logannotation.LoginRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departmentInfo")
public class DepartmentInfoController {

    @Autowired
    private DepartmentInfoService departmentInfoService;

    @LoginRequired(remark="查询部门列表操作")
    @GetMapping("/list")
    public ResultModel getAllDepartments(@CurrentUser Operator currentUser) {
        try {
            List<DepartmentInfo> departments = departmentInfoService.getAllDepartments();
            return ResultModel.success("查询成功", departments);
        } catch (Exception e) {
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }

    @LoginRequired(remark="查询部门详情操作")
    @GetMapping("/get/{id}")
    public ResultModel getDepartmentById(@CurrentUser Operator currentUser, @PathVariable Integer id) {
        try {
            DepartmentInfo department = departmentInfoService.getDepartmentById(id);
            if (department == null) {
                return ResultModel.error("部门不存在");
            }
            return ResultModel.success("查询成功", department);
        } catch (Exception e) {
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }

    @LoginRequired(remark="查询子部门列表操作")
    @GetMapping("/listByParent/{parentId}")
    public ResultModel getDepartmentsByParentId(@CurrentUser Operator currentUser, @PathVariable Integer parentId) {
        try {
            List<DepartmentInfo> departments = departmentInfoService.getDepartmentsByParentId(parentId);
            return ResultModel.success("查询成功", departments);
        } catch (Exception e) {
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }

    @LoginRequired(remark="创建部门操作")
    @PostMapping("/create")
    public ResultModel createDepartment(@CurrentUser Operator currentUser, @RequestBody DepartmentInfo department) {
        try {
            if (!isAdmin(currentUser)) {
                return ResultModel.error("无权限操作");
            }
            if (department.getDeptName() == null || department.getDeptName().trim().isEmpty()) {
                return ResultModel.error("部门名称不能为空");
            }

            boolean success = departmentInfoService.createDepartment(department);
            if (success) {
                return ResultModel.success("创建成功", department);
            } else {
                return ResultModel.error("创建失败");
            }
        } catch (Exception e) {
            return ResultModel.error("创建失败: " + e.getMessage());
        }
    }

    @LoginRequired(remark="更新部门操作")
    @PutMapping("/update")
    public ResultModel updateDepartment(@CurrentUser Operator currentUser, @RequestBody DepartmentInfo department) {
        try {
            if (!isAdmin(currentUser)) {
                return ResultModel.error("无权限操作");
            }
            if (department.getId() == null) {
                return ResultModel.error("部门ID不能为空");
            }

            DepartmentInfo existingDept = departmentInfoService.getDepartmentById(department.getId());
            if (existingDept == null) {
                return ResultModel.error("部门不存在");
            }

            boolean success = departmentInfoService.updateDepartment(department);
            if (success) {
                return ResultModel.success("更新成功");
            } else {
                return ResultModel.error("更新失败");
            }
        } catch (Exception e) {
            return ResultModel.error("更新失败: " + e.getMessage());
        }
    }

    @LoginRequired(remark="删除部门操作")
    @DeleteMapping("/delete/{id}")
    public ResultModel deleteDepartment(@CurrentUser Operator currentUser, @PathVariable Integer id) {
        try {
            if (!isAdmin(currentUser)) {
                return ResultModel.error("无权限操作");
            }
            DepartmentInfo existingDept = departmentInfoService.getDepartmentById(id);
            if (existingDept == null) {
                return ResultModel.error("部门不存在");
            }

            boolean success = departmentInfoService.deleteDepartment(id);
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
