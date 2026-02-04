package com.xiao.core.basic.sys_dict.domain;

import java.util.List;

import com.xiao.base.BaseDomain;


public class SiDictTree extends BaseDomain{
	
	private static final long serialVersionUID = 1L;

   	private String value;
   	private String label;
   	private List<SiDictTree> children;
   	private String level;
   	private String parentId;
   	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public List<SiDictTree> getChildren() {
		return children;
	}
	public void setChildren(List<SiDictTree> children) {
		this.children = children;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
   	
}