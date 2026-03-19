package com.xiao.core.data_permission.service;

import com.xiao.core.data_permission.domain.UserDataRole;

import java.util.List;

public interface UserDataRoleService {

    List<UserDataRole> getRolesByUserId(Integer userId);

    List<UserDataRole> getUsersByRoleId(Integer roleId);

    boolean assignRoleToUser(Integer userId, Integer roleId);

    boolean revokeRoleFromUser(Integer userId, Integer roleId);

    boolean revokeAllRolesFromUser(Integer userId);

    boolean batchAssignRolesToUser(Integer userId, List<Integer> roleIds);
}
