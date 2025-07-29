package com.xiao.core.zhongbiao.controller;
import java.util.List;

import com.xiao.base.BaseController;
import com.xiao.base.Page;
import com.xiao.base.ResultModel;
import com.xiao.constans.E;
import com.xiao.core.basic.operator.domain.Operator;
import com.xiao.logannotation.CurrentUser;
import com.xiao.logannotation.LoginRequired;
import com.xiao.util.StringUtil;
import javax.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import com.xiao.core.zhongbiao.service.ZhongbiaoService;
import com.xiao.core.zhongbiao.domain.Zhongbiao;


@RestController
@RequestMapping("/admin/zhongbiao")
//@Api(tags = "ZhongbiaoController", description = "")
public class ZhongbiaoController extends BaseController{

	@Resource
	ZhongbiaoService zhongbiaoService;

	/**
	* 到主页面
	* @return
	*/
	@LoginRequired(remark="查询列表操作")
	@RequestMapping(value = "/toList",method={RequestMethod.POST,RequestMethod.GET})
	//@ApiOperation(value = "查询列表操作")
	public ResultModel toList(@CurrentUser Operator oper,String projectNo,String customer,String noticeType
			,String title,String winnerPrincipal, Integer limit, Integer currPageNo) {
		Page<Zhongbiao> page=new Page<Zhongbiao>(currPageNo,limit);
		//查询位置
		//page.putQueryParam("loginName", loginName);
		page.putQueryParam("projectNo", projectNo);
		page.putQueryParam("customer", customer);
		page.putQueryParam("noticeType", noticeType);
		page.putQueryParam("title", title);
		page.putQueryParam("winnerPrincipal", winnerPrincipal);
		//查询总数
		int rowCount = zhongbiaoService.queryByCount(page.getQueryParams());
		page.setTotal(rowCount);
		List<Zhongbiao> zhongbiaoList = zhongbiaoService.queryByMap(page.getQueryParams());
		//查询结果
		page.setList(zhongbiaoList);
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
			zhongbiaoService.deleteById(id);
			return new ResultModel(true, "删除成功");
		}catch (Exception e) {
			e.printStackTrace();
			log.error("删除异常:"+e.getMessage());
			return new ResultModel(false,"网络异常，操作失败");
		}
	}

	/**
	* 修改或保存
	* @param zhongbiao
	*/
	@LoginRequired(remark="添加修改信息")
	//@ApiOperation(value = "添加修改信息")
	@RequestMapping(value = "/insertOupdate",method={RequestMethod.POST})
	public ResultModel insertOupdate( Zhongbiao zhongbiao) {
		boolean flag=true;
		try {
			flag=zhongbiaoService.insertOupdate(zhongbiao);
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
		Zhongbiao zhongbiao = zhongbiaoService.queryById(id);
		return sendSuccessMessage("成功").setData(zhongbiao);
	}
	
}
