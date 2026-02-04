package com.xiao.base;

import java.io.Serializable;

import com.xiao.util.StringUtil;

public class BaseDomain implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**添加时间*/
    private String addTime;
    /**最后更新时间*/
    private String editTime;
    
    /**分页参数****************************/
    
    /**分页起始行*/
    private int rowIndex;
    /**页大小*/
    private int rowNum;
    
	public String getAddTime() {
		return StringUtil.isEmpty(addTime)?addTime:addTime.substring(0, 19);
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public String getEditTime() {
		return StringUtil.isEmpty(editTime)?editTime:editTime.substring(0, 19);
	}
	public void setEditTime(String editTime) {
		this.editTime = editTime;
	}
	public int getRowIndex() {
		return rowIndex;
	}
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}
	public int getRowNum() {
		return rowNum;
	}
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
	
}
