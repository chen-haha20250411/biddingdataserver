package com.xiao.core.basic.operator_log.controller;


import com.xiao.base.BaseController;
import com.xiao.base.Page;
import com.xiao.base.ResultModel;
import com.xiao.core.basic.operator.domain.Operator;
import com.xiao.core.basic.operator_log.domain.OperatorLog;
import com.xiao.core.basic.operator_log.service.OperatorLogService;
import com.xiao.logannotation.CurrentUser;
import com.xiao.logannotation.LoginRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台操作日志表 前端控制器
 * </p>
 *
 * @author 裘晓伟
 * @since 2018-07-12
 */
@RestController
@RequestMapping("/admin/operLog")
public class OperatorLogController extends BaseController{
	@Autowired
	OperatorLogService logService;

	@LoginRequired
	@RequestMapping(value = "/list",method={RequestMethod.POST,RequestMethod.GET})
	public ResultModel list(@CurrentUser Operator oper, String loginName, String startTime, String endTime, Integer limit, Integer currPageNo) {
//		Map<String, Object> context = getRootMap();
//		Operator oper = SessionUtils.getUser(req);
//		//查询总数
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("loginName", req.getParameter("loginName"));
//		params.put("startTime", req.getParameter("startTime"));
//		params.put("endTime", req.getParameter("endTime"));
//
//		int rowCount = logService.queryByCount(params);
//		//设置分页信息
//		setPage(req,context,rowCount,params);
//
//		//角色列表
//		List<OperatorLog> logList = logService.queryByMap(params);
//
//		context.put("logList", logList);
//		context.put("loginName", req.getParameter("loginName"));
//		context.put("startTime", req.getParameter("startTime"));
//		context.put("endTime", req.getParameter("endTime"));
//
//		context.put("oper", oper);
//		context.put("menuName1", "系统设置");
//		context.put("menuName", "系统日志");
//		return forword("sys/oper/logList", context);

		Page<OperatorLog> page=new Page<OperatorLog>(currPageNo,limit);
		page.putQueryParam("loginName", loginName);
		page.putQueryParam("startTime", startTime);
		page.putQueryParam("endTime", endTime);
		int rowCount = logService.queryByCount(page.getQueryParams());
		page.setTotal(rowCount);
		//角色列表
		page.setList(logService.queryByMap(page.getQueryParams()));
		return new ResultModel(true,"返回list数据").setData(page);
	}


	@LoginRequired
	@RequestMapping(value = "/excelList",method={RequestMethod.POST,RequestMethod.GET})
	public ResultModel excelList(@CurrentUser Operator oper, String loginName, String startTime, String endTime) {
		Map<String, Object> queryParams =new HashMap<String, Object>();
		queryParams.put("loginName", loginName);
		queryParams.put("startTime", startTime);
		queryParams.put("endTime", endTime);
		List<OperatorLog> list=logService.queryByMap(queryParams);
		return new ResultModel(true,"返回list数据").setData(list);
	}
}
