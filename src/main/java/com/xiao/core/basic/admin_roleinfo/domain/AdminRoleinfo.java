package com.xiao.core.basic.admin_roleinfo.domain;


import com.xiao.base.BaseDomain;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author 裘晓伟
 * @since 2018-07-12
 */
public class AdminRoleinfo extends BaseDomain{
private static final long serialVersionUID = 1L;

    /**自增Id*/
    private int roleInfoId;
    /**角色名称*/
    private String roleName;
    /**操作员*/
    private int operatorId;
    /**操作员名称 （显示时使用）*/
    private String loginName;
    /**备注*/
    private String remark;

	public int getRoleInfoId() {
		return roleInfoId;
	}
	public void setRoleInfoId(int roleInfoId) {
		this.roleInfoId = roleInfoId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public int getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(int operatorId) {
		this.operatorId = operatorId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
}
