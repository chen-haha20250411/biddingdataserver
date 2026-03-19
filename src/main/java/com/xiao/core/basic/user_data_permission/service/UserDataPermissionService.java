package com.xiao.core.basic.user_data_permission.service;

import com.xiao.core.basic.user_data_permission.domain.UserDataPermission;

import java.util.List;

public interface UserDataPermissionService {

    /**
     * 获取用户的数据访问权限列表
     * @param userId 用户ID
     * @return 权限列表
     */
    List<UserDataPermission> getUserDataPermissions(Integer userId);

    /**
     * 授予数据访问权限
     * @param sourceUserId 授权用户ID
     * @param targetUserId 被访问用户ID
     * @param permissionType 权限类型
     * @return 是否成功
     */
    boolean grantUserDataPermission(Integer sourceUserId, Integer targetUserId, String permissionType);

    /**
     * 撤销数据访问权限
     * @param sourceUserId 授权用户ID
     * @param targetUserId 被访问用户ID
     * @param permissionType 权限类型
     * @return 是否成功
     */
    boolean revokeUserDataPermission(Integer sourceUserId, Integer targetUserId, String permissionType);

    /**
     * 检查用户是否有访问目标用户数据的权限
     * @param sourceUserId 授权用户ID
     * @param targetUserId 被访问用户ID
     * @param permissionType 权限类型
     * @return 是否有权限
     */
    boolean checkUserDataPermission(Integer sourceUserId, Integer targetUserId, String permissionType);
}
