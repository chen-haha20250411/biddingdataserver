package com.xiao.util;

import java.util.List;

/**
 * 说明：ztree节点信息
 * 
 * @author 赵增丰
 * @version 1.0 2014-8-22 上午11:17:31
 */
public class TreeNode {

	private String id;
	private String pId;// 父节点
	private String name;
	private Boolean checked;
	private Boolean open;
	private String icon;// 节点图标
	private String iconOpen;// 节点图标
	private String iconClose;// 节点图标
	private Boolean isParent;
	private String zone_level;
	private String zone_code;
	private List<TreeNode> children;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean isChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public Boolean isOpen() {
		return open;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public Boolean getChecked() {
		return checked;
	}

	public Boolean getOpen() {
		return open;
	}

	public TreeNode(String id, String pId, String name, Boolean checked,
			Boolean open, List<TreeNode> children) {
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.checked = checked;
		this.open = open;
		this.children = children;
	}

	public TreeNode() {
		super();
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getIconOpen() {
		return iconOpen;
	}

	public void setIconOpen(String iconOpen) {
		this.iconOpen = iconOpen;
	}

	public String getIconClose() {
		return iconClose;
	}

	public void setIconClose(String iconClose) {
		this.iconClose = iconClose;
	}


	public String getZone_level() {
		return zone_level;
	}

	public void setZone_level(String zone_level) {
		this.zone_level = zone_level;
	}

	public Boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}

	public String getZone_code() {
		return zone_code;
	}

	public void setZone_code(String zone_code) {
		this.zone_code = zone_code;
	}


}
