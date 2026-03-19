package com.xiao.core.data_permission.service;

import com.xiao.core.data_permission.domain.DepartmentInfo;

import java.util.List;

public interface DepartmentInfoService {

    List<DepartmentInfo> getAllDepartments();

    DepartmentInfo getDepartmentById(Integer id);

    List<DepartmentInfo> getDepartmentsByParentId(Integer parentId);

    List<DepartmentInfo> getDepartmentsByStatus(Integer status);

    DepartmentInfo getDepartmentByCode(String deptCode);

    boolean createDepartment(DepartmentInfo department);

    boolean updateDepartment(DepartmentInfo department);

    boolean deleteDepartment(Integer id);
}
