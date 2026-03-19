package com.xiao.core.data_permission.service.impl;

import com.xiao.core.data_permission.constants.PermissionTypeConstants;
import com.xiao.core.data_permission.domain.DataPermission;
import com.xiao.core.data_permission.domain.DataRole;
import com.xiao.core.data_permission.domain.UserDataRole;
import com.xiao.core.data_permission.service.DataPermissionCheckService;
import com.xiao.core.data_permission.service.DataPermissionService;
import com.xiao.core.data_permission.service.DataRoleService;
import com.xiao.core.data_permission.service.UserDataRoleService;
import com.xiao.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class DataPermissionCheckServiceImpl implements DataPermissionCheckService {

    private static final String PERMISSION_CACHE_PREFIX = "data_permission:user:";
    private static final long CACHE_EXPIRE_HOURS = 2;

    @Autowired
    private UserDataRoleService userDataRoleService;

    @Autowired
    private DataRoleService dataRoleService;

    @Autowired
    private DataPermissionService dataPermissionService;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public boolean checkPermission(Integer userId, String permissionType, String permissionValue) {
        if (userId == null || StringUtils.isEmpty(permissionType) || StringUtils.isEmpty(permissionValue)) {
            return false;
        }

        List<String> permissionValues = getUserPermissionValues(userId, permissionType);
        if (permissionValues == null || permissionValues.isEmpty()) {
            return false;
        }

        if (permissionValues.contains(PermissionTypeConstants.ALL)) {
            return true;
        }

        return permissionValues.contains(permissionValue);
    }

    @Override
    public List<String> getUserPermissionValues(Integer userId, String permissionType) {
        if (userId == null || StringUtils.isEmpty(permissionType)) {
            return new ArrayList<>();
        }

        String cacheKey = PERMISSION_CACHE_PREFIX + userId + ":" + permissionType;
        Object cachedValue = redisUtils.get(cacheKey);
        if (cachedValue != null) {
            return (List<String>) cachedValue;
        }

        List<String> permissionValues = new ArrayList<>();
        List<UserDataRole> userRoles = userDataRoleService.getRolesByUserId(userId);
        
        if (userRoles == null || userRoles.isEmpty()) {
            return permissionValues;
        }

        for (UserDataRole userRole : userRoles) {
            DataRole role = dataRoleService.getRoleById(userRole.getRoleId());
            if (role == null || role.getStatus() != 1) {
                continue;
            }

            List<DataPermission> permissions = dataPermissionService.getPermissionsByRoleId(role.getId());
            if (permissions == null || permissions.isEmpty()) {
                continue;
            }

            for (DataPermission permission : permissions) {
                if (permissionType.equals(permission.getPermissionType())) {
                    String value = permission.getPermissionValue();
                    if (!StringUtils.isEmpty(value)) {
                        if (value.contains(",")) {
                            permissionValues.addAll(Arrays.asList(value.split(",")));
                        } else {
                            permissionValues.add(value);
                        }
                    }
                }
            }
        }

        permissionValues = permissionValues.stream().distinct().collect(Collectors.toList());
        
        redisUtils.set(cacheKey, permissionValues);
        redisUtils.expire(cacheKey, CACHE_EXPIRE_HOURS * 3600);
        
        return permissionValues;
    }

    @Override
    public boolean hasAllPermission(Integer userId) {
        if (userId == null) {
            return false;
        }

        List<String> allPermissionTypes = Arrays.asList(
                PermissionTypeConstants.CUSTOMER,
                PermissionTypeConstants.INDUSTRY,
                PermissionTypeConstants.PRODUCT_TYPE,
                PermissionTypeConstants.PRODUCT_LINE,
                PermissionTypeConstants.WAREHOUSE,
                PermissionTypeConstants.BRANCH,
                PermissionTypeConstants.DEPARTMENT,
                PermissionTypeConstants.EMPLOYEE
        );

        for (String permissionType : allPermissionTypes) {
            List<String> permissionValues = getUserPermissionValues(userId, permissionType);
            if (permissionValues != null && permissionValues.contains(PermissionTypeConstants.ALL)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isCurrentUserOnly(Integer userId) {
        if (userId == null) {
            return false;
        }

        List<String> permissionValues = getUserPermissionValues(userId, PermissionTypeConstants.EMPLOYEE);
        return permissionValues != null && permissionValues.contains(PermissionTypeConstants.CURRENT_USER);
    }

    @Override
    public List<Integer> getUserDepartmentIds(Integer userId) {
        List<String> permissionValues = getUserPermissionValues(userId, PermissionTypeConstants.DEPARTMENT);
        return parsePermissionValuesToIntegers(permissionValues);
    }

    @Override
    public List<Integer> getUserBranchIds(Integer userId) {
        List<String> permissionValues = getUserPermissionValues(userId, PermissionTypeConstants.BRANCH);
        return parsePermissionValuesToIntegers(permissionValues);
    }

    @Override
    public List<Integer> getUserEmployeeIds(Integer userId) {
        List<String> permissionValues = getUserPermissionValues(userId, PermissionTypeConstants.EMPLOYEE);
        if (permissionValues != null && permissionValues.contains(PermissionTypeConstants.CURRENT_USER)) {
            List<Integer> result = new ArrayList<>();
            result.add(userId);
            return result;
        }
        return parsePermissionValuesToIntegers(permissionValues);
    }

    @Override
    public void clearUserPermissionCache(Integer userId) {
        if (userId == null) {
            return;
        }

        List<String> allPermissionTypes = Arrays.asList(
                PermissionTypeConstants.CUSTOMER,
                PermissionTypeConstants.INDUSTRY,
                PermissionTypeConstants.PRODUCT_TYPE,
                PermissionTypeConstants.PRODUCT_LINE,
                PermissionTypeConstants.WAREHOUSE,
                PermissionTypeConstants.BRANCH,
                PermissionTypeConstants.DEPARTMENT,
                PermissionTypeConstants.EMPLOYEE
        );

        for (String permissionType : allPermissionTypes) {
            String cacheKey = PERMISSION_CACHE_PREFIX + userId + ":" + permissionType;
            redisUtils.del(cacheKey);
        }
    }

    private List<Integer> parsePermissionValuesToIntegers(List<String> permissionValues) {
        if (permissionValues == null || permissionValues.isEmpty()) {
            return new ArrayList<>();
        }

        return permissionValues.stream()
                .filter(value -> !PermissionTypeConstants.ALL.equals(value) && !PermissionTypeConstants.CURRENT_USER.equals(value))
                .map(value -> {
                    try {
                        return Integer.parseInt(value.trim());
                    } catch (NumberFormatException e) {
                        return null;
                    }
                })
                .filter(value -> value != null)
                .collect(Collectors.toList());
    }
}
