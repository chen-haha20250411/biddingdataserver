package com.xiao.core.data_permission.service.impl;

import com.xiao.base.BaseMapper;
import com.xiao.base.BaseServiceImpl;
import com.xiao.core.data_permission.domain.DataPermission;
import com.xiao.core.data_permission.mapper.DataPermissionMapper;
import com.xiao.core.data_permission.service.DataPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DataPermissionServiceImpl extends BaseServiceImpl<DataPermission> implements DataPermissionService {

    @Autowired
    private DataPermissionMapper dataPermissionMapper;

    @Override
    public BaseMapper<DataPermission> getMap() {
        return dataPermissionMapper;
    }

    @Override
    public List<DataPermission> getPermissionsByRoleId(Integer roleId) {
        return dataPermissionMapper.queryByRoleId(roleId);
    }

    @Override
    public List<DataPermission> getPermissionsByType(String permissionType) {
        return dataPermissionMapper.queryByPermissionType(permissionType);
    }

    @Override
    public DataPermission getPermissionByRoleIdAndType(Integer roleId, String permissionType) {
        return dataPermissionMapper.queryByRoleIdAndType(roleId, permissionType);
    }

    @Override
    public boolean assignPermission(DataPermission permission) {
        // 检查是否已存在同角色同类型的权限
        DataPermission existingPermission = dataPermissionMapper.queryByRoleIdAndType(permission.getRoleId(), permission.getPermissionType());
        if (existingPermission != null) {
            // 存在则更新
            existingPermission.setPermissionValue(permission.getPermissionValue());
            return dataPermissionMapper.update(existingPermission) > 0;
        } else {
            // 不存在则新增
            return dataPermissionMapper.insert(permission) > 0;
        }
    }

    @Override
    public boolean updatePermission(DataPermission permission) {
        return dataPermissionMapper.update(permission) > 0;
    }

    @Override
    public boolean deletePermission(Integer id) {
        return dataPermissionMapper.deleteById(String.valueOf(id)) > 0;
    }

    @Override
    public boolean deletePermissionsByRoleId(Integer roleId) {
        return dataPermissionMapper.deleteByRoleId(roleId) > 0;
    }
}
