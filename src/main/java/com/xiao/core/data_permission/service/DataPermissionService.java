package com.xiao.core.data_permission.service;

import com.xiao.core.data_permission.domain.DataPermission;

import java.util.List;

public interface DataPermissionService {

    List<DataPermission> getPermissionsByRoleId(Integer roleId);

    List<DataPermission> getPermissionsByType(String permissionType);

    boolean assignPermission(DataPermission permission);

    boolean updatePermission(DataPermission permission);

    boolean deletePermission(Integer id);

    boolean deletePermissionsByRoleId(Integer roleId);
}
