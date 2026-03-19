package com.xiao.core.data_permission.mapper;

import com.xiao.base.BaseMapper;
import com.xiao.core.data_permission.domain.UserDataRole;

import java.util.List;

public interface UserDataRoleMapper extends BaseMapper<UserDataRole> {

    List<UserDataRole> queryByUserId(Integer userId);

    List<UserDataRole> queryByRoleId(Integer roleId);

    int deleteByUserId(Integer userId);

    int deleteByUserIdAndRoleId(Integer userId, Integer roleId);
}
