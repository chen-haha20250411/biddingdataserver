package com.xiao.core.biddingInfo.service.impl;

import com.xiao.core.biddingInfo.domain.BiddingInfo;
import org.springframework.stereotype.Service;
import com.xiao.base.BaseMapper;
import com.xiao.base.BaseServiceImpl;
import com.xiao.util.StringUtil;
import java.util.List;
import javax.annotation.Resource;
import com.xiao.core.biddingInfo.domain.BiddingInfo;
import com.xiao.core.biddingInfo.mapper.BiddingInfoMapper;
import com.xiao.core.biddingInfo.service.BiddingInfoService;

@Service("biddingInfoService")
public class BiddingInfoServiceImpl extends BaseServiceImpl<BiddingInfo> implements BiddingInfoService {

	@Resource
	BiddingInfoMapper<BiddingInfo> biddingInfoMapper;

	@Override
	public BaseMapper<BiddingInfo> getMap() {
		return this.biddingInfoMapper;
	}

	@Override
	public boolean insertOupdate(BiddingInfo biddingInfo) {
		boolean flag=true;
		int i = 0;
		if(!StringUtil.isEmpty(biddingInfo.getId())){
			i = biddingInfoMapper.update(biddingInfo);
		}else{
			i = biddingInfoMapper.insert(biddingInfo);
		}
		if(i<=0){
			flag=false;
		}
		return flag;
	}

	@Override
	public boolean insertList(List<BiddingInfo> addList) {
		boolean flag=true;
		int i = biddingInfoMapper.insertList(addList);
		if(i<=0){
			flag=false;
		}
		return flag;
	}

	@Override
	public boolean updateList(List<BiddingInfo> updateList) {
		boolean flag=true;
		int i = biddingInfoMapper.updateList(updateList);
		if(i<=0){
			flag=false;
		}
		return flag;
	}
}
