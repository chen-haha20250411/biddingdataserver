package com.xiao.core.basic.si_zone.service;

import com.xiao.base.BaseService;
import com.xiao.core.basic.si_zone.domain.Si_zone;

import java.util.Map;
import java.util.Set;


public interface Si_zoneService extends BaseService<Si_zone> {
	
	public boolean addList(Map<String, Set<Si_zone>> zoneMap);
	
	Si_zone queryByName(Map<String, Object> columnMap);
	
}