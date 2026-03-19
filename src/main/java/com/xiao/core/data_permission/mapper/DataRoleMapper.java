package com.xiao.core.data_permission.mapper;

import com.xiao.base.BaseMapper;
import com.xiao.core.data_permission.domain.DataRole;

import java.util.List;

public interface DataRoleMapper extends BaseMapper<DataRole> {

    List<DataRole> queryAllRoles();

    DataRole queryByRoleCode(String roleCode);

    List<DataRole> queryByStatus(Integer status);
}
