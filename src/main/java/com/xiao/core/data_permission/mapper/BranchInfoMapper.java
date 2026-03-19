package com.xiao.core.data_permission.mapper;

import com.xiao.base.BaseMapper;
import com.xiao.core.data_permission.domain.BranchInfo;

import java.util.List;

public interface BranchInfoMapper extends BaseMapper<BranchInfo> {

    List<BranchInfo> queryByParentId(Integer parentId);

    List<BranchInfo> queryByStatus(Integer status);

    BranchInfo queryByBranchCode(String branchCode);
}
