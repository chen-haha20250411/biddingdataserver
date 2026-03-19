package com.xiao.core.data_permission.service;

import com.xiao.core.data_permission.domain.BranchInfo;

import java.util.List;

public interface BranchInfoService {

    List<BranchInfo> getAllBranches();

    BranchInfo getBranchById(Integer id);

    List<BranchInfo> getBranchesByParentId(Integer parentId);

    List<BranchInfo> getBranchesByStatus(Integer status);

    BranchInfo getBranchByCode(String branchCode);

    boolean createBranch(BranchInfo branch);

    boolean updateBranch(BranchInfo branch);

    boolean deleteBranch(Integer id);
}
