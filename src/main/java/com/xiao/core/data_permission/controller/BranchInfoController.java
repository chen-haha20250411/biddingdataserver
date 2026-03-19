package com.xiao.core.data_permission.controller;

import com.xiao.base.ResultModel;
import com.xiao.core.basic.operator.domain.Operator;
import com.xiao.core.data_permission.domain.BranchInfo;
import com.xiao.core.data_permission.service.BranchInfoService;
import com.xiao.logannotation.CurrentUser;
import com.xiao.logannotation.LoginRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branchInfo")
public class BranchInfoController {

    @Autowired
    private BranchInfoService branchInfoService;

    @LoginRequired(remark="查询分支机构列表操作")
    @GetMapping("/list")
    public ResultModel getAllBranches(@CurrentUser Operator currentUser) {
        try {
            List<BranchInfo> branches = branchInfoService.getAllBranches();
            return ResultModel.success("查询成功", branches);
        } catch (Exception e) {
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }

    @LoginRequired(remark="查询分支机构详情操作")
    @GetMapping("/get/{id}")
    public ResultModel getBranchById(@CurrentUser Operator currentUser, @PathVariable Integer id) {
        try {
            BranchInfo branch = branchInfoService.getBranchById(id);
            if (branch == null) {
                return ResultModel.error("分支机构不存在");
            }
            return ResultModel.success("查询成功", branch);
        } catch (Exception e) {
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }

    @LoginRequired(remark="查询子分支机构列表操作")
    @GetMapping("/listByParent/{parentId}")
    public ResultModel getBranchesByParentId(@CurrentUser Operator currentUser, @PathVariable Integer parentId) {
        try {
            List<BranchInfo> branches = branchInfoService.getBranchesByParentId(parentId);
            return ResultModel.success("查询成功", branches);
        } catch (Exception e) {
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }

    @LoginRequired(remark="创建分支机构操作")
    @PostMapping("/create")
    public ResultModel createBranch(@CurrentUser Operator currentUser, @RequestBody BranchInfo branch) {
        try {
            if (!isAdmin(currentUser)) {
                return ResultModel.error("无权限操作");
            }
            if (branch.getBranchName() == null || branch.getBranchName().trim().isEmpty()) {
                return ResultModel.error("分支机构名称不能为空");
            }

            boolean success = branchInfoService.createBranch(branch);
            if (success) {
                return ResultModel.success("创建成功", branch);
            } else {
                return ResultModel.error("创建失败");
            }
        } catch (Exception e) {
            return ResultModel.error("创建失败: " + e.getMessage());
        }
    }

    @LoginRequired(remark="更新分支机构操作")
    @PutMapping("/update")
    public ResultModel updateBranch(@CurrentUser Operator currentUser, @RequestBody BranchInfo branch) {
        try {
            if (!isAdmin(currentUser)) {
                return ResultModel.error("无权限操作");
            }
            if (branch.getId() == null) {
                return ResultModel.error("分支机构ID不能为空");
            }

            BranchInfo existingBranch = branchInfoService.getBranchById(branch.getId());
            if (existingBranch == null) {
                return ResultModel.error("分支机构不存在");
            }

            boolean success = branchInfoService.updateBranch(branch);
            if (success) {
                return ResultModel.success("更新成功");
            } else {
                return ResultModel.error("更新失败");
            }
        } catch (Exception e) {
            return ResultModel.error("更新失败: " + e.getMessage());
        }
    }

    @LoginRequired(remark="删除分支机构操作")
    @DeleteMapping("/delete/{id}")
    public ResultModel deleteBranch(@CurrentUser Operator currentUser, @PathVariable Integer id) {
        try {
            if (!isAdmin(currentUser)) {
                return ResultModel.error("无权限操作");
            }
            BranchInfo existingBranch = branchInfoService.getBranchById(id);
            if (existingBranch == null) {
                return ResultModel.error("分支机构不存在");
            }

            boolean success = branchInfoService.deleteBranch(id);
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
