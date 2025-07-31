package com.xiao.core.zhongbiao.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.xiao.base.BaseService;
import com.xiao.core.zhongbiao.domain.Zhongbiao;

public interface ZhongbiaoService extends BaseService<Zhongbiao>{

	public boolean insertOupdate(Zhongbiao zhongbiao);
	
	public boolean insertList(List<Zhongbiao> addList);
	
	public boolean updateList(List<Zhongbiao> updateList);

	// 查询总金额
	BigDecimal queryTotalAmount(Map<String, Object> params);

	BigDecimal queryTotalAmonutByDate(Map<String,Object> params);

	List queryPageWithTotal(Map<String,Object> params);
}