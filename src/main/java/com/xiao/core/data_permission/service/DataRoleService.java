package com.xiao.core.data_permission.service;

import com.xiao.core.data_permission.domain.DataRole;

import java.util.List;

public interface DataRoleService {

    List<DataRole> getAllRoles();

    DataRole getRoleById(Integer id);

    DataRole getRoleByCode(String roleCode);

    List<DataRole> getRolesByStatus(Integer status);

    boolean createRole(DataRole role);

    boolean updateRole(DataRole role);

    boolean deleteRole(Integer id);
}
