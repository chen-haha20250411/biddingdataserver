package com.xiao.core.data_permission.service.impl;

import com.xiao.base.BaseMapper;
import com.xiao.base.BaseServiceImpl;
import com.xiao.core.data_permission.domain.DataRole;
import com.xiao.core.data_permission.mapper.DataRoleMapper;
import com.xiao.core.data_permission.service.DataRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DataRoleServiceImpl extends BaseServiceImpl<DataRole> implements DataRoleService {

    @Autowired
    private DataRoleMapper dataRoleMapper;

    @Override
    public BaseMapper<DataRole> getMap() {
        return dataRoleMapper;
    }

    @Override
    public List<DataRole> getAllRoles() {
        return dataRoleMapper.queryAllRoles();
    }

    @Override
    public DataRole getRoleById(Integer id) {
        return dataRoleMapper.queryById(String.valueOf(id));
    }

    @Override
    public DataRole getRoleByCode(String roleCode) {
        return dataRoleMapper.queryByRoleCode(roleCode);
    }

    @Override
    public List<DataRole> getRolesByStatus(Integer status) {
        return dataRoleMapper.queryByStatus(status);
    }

    @Override
    public boolean createRole(DataRole role) {
        return dataRoleMapper.insert(role) > 0;
    }

    @Override
    public boolean updateRole(DataRole role) {
        return dataRoleMapper.update(role) > 0;
    }

    @Override
    public boolean deleteRole(Integer id) {
        return dataRoleMapper.deleteById(String.valueOf(id)) > 0;
    }
}
