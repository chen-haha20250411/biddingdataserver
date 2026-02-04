package com.xiao.core.basic.sys_dict.domain;

import com.xiao.base.BaseDomain;

public class Sys_dict extends BaseDomain{

	private static final long serialVersionUID = 1L;

	//主键
   	private String dict_id;
	//数据字典名称
   	private String dict_name;
   	//父节点id
	private String parent_id;
	//该字段的父节点存储在静态变量类中
   	private String dict_type;
	//字典顺序
   	private String dict_order;
	//字典状态 0弃用 1启用
   	private String dict_stat;
	//参数名称
   	private String param_name;
	//添加时间
   	private String addtime;
	//修改时间
   	private String edittime;
	//创建人
   	private String add_operid;
	//修改人
   	private String edit_operid;
   	//税率
   	private String tax;
   	//服务类型编码
   	private String typeCode;
   	
    public String getDict_id(){
    	return dict_id;
    }

    public void setDict_id(String dict_id){
		this.dict_id=dict_id;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getDict_name(){
    	return dict_name;
    }

    public void setDict_name(String dict_name){
		this.dict_name=dict_name;
	}

    public String getDict_type(){
    	return dict_type;
    }

    public void setDict_type(String dict_type){
		this.dict_type=dict_type;
	}

    public String getDict_order(){
    	return dict_order;
    }

    public void setDict_order(String dict_order){
		this.dict_order=dict_order;
	}

    public String getDict_stat(){
    	return dict_stat;
    }

    public void setDict_stat(String dict_stat){
		this.dict_stat=dict_stat;
	}

    public String getParam_name(){
    	return param_name;
    }

    public void setParam_name(String param_name){
		this.param_name=param_name;
	}

    public String getAddtime(){
    	return addtime;
    }

    public void setAddtime(String addtime){
		this.addtime=addtime;
	}

    public String getEdittime(){
    	return edittime;
    }

    public void setEdittime(String edittime){
		this.edittime=edittime;
	}

    public String getAdd_operid(){
    	return add_operid;
    }

    public void setAdd_operid(String add_operid){
		this.add_operid=add_operid;
	}

    public String getEdit_operid(){
    	return edit_operid;
    }

    public void setEdit_operid(String edit_operid){
		this.edit_operid=edit_operid;
	}

	public String getTax() {
		return tax;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

}
