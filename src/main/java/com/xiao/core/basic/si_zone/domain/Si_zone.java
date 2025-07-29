package com.xiao.core.basic.si_zone.domain;

import java.io.Serializable;
import java.util.List;

public class Si_zone implements Serializable{
	
	private static final long serialVersionUID = 1L;

	//地区编号
   	private String zone_id;
	//地区号
   	private String zone_no;
	//地区名称
   	private String zone_name;
	//上级地区id
   	private String parent_id;
   	//上级地区编号
   	private String parent_code;
	//上级地区名称
   	private String parent_name;
	//地区级别 0省 1地区 2县市区 3乡镇街道
   	private String zone_level;
	//地区状态 0弃用 1启用
   	private String zone_stat;
	//备注
   	private String remark;
   	//
   	private boolean open;
   	
   	private boolean isParent;
   	
   	private List<Si_zone> childNode;

    public String getZone_id(){ 
    	return zone_id;
    }
    	
    public void setZone_id(String zone_id){
		this.zone_id=zone_id;
	}

    public String getZone_no(){ 
    	return zone_no;
    }
    	
    public void setZone_no(String zone_no){
		this.zone_no=zone_no;
	}

    public String getZone_name(){ 
    	return zone_name;
    }
    	
    public void setZone_name(String zone_name){
		this.zone_name=zone_name;
	}

    public String getParent_id(){ 
    	return parent_id;
    }
    	
    public void setParent_id(String parent_id){
		this.parent_id=parent_id;
	}

    public String getParent_name(){ 
    	return parent_name;
    }
    	
    
    public String getParent_code() {
		return parent_code;
	}

	public void setParent_code(String parent_code) {
		this.parent_code = parent_code;
	}

	public void setParent_name(String parent_name){
		this.parent_name=parent_name;
	}

    public String getZone_level(){ 
    	return zone_level;
    }
    	
    public void setZone_level(String zone_level){
		this.zone_level=zone_level;
	}

    public String getZone_stat(){ 
    	return zone_stat;
    }
    	
    public void setZone_stat(String zone_stat){
		this.zone_stat=zone_stat;
	}

    public String getRemark(){ 
    	return remark;
    }
    	
    public void setRemark(String remark){
		this.remark=remark;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public List<Si_zone> getChildNode() {
		return childNode;
	}

	public void setChildNode(List<Si_zone> childNode) {
		this.childNode = childNode;
	}



	public boolean isParent() {
		return isParent;
	}

	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}

	/*public Map<String,String> getFields() {
		Map<String,String> fieldMap = new HashMap<String,String>();
			fieldMap.put("zone_no","地区号");
			fieldMap.put("zone_name","地区名称");
			fieldMap.put("parent_id","上级地区编号");
			fieldMap.put("parent_name","上级地区名称");
			fieldMap.put("zone_level","地区级别 0省 1地区 2县市区 3乡镇街道");
			fieldMap.put("zone_stat","地区状态 0弃用 1启用");
			fieldMap.put("remark","备注");
		return fieldMap;
    }*/
}