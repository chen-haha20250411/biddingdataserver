package com.xiao.core.data_permission.service.impl;

import com.xiao.base.BaseMapper;
import com.xiao.base.BaseServiceImpl;
import com.xiao.core.data_permission.domain.BranchInfo;
import com.xiao.core.data_permission.mapper.BranchInfoMapper;
import com.xiao.core.data_permission.service.BranchInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BranchInfoServiceImpl extends BaseServiceImpl<BranchInfo> implements BranchInfoService {

    @Autowired
    private BranchInfoMapper branchInfoMapper;

    @Override
    public BaseMapper<BranchInfo> getMap() {
        return branchInfoMapper;
    }

    @Override
    public List<BranchInfo> getAllBranches() {
        return branchInfoMapper.queryByStatus(1);
    }

    @Override
    public BranchInfo getBranchById(Integer id) {
        return branchInfoMapper.queryById(String.valueOf(id));
    }

    @Override
    public List<BranchInfo> getBranchesByParentId(Integer parentId) {
        return branchInfoMapper.queryByParentId(parentId);
    }

    @Override
    public List<BranchInfo> getBranchesByStatus(Integer status) {
        return branchInfoMapper.queryByStatus(status);
    }

    @Override
    public BranchInfo getBranchByCode(String branchCode) {
        return branchInfoMapper.queryByBranchCode(branchCode);
    }

    @Override
    public boolean createBranch(BranchInfo branch) {
        return branchInfoMapper.insert(branch) > 0;
    }

    @Override
    public boolean updateBranch(BranchInfo branch) {
        return branchInfoMapper.update(branch) > 0;
    }

    @Override
    public boolean deleteBranch(Integer id) {
        return branchInfoMapper.deleteById(String.valueOf(id)) > 0;
    }
}
