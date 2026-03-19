package com.xiao.core.basic.user_data_permission.mapper;

import com.xiao.base.BaseMapper;
import com.xiao.core.basic.user_data_permission.domain.UserDataPermission;

import java.util.List;

public interface UserDataPermissionMapper extends BaseMapper<UserDataPermission> {

    /**
     * 根据授权用户ID获取权限列表
     * @param sourceUserId 授权用户ID
     * @return 权限列表
     */
    List<UserDataPermission> getPermissionsBySourceUserId(Integer sourceUserId);

    /**
     * 根据被访问用户ID获取权限列表
     * @param targetUserId 被访问用户ID
     * @return 权限列表
     */
    List<UserDataPermission> getPermissionsByTargetUserId(Integer targetUserId);

    /**
     * 检查权限是否存在
     * @param sourceUserId 授权用户ID
     * @param targetUserId 被访问用户ID
     * @param permissionType 权限类型
     * @return 权限数量
     */
    int checkPermissionExists(Integer sourceUserId, Integer targetUserId, String permissionType);

    /**
     * 删除权限
     * @param sourceUserId 授权用户ID
     * @param targetUserId 被访问用户ID
     * @param permissionType 权限类型
     * @return 删除数量
     */
    int deletePermission(Integer sourceUserId, Integer targetUserId, String permissionType);
}
