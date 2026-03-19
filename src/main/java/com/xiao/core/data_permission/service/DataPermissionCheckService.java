package com.xiao.core.data_permission.service;

import java.util.List;

public interface DataPermissionCheckService {

    /**
     * 检查用户是否有访问指定类型数据的权限
     * @param userId 用户ID
     * @param permissionType 权限类型
     * @param permissionValue 权限值
     * @return 是否有权限
     */
    boolean checkPermission(Integer userId, String permissionType, String permissionValue);

    /**
     * 获取用户在指定权限类型下的所有权限值
     * @param userId 用户ID
     * @param permissionType 权限类型
     * @return 权限值列表
     */
    List<String> getUserPermissionValues(Integer userId, String permissionType);

    /**
     * 检查用户是否有访问所有数据的权限
     * @param userId 用户ID
     * @return 是否有权限
     */
    boolean hasAllPermission(Integer userId);

    /**
     * 检查用户是否只能访问自己的数据
     * @param userId 用户ID
     * @return 是否只能访问自己的数据
     */
    boolean isCurrentUserOnly(Integer userId);

    /**
     * 获取用户可访问的部门ID列表
     * @param userId 用户ID
     * @return 部门ID列表
     */
    List<Integer> getUserDepartmentIds(Integer userId);

    /**
     * 获取用户可访问的分支机构ID列表
     * @param userId 用户ID
     * @return 分支机构ID列表
     */
    List<Integer> getUserBranchIds(Integer userId);

    /**
     * 获取用户可访问的员工ID列表
     * @param userId 用户ID
     * @return 员工ID列表
     */
    List<Integer> getUserEmployeeIds(Integer userId);

    /**
     * 清除用户权限缓存
     * @param userId 用户ID
     */
    void clearUserPermissionCache(Integer userId);
}
