package com.xiao.core.biddingInfo.controller;
import java.util.List;

import com.xiao.base.BaseController;
import com.xiao.base.Page;
import com.xiao.base.ResultModel;
import com.xiao.constans.E;
import com.xiao.core.basic.operator.domain.Operator;
import com.xiao.core.biddingInfo.service.BiddingInfoService;
import com.xiao.logannotation.CurrentUser;
import com.xiao.logannotation.LoginRequired;
import javax.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import com.xiao.core.biddingInfo.domain.BiddingInfo;


@RestController
@RequestMapping("/admin/biddingInfo")
//@Api(tags = "BiddingInfoController", description = "")
public class BiddingInfoController extends BaseController{

	@Resource
	BiddingInfoService biddingInfoService;

	/**
	* 到主页面
	* @return
	*/
	@LoginRequired(remark="查询列表操作")
	@RequestMapping(value = "/toList",method={RequestMethod.POST,RequestMethod.GET})
	//@ApiOperation(value = "查询列表操作")
	public ResultModel toList(@CurrentUser Operator oper,String projectNumber,String projectName,String descase, Integer limit, Integer currPageNo) {
		Page<BiddingInfo> page=new Page<BiddingInfo>(currPageNo,limit);
		//查询位置
		page.putQueryParam("projectNumber", projectNumber);
		page.putQueryParam("projectName", projectName);
		//查询总数
		int rowCount = biddingInfoService.queryByCount(page.getQueryParams());
		page.setTotal(rowCount);
		List<BiddingInfo> biddingInfoList = biddingInfoService.queryByMap(page.getQueryParams());
		//查询结果
		page.setList(biddingInfoList);
		return new ResultModel(true,"返回list数据").setData(page);
	}

	/**
	* 删除
	*/
	@LoginRequired(remark="删除操作")
	//@ApiOperation(value = "删除操作")
	@RequestMapping(value = "/deletevalue",method={RequestMethod.POST})
	public ResultModel deletevalue(@RequestParam @NotNull(message=E.E0) String id) {
		try {
			biddingInfoService.deleteById(id);
			return new ResultModel(true, "删除成功");
		}catch (Exception e) {
			e.printStackTrace();
			log.error("删除异常:"+e.getMessage());
			return new ResultModel(false,"网络异常，操作失败");
		}
	}

	/**
	* 修改或保存
	* @param biddingInfo
	*/
	@LoginRequired(remark="添加修改信息")
	//@ApiOperation(value = "添加修改信息")
	@RequestMapping(value = "/insertOupdate",method={RequestMethod.POST})
	public ResultModel insertOupdate( BiddingInfo biddingInfo) {
		boolean flag=true;
		try {
			flag=biddingInfoService.insertOupdate(biddingInfo);
			if(flag){
				return new ResultModel(true,"保存成功");
			}else{
				return new ResultModel(false,"保存失败，请重试");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("删除异常:"+e.getMessage());
			return new ResultModel(false,"网络异常，操作失败");
		}
	}
	/**
	* 查询单条记录
	* @param id
	*/
	@LoginRequired(remark="查询单条记录")
	//@ApiOperation(value = "查询单条记录")
	@RequestMapping(value = "/queryById",method={RequestMethod.GET})
	public ResultModel queryById(@RequestParam @NotNull(message=E.E0) String id) {
		BiddingInfo biddingInfo = biddingInfoService.queryById(id);
		return sendSuccessMessage("成功").setData(biddingInfo);
	}
	
}
