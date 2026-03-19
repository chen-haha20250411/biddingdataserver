package com.xiao.core.data_permission.service.impl;

import com.xiao.base.BaseMapper;
import com.xiao.base.BaseServiceImpl;
import com.xiao.core.data_permission.domain.DepartmentInfo;
import com.xiao.core.data_permission.mapper.DepartmentInfoMapper;
import com.xiao.core.data_permission.service.DepartmentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DepartmentInfoServiceImpl extends BaseServiceImpl<DepartmentInfo> implements DepartmentInfoService {

    @Autowired
    private DepartmentInfoMapper departmentInfoMapper;

    @Override
    public BaseMapper<DepartmentInfo> getMap() {
        return departmentInfoMapper;
    }

    @Override
    public List<DepartmentInfo> getAllDepartments() {
        return departmentInfoMapper.queryByStatus(1);
    }

    @Override
    public DepartmentInfo getDepartmentById(Integer id) {
        return departmentInfoMapper.queryById(String.valueOf(id));
    }

    @Override
    public List<DepartmentInfo> getDepartmentsByParentId(Integer parentId) {
        return departmentInfoMapper.queryByParentId(parentId);
    }

    @Override
    public List<DepartmentInfo> getDepartmentsByStatus(Integer status) {
        return departmentInfoMapper.queryByStatus(status);
    }

    @Override
    public DepartmentInfo getDepartmentByCode(String deptCode) {
        return departmentInfoMapper.queryByDeptCode(deptCode);
    }

    @Override
    public boolean createDepartment(DepartmentInfo department) {
        return departmentInfoMapper.insert(department) > 0;
    }

    @Override
    public boolean updateDepartment(DepartmentInfo department) {
        return departmentInfoMapper.update(department) > 0;
    }

    @Override
    public boolean deleteDepartment(Integer id) {
        return departmentInfoMapper.deleteById(String.valueOf(id)) > 0;
    }
}
