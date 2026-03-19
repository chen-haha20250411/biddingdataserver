package com.xiao.core.basic.user_data_permission.service.impl;

import com.xiao.base.BaseMapper;
import com.xiao.base.BaseServiceImpl;
import com.xiao.core.basic.user_data_permission.domain.UserDataPermission;
import com.xiao.core.basic.user_data_permission.mapper.UserDataPermissionMapper;
import com.xiao.core.basic.user_data_permission.service.UserDataPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserDataPermissionServiceImpl extends BaseServiceImpl<UserDataPermission> implements UserDataPermissionService {

    @Autowired
    private UserDataPermissionMapper userDataPermissionMapper;

    @Override
    public BaseMapper<UserDataPermission> getMap() {
        return userDataPermissionMapper;
    }

    @Override
    public List<UserDataPermission> getUserDataPermissions(Integer userId) {
        return userDataPermissionMapper.getPermissionsBySourceUserId(userId);
    }

    @Override
    public boolean grantUserDataPermission(Integer sourceUserId, Integer targetUserId, String permissionType) {
        // 检查权限是否已存在
        int count = userDataPermissionMapper.checkPermissionExists(sourceUserId, targetUserId, permissionType);
        if (count > 0) {
            return false; // 权限已存在
        }

        // 创建新权限
        UserDataPermission permission = new UserDataPermission();
        permission.setSourceUserId(sourceUserId);
        permission.setTargetUserId(targetUserId);
        permission.setPermissionType(permissionType);
        return userDataPermissionMapper.insert(permission) > 0;
    }

    @Override
    public boolean revokeUserDataPermission(Integer sourceUserId, Integer targetUserId, String permissionType) {
        return userDataPermissionMapper.deletePermission(sourceUserId, targetUserId, permissionType) > 0;
    }

    @Override
    public boolean checkUserDataPermission(Integer sourceUserId, Integer targetUserId, String permissionType) {
        int count = userDataPermissionMapper.checkPermissionExists(sourceUserId, targetUserId, permissionType);
        return count > 0;
    }
}
