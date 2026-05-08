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
import java.util.Map;
import java.util.stream.Collectors;

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
        DataPermission existingPermission = dataPermissionMapper.queryByRoleIdAndType(permission.getRoleId(), permission.getPermissionType());
        if (existingPermission != null) {
            existingPermission.setPermissionValue(permission.getPermissionValue());
            return dataPermissionMapper.update(existingPermission) > 0;
        } else {
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

    @Override
    public void savePermissions(Integer roleId, List<Map<String, Object>> permissions) {
        dataPermissionMapper.deleteByRoleId(roleId);
        if (permissions == null || permissions.isEmpty()) {
            return;
        }
        for (Map<String, Object> p : permissions) {
            if (p == null) {
                continue;
            }
            DataPermission perm = new DataPermission();
            perm.setRoleId(roleId);
            String scopeType = p.get("scopeType") != null ? p.get("scopeType").toString() : "";
            perm.setPermissionType(scopeType);
            Boolean isAll = Boolean.TRUE.equals(p.get("isAll"));
            if (isAll) {
                perm.setPermissionValue("ALL");
            } else {
                @SuppressWarnings("unchecked")
                List<Object> selectedIds = (List<Object>) p.get("selectedIds");
                if (selectedIds != null && !selectedIds.isEmpty()) {
                    String idsStr = String.join(",", selectedIds.stream().map(Object::toString).collect(Collectors.toList()));
                    perm.setPermissionValue(idsStr);
                } else {
                    perm.setPermissionValue("");
                }
            }
            dataPermissionMapper.insert(perm);
        }
    }
}
