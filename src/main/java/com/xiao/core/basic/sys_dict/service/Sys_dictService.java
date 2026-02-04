package com.xiao.core.basic.sys_dict.service;

import com.xiao.base.BaseService;
import com.xiao.core.basic.sys_dict.domain.Sys_dict;

import java.util.List;

public interface Sys_dictService extends BaseService<Sys_dict>{

	public boolean insertOupdate(Sys_dict sys_dict);

	public boolean insertList(List<Sys_dict> addList);

	public boolean updateList(List<Sys_dict> updateList);

	List<Sys_dict> queryBytypeIdaall(String dict_type);

	List<Sys_dict> queryBytypeIdParents(String parent_id);
}
