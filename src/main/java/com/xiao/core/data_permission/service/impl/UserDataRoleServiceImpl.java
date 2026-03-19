package com.xiao.core.data_permission.service.impl;

import com.xiao.base.BaseMapper;
import com.xiao.base.BaseServiceImpl;
import com.xiao.core.data_permission.domain.UserDataRole;
import com.xiao.core.data_permission.mapper.UserDataRoleMapper;
import com.xiao.core.data_permission.service.UserDataRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserDataRoleServiceImpl extends BaseServiceImpl<UserDataRole> implements UserDataRoleService {

    @Autowired
    private UserDataRoleMapper userDataRoleMapper;

    @Override
    public BaseMapper<UserDataRole> getMap() {
        return userDataRoleMapper;
    }

    @Override
    public List<UserDataRole> getRolesByUserId(Integer userId) {
        return userDataRoleMapper.queryByUserId(userId);
    }

    @Override
    public List<UserDataRole> getUsersByRoleId(Integer roleId) {
        return userDataRoleMapper.queryByRoleId(roleId);
    }

    @Override
    public boolean assignRoleToUser(Integer userId, Integer roleId) {
        UserDataRole userDataRole = new UserDataRole();
        userDataRole.setUserId(userId);
        userDataRole.setRoleId(roleId);
        return userDataRoleMapper.insert(userDataRole) > 0;
    }

    @Override
    public boolean revokeRoleFromUser(Integer userId, Integer roleId) {
        userDataRoleMapper.deleteByUserIdAndRoleId(userId, roleId);
        return true;
    }

    @Override
    public boolean revokeAllRolesFromUser(Integer userId) {
        userDataRoleMapper.deleteByUserId(userId);
        return true;
    }

    @Override
    public boolean batchAssignRolesToUser(Integer userId, List<Integer> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return false;
        }
        for (Integer roleId : roleIds) {
            assignRoleToUser(userId, roleId);
        }
        return true;
    }
}
