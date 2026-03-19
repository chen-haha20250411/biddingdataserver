package com.xiao.core.basic.user_data_permission.domain;

import com.xiao.base.BaseDomain;

import java.util.Date;

public class UserDataPermission extends BaseDomain {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 授权用户ID
     */
    private Integer sourceUserId;

    /**
     * 被访问用户ID
     */
    private Integer targetUserId;

    /**
     * 权限类型
     */
    private String permissionType;

    /**
     * 创建时间
     */
    private Date createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSourceUserId() {
        return sourceUserId;
    }

    public void setSourceUserId(Integer sourceUserId) {
        this.sourceUserId = sourceUserId;
    }

    public Integer getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(Integer targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
