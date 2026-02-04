package com.xiao.core.biddingInfo.mapper;

import com.xiao.base.BaseMapper;
import java.util.List;

public interface BiddingInfoMapper<T> extends BaseMapper<T> {
	int insertList(List<T> addList);
	
	int updateList(List<T> updateList);
}