package com.xiao.core.data_permission.mapper;

import com.xiao.base.BaseMapper;
import com.xiao.core.data_permission.domain.DepartmentInfo;

import java.util.List;

public interface DepartmentInfoMapper extends BaseMapper<DepartmentInfo> {

    List<DepartmentInfo> queryByParentId(Integer parentId);

    List<DepartmentInfo> queryByStatus(Integer status);

    DepartmentInfo queryByDeptCode(String deptCode);
}
