package com.xiao.core.basic.sys_dict.mapper;

import com.xiao.base.BaseMapper;

import java.util.List;

public interface Sys_dictMapper<T> extends BaseMapper<T> {
	int insertList(List<T> addList);

	int updateList(List<T> updateList);

	List<T> queryBytypeIdaall(String dict_type);

	List<T> queryBytypeIdParents(String parent_id);
}
