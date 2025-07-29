package com.xiao.core.basic.si_zone.mapper;

import com.xiao.base.BaseMapper;
import com.xiao.core.basic.si_zone.domain.Si_zone;

import java.util.List;
import java.util.Map;



public interface Si_zoneMapper<T> extends BaseMapper<T> {
	
	public boolean deleteByParentId(String id);
	
	public boolean addBatch(List<Si_zone> list);
	
	public boolean updBatch(List<Si_zone> list);
	
	public boolean updateParentId(List<String> list);
	
	Si_zone queryByName(Map<String, Object> columnMap);
}