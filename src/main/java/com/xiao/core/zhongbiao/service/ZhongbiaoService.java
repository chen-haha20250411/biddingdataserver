package com.xiao.core.zhongbiao.service;

import java.util.List;
import com.xiao.base.BaseService;
import com.xiao.core.zhongbiao.domain.Zhongbiao;

public interface ZhongbiaoService extends BaseService<Zhongbiao>{

	public boolean insertOupdate(Zhongbiao zhongbiao);
	
	public boolean insertList(List<Zhongbiao> addList);
	
	public boolean updateList(List<Zhongbiao> updateList);
}