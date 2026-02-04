package com.xiao.core.basic.si_zone.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.xiao.base.BaseMapper;
import com.xiao.base.BaseServiceImpl;
import com.xiao.core.basic.si_zone.domain.Si_zone;
import com.xiao.core.basic.si_zone.mapper.Si_zoneMapper;
import com.xiao.core.basic.si_zone.service.Si_zoneService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service("si_zoneService")
public class Si_zoneServiceImpl extends BaseServiceImpl<Si_zone> implements Si_zoneService {

	@Resource
	Si_zoneMapper<Si_zone> si_zoneMapper;

	@Override
	public BaseMapper<Si_zone> getMap() {
		return this.si_zoneMapper;
	}
	

	@Override
    public boolean insert(Si_zone si_zpne){
		return retBool(si_zoneMapper.insert(si_zpne));
	}

	

	@Override
	public boolean update(Si_zone entity){
		return retBool(si_zoneMapper.update(entity));
	}

	@Override
	public List<Si_zone> queryByMap(Map<String, Object> columnMap){
		return si_zoneMapper.queryByMap(columnMap);
	}
	

	@Override
	public Si_zone queryById(String id){
		return si_zoneMapper.queryById(id);
	}
	

	@Override
	public boolean deleteById(String id){
		boolean flag=true;
		Si_zone si_zone =si_zoneMapper.queryById(id);
		try{
			if("3".equals(si_zone.getZone_level())){
				si_zoneMapper.deleteById(id);
			}else{
				si_zoneMapper.deleteById(id);
				si_zoneMapper.deleteByParentId(id);
			}
		}catch(Exception e){
			 flag=false;
		}
		return flag;
		
	}	

	@Override
	public boolean addList(Map<String,Set<Si_zone>> zoneMap){
	   boolean flag=true;
	   try{
		   Set<Si_zone> addSet=zoneMap.get("addList");
		   List<String> strList = new ArrayList<String>();
		   if(addSet !=null && addSet.size()>0){
			   List<Si_zone> addList=new ArrayList<Si_zone>();
			   addList.addAll(addSet);
			   flag=si_zoneMapper.addBatch(addList);
			   for(Si_zone zone:addList){
				   strList.add(zone.getZone_no());
			   }
			   flag=si_zoneMapper.updateParentId(strList);
		   }
		   Set<Si_zone> updSet=zoneMap.get("updList");
		   if(updSet !=null && updSet.size()>0){
			   List<Si_zone> updList=new ArrayList<Si_zone>();
			   updList.addAll(updSet);
			   flag=si_zoneMapper.updBatch(updList);
		   }

	   }catch(Exception e){
		   flag=false;
	   }
	   return flag;
	}

	@Override
	public Si_zone queryByName(Map<String, Object> columnMap) {
		return si_zoneMapper.queryByName(columnMap);
	}
}