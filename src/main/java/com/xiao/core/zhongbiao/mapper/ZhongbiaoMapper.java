package com.xiao.core.zhongbiao.mapper;

import com.xiao.base.BaseMapper;
import com.xiao.core.zhongbiao.domain.Zhongbiao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ZhongbiaoMapper<T> extends BaseMapper<T> {
	int insertList(List<T> addList);
	
	int updateList(List<T> updateList);

	BigDecimal queryTotalAmount(Map<String, Object> params);

	BigDecimal queryTotalAmountBydate(Map<String,Object> params);

	List<Zhongbiao> queryPageWithTotal(Map<String,Object> params);
}