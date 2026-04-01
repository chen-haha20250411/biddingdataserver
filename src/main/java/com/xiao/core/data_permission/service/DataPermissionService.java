package com.xiao.core.data_permission.service;

import com.xiao.base.BaseService;
import com.xiao.core.data_permission.domain.DataPermission;

import java.util.List;

public interface DataPermissionService extends BaseService<DataPermission> {

    List<DataPermission> getPermissionsByRoleId(Integer roleId);

    List<DataPermission> getPermissionsByType(String permissionType);

    DataPermission getPermissionByRoleIdAndType(Integer roleId, String permissionType);

    boolean assignPermission(DataPermission permission);

    boolean updatePermission(DataPermission permission);

    boolean deletePermission(Integer id);

    boolean deletePermissionsByRoleId(Integer roleId);
}
