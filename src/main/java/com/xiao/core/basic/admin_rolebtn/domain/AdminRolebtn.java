package com.xiao.core.basic.admin_rolebtn.domain;

import com.xiao.base.BaseDomain;

/**
 * <p>
 * 角色按钮表
 * </p>
 *
 * @author 裘晓伟
 * @since 2018-07-12
 */
public class AdminRolebtn extends BaseDomain {
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private Integer btnId;
	private Integer roleInfoId;


	public Integer getBtnId() {
		return btnId;
	}

	public void setBtnId(Integer btnId) {
		this.btnId = btnId;
	}

	public Integer getRoleInfoId() {
		return roleInfoId;
	}

	public void setRoleInfoId(Integer roleInfoId) {
		this.roleInfoId = roleInfoId;
	}

	@Override
	public String toString() {
		return "AdminRolebtn{" +
			"btnId=" + btnId +
			", roleInfoId=" + roleInfoId +
			"}";
	}
}
