package com.xiao.core.zhongbiao.mapper;

import com.xiao.base.BaseMapper;
import java.util.List;

public interface ZhongbiaoMapper<T> extends BaseMapper<T> {
	int insertList(List<T> addList);
	
	int updateList(List<T> updateList);
}