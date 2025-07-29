package com.xiao.core.basic.admin_menu.domain;

import com.xiao.base.BaseDomain;

import java.util.List;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author 裘晓伟
 * @since 2018-07-12
 */
public class AdminMenu extends BaseDomain{

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

    /**
     * 菜单Id
     */
	private Integer menuId;
    /**
     * 菜单名称
     */
	private String menuName;
    /**
     * 菜单url
     */
	private String menuURL;
    /**
     * 菜单状态
            0：正常
            1：删除
     */
	private Integer state;
    /**
     * 父亲节点
     */
	private Integer parentNo;
    /**
     * 添加人
     */
	private Integer operatoNo;
	private Integer sortOrder;

	private String imageurl;

	private String menu_auth;
	
	private List<AdminMenu> subMenuList;

	private List<String> btnList;

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuURL() {
		return menuURL;
	}

	public void setMenuURL(String menuURL) {
		this.menuURL = menuURL;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getParentNo() {
		return parentNo;
	}

	public void setParentNo(Integer parentNo) {
		this.parentNo = parentNo;
	}

	public Integer getOperatoNo() {
		return operatoNo;
	}

	public void setOperatoNo(Integer operatoNo) {
		this.operatoNo = operatoNo;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	@Override
	public String toString() {
		return "AdminMenu{" +
			"menuId=" + menuId +
			", menuName=" + menuName +
			", menuUrl=" + menuURL +
			", state=" + state +
			", parentNo=" + parentNo +
			", operatoNo=" + operatoNo +
			", sortOrder=" + sortOrder +
			"}";
	}

	public List<AdminMenu> getSubMenuList() {
		return subMenuList;
	}

	public void setSubMenuList(List<AdminMenu> subMenuList) {
		this.subMenuList = subMenuList;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public List<String> getBtnList() {
		return btnList;
	}

	public void setBtnList(List<String> btnList) {
		this.btnList = btnList;
	}

	public String getMenu_auth() {
		return menu_auth;
	}

	public void setMenu_auth(String menu_auth) {
		this.menu_auth = menu_auth;
	}


}
