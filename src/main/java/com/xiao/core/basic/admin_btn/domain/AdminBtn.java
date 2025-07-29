package com.xiao.core.basic.admin_btn.domain;

import com.xiao.base.BaseDomain;

/**
 * <p>
 * 权限按钮表
 * </p>
 *
 * @author 裘晓伟
 * @since 2018-07-12
 */
public class AdminBtn extends BaseDomain{

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 自增主键
     */
	private Integer btnId;
    /**
     * 菜单Id
     */
	private Integer menuId;
    /**
     * 菜单名称
     */
	private String btnName;
    /**
     * 菜单类型
     */
	private String btnType;
    /**
     * 备注
     */
	private String remark;

	private String menuURL;


	public Integer getBtnId() {
		return btnId;
	}

	public void setBtnId(Integer btnId) {
		this.btnId = btnId;
	}

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public String getBtnName() {
		return btnName;
	}

	public void setBtnName(String btnName) {
		this.btnName = btnName;
	}

	public String getBtnType() {
		return btnType;
	}

	public void setBtnType(String btnType) {
		this.btnType = btnType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "AdminBtn{" +
			"btnId=" + btnId +
			", menuId=" + menuId +
			", btnName=" + btnName +
			", btnType=" + btnType +
			", remark=" + remark +
			"}";
	}

	public String getMenuURL() {
		return menuURL;
	}

	public void setMenuURL(String menuURL) {
		this.menuURL = menuURL;
	}
}
