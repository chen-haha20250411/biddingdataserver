package com.xiao.core.data_permission.mapper;

import com.xiao.base.BaseMapper;
import com.xiao.core.data_permission.domain.DataPermission;

import java.util.List;

public interface DataPermissionMapper extends BaseMapper<DataPermission> {

    List<DataPermission> queryByRoleId(Integer roleId);

    List<DataPermission> queryByPermissionType(String permissionType);

    int deleteByRoleId(Integer roleId);
}
