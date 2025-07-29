package com.xiao.core.basic.sys_dict.service.impl;

import com.xiao.base.BaseMapper;
import com.xiao.base.BaseServiceImpl;
import com.xiao.core.basic.sys_dict.domain.Sys_dict;
import com.xiao.core.basic.sys_dict.mapper.Sys_dictMapper;
import com.xiao.core.basic.sys_dict.service.Sys_dictService;
import com.xiao.util.StringUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("sys_dictService")
public class Sys_dictServiceImpl extends BaseServiceImpl<Sys_dict> implements Sys_dictService {

	@Resource
	Sys_dictMapper<Sys_dict> sys_dictMapper;

	@Override
	public BaseMapper<Sys_dict> getMap() {
		return this.sys_dictMapper;
	}

	@Cacheable(value="dictList",key="'dict_'+#columnMap['parent_id']+'_'+#columnMap['dict_type']+'_'+#columnMap['dict_name']+'_'+#columnMap['rowNum']+'_'+#columnMap['rowIndex']+'_'+#columnMap['list']+'_'+#columnMap['dict_nameQuery']+'_'+#columnMap['dict_id']+'_'+#columnMap['idList']+'_'+#columnMap['dict_stat']")
	@Override
	public List<Sys_dict> queryByMap(Map<String, Object> columnMap){
		return sys_dictMapper.queryByMap(columnMap);
	}
	
	@Cacheable(value="dictList",key="'idlist_'+#idList")
	@Override
	public List<Sys_dict> queryByIds(List<String> idList){
		return sys_dictMapper.queryByIds(idList);
	}
	
	@CacheEvict(value= {"dictList"},allEntries=true)
	@Override
	public boolean deleteById(String id) {
		return retBool(sys_dictMapper.deleteById(id));
	}
	
	@CacheEvict(value= {"dictList"},allEntries=true)
	@Override
	public boolean insertOupdate(Sys_dict sys_dict) {
		boolean flag=true;
		int i = 0;
		if(!StringUtil.isEmpty(sys_dict.getDict_id())){
			i = sys_dictMapper.update(sys_dict);
		}else{
			i = sys_dictMapper.insert(sys_dict);
		}
		if(i<=0){
			flag=false;
		}
		return flag;
	}

	@CacheEvict(value= {"dictList"},allEntries=true)
	@Override
	public boolean insertList(List<Sys_dict> addList) {
		boolean flag=true;
		int i = 0;
		i = sys_dictMapper.insertList(addList);
		if(i<=0){
			flag=false;
		}
		return flag;
	}

	@CacheEvict(value= {"dictList"},allEntries=true)
	@Override
	public boolean updateList(List<Sys_dict> updateList) {
		boolean flag=true;
		int i = 0;
		i = sys_dictMapper.updateList(updateList);
		if(i<=0){
			flag=false;
		}
		return flag;
	}

	@Override
	public List<Sys_dict> queryBytypeIdaall(String dict_type) {
		return sys_dictMapper.queryBytypeIdaall(dict_type);
	}

	@Override
	public List<Sys_dict> queryBytypeIdParents(String parent_id) {
		return sys_dictMapper.queryBytypeIdParents(parent_id);
	}

}
