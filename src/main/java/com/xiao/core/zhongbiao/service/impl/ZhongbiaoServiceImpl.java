package com.xiao.core.zhongbiao.service.impl;

import org.springframework.stereotype.Service;
import com.xiao.base.BaseMapper;
import com.xiao.base.BaseServiceImpl;
import com.xiao.util.StringUtil;
import java.util.List;
import javax.annotation.Resource;
import com.xiao.core.zhongbiao.domain.Zhongbiao;
import com.xiao.core.zhongbiao.mapper.ZhongbiaoMapper;
import com.xiao.core.zhongbiao.service.ZhongbiaoService;

@Service("zhongbiaoService")
public class ZhongbiaoServiceImpl extends BaseServiceImpl<Zhongbiao> implements ZhongbiaoService {

	@Resource
	ZhongbiaoMapper<Zhongbiao> zhongbiaoMapper;

	@Override
	public BaseMapper<Zhongbiao> getMap() {
		return this.zhongbiaoMapper;
	}

	@Override
	public boolean insertOupdate(Zhongbiao zhongbiao) {
		boolean flag=true;
		int i = 0;
		if(!StringUtil.isEmpty(zhongbiao.getId())){
			i = zhongbiaoMapper.update(zhongbiao);
		}else{
			i = zhongbiaoMapper.insert(zhongbiao);
		}
		if(i<=0){
			flag=false;
		}
		return flag;
	}

	@Override
	public boolean insertList(List<Zhongbiao> addList) {
		boolean flag=true;
		int i = zhongbiaoMapper.insertList(addList);
		if(i<=0){
			flag=false;
		}
		return flag;
	}

	@Override
	public boolean updateList(List<Zhongbiao> updateList) {
		boolean flag=true;
		int i = zhongbiaoMapper.updateList(updateList);
		if(i<=0){
			flag=false;
		}
		return flag;
	}
}
