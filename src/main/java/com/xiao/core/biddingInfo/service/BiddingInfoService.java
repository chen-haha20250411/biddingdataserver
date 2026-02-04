package com.xiao.core.biddingInfo.service;

import java.util.List;
import com.xiao.base.BaseService;
import com.xiao.core.biddingInfo.domain.BiddingInfo;

public interface BiddingInfoService extends BaseService<BiddingInfo>{

	public boolean insertOupdate(BiddingInfo biddingInfo);
	
	public boolean insertList(List<BiddingInfo> addList);
	
	public boolean updateList(List<BiddingInfo> updateList);
}