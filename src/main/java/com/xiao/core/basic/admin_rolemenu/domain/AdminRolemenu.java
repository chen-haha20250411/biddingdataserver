package com.xiao.core.basic.admin_rolemenu.domain;

import com.xiao.base.BaseDomain;

/**
 * <p>
 * 角色菜单表
 * </p>
 *
 * @author 裘晓伟
 * @since 2018-07-12
 */
public class AdminRolemenu extends BaseDomain {

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

    /**
     * 菜单Id
     */
	private Integer menuId;
    /**
     * 角色Id
     */
	private Integer roleInfoId;


	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public Integer getRoleInfoId() {
		return roleInfoId;
	}

	public void setRoleInfoId(Integer roleInfoId) {
		this.roleInfoId = roleInfoId;
	}

	@Override
	public String toString() {
		return "AdminRolemenu{" +
			"menuId=" + menuId +
			", roleInfoId=" + roleInfoId +
			"}";
	}
}
