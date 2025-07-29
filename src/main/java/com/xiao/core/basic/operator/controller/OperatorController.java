package com.xiao.core.basic.operator.controller;


import com.xiao.base.BaseController;
import com.xiao.base.Page;
import com.xiao.base.ResultModel;
import com.xiao.constans.E;
import com.xiao.core.basic.admin_roleinfo.domain.AdminRoleinfo;
import com.xiao.core.basic.admin_roleinfo.service.AdminRoleinfoService;
import com.xiao.core.basic.operator.domain.Operator;
import com.xiao.core.basic.operator.service.OperatorService;
import com.xiao.logannotation.CurrentUser;
import com.xiao.logannotation.LoginRequired;
import com.xiao.util.MethodUtil;
import com.xiao.util.SessionUtils;
import com.xiao.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台操作员 前端控制器
 * </p>
 *
 * @author 裘晓伟
 * @since 2018-07-12
 */
@RestController
@RequestMapping("/admin/oper")
public class OperatorController extends BaseController{
	@Autowired
    OperatorService operService;
	@Autowired
    AdminRoleinfoService roleInfoService;

	@LoginRequired(remark="查询用户列表操作")
	@RequestMapping(value = "/list",method={RequestMethod.POST,RequestMethod.GET})
	public ResultModel left(@CurrentUser Operator oper, String loginName, String roleinfoId, Integer limit, Integer currPageNo) {
		Page<Operator> page=new Page<Operator>(currPageNo,limit);
		page.putQueryParam("loginName", loginName);
		page.putQueryParam("roleinfoId", roleinfoId);
		if(oper.getRoleinfoId() != 1 && oper.getRoleinfoId() != 3){
			page.putQueryParam("operator_id", oper.getOperatorId());
		}
		//查询总数
		int rowCount = operService.queryByCount(page.getQueryParams());
		page.setTotal(rowCount);
		List<Operator> operList = operService.queryByMap(page.getQueryParams());
		for(Operator operator :operList){
			operator.setLoginPwd(null);
		}
		// 查询�?有角�?
		page.setList(operList);
		//角色列表
		//page.setList(roleInfoService.queryByMap(page.getQueryParams()));
		page.putQueryParam("operRoleinfoId", oper.getRoleinfoId());
		return new ResultModel(true,"返回list数据").setData(page);
	}


	@RequestMapping(value = "/getjguser",method={RequestMethod.GET})
	public ResultModel getjguser() {
		Map<String,Object> map=new HashMap<>();
		map.put("roleinfoId","4");
		// 查询�?有角�?
		List<Operator> operList = operService.queryByMap(map);
//		map.put("roleinfoId","5");
//		// 查询�?有角�?
//		List<Operator> operList2 = operService.queryByMap(map);
//		operList.addAll(operList2);
		//角色列表
		return new ResultModel(true,"返回role数据").setData(operList);
	}

	@RequestMapping(value = "/getpguser",method={RequestMethod.GET})
	public ResultModel getpguser() {
		Map<String,Object> map=new HashMap<>();
		map.put("roleinfoId","5");
		// 查询�?有角�?
		List<Operator> operList = operService.queryByMap(map);
		//角色列表
		return new ResultModel(true,"返回role数据").setData(operList);
	}

	@LoginRequired(remark="查询用户角色操作")
	@RequestMapping(value = "/getroleinfoAll",method={RequestMethod.POST,RequestMethod.GET})
	public ResultModel getroleinfoAll() {
		// 查询�?有角�?
		List<AdminRoleinfo> roleInfoAll = operService.queryAllRole(null);//取到�?有角色信�?
		//角色列表
		return new ResultModel(true,"返回role数据").setData(roleInfoAll);
	}

	@LoginRequired(remark="查询客户操作")
	@RequestMapping(value = "/getroleinfoforgys",method={RequestMethod.POST,RequestMethod.GET})
	public ResultModel getroleinfoforgys() {
		// 所用用户
		List<Operator> operatorall = operService.queryUserForSup("4");//取到�?有角色信�?
		for(Operator oper :operatorall){
			oper.setLoginPwd(null);
		}
		//角色列表
		return new ResultModel(true,"返回role数据").setData(operatorall);
	}

	/** 去添加页�? */
	@RequestMapping(value = "/toAddOper")
	public ModelAndView toAddOper(HttpServletRequest req,HttpServletResponse resp, HttpSession session) {
		Map<String, Object> context = getRootMap();
		Operator oper=SessionUtils.getUser(req);
		context.put("oper", oper);

		return forword("sys/oper/addOper", context);
	}

	/**添加*/
	@LoginRequired(remark="查询用户列表操作")
	@RequestMapping(value = "/addOper",method={RequestMethod.POST,RequestMethod.GET})
	public ResultModel addOper(Operator oper){
		try {
			if(oper==null){
				return new ResultModel(false,"数据为空不允许添加");
			}
			int operRepeat = operService.queryRepeat(oper.getLoginName());
			if(operRepeat > 0){
				return new ResultModel(false,"该登录名已添加，请勿重复添加");
			}else{
				if(StringUtil.isEmpty(oper.getRealName())){
					return new ResultModel(false,"真实姓名不能为空");
				}
				if(StringUtil.isEmpty(oper.getLoginName())){
					return new ResultModel(false,"登录名不能为空");
				}
				if(StringUtil.isEmpty(oper.getLoginPwd())){
					return new ResultModel(false,"密码不能为空");
				}
				if(StringUtil.isEmpty(oper.getFailTimes())){
					oper.setFailTimes(null);
				}
				oper.setLoginPwd(MethodUtil.MD5(oper.getLoginPwd()));
				operService.insert(oper);
				return new ResultModel(true,"添加成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("添加异常:"+e.getMessage(),e);
			return new ResultModel(false,"添加失败");
		}

	}

	/**删除*/
	@LoginRequired(remark="查询用户列表操作")
	@RequestMapping(value = "/delOper",method={RequestMethod.POST,RequestMethod.GET})
	public ResultModel delOper(@RequestParam @NotNull(message= E.E0) String operatorId){
		try {
			List<String> operatorIds =new ArrayList<String>();
			if(!StringUtil.isEmpty(operatorId)){
				/*String[] idArray =operatorId.split(",");
				for(int i=0;i<idArray.length;i++){
					operatorIds.add(idArray[i]);
				}*/
				operatorIds.add(operatorId);
			}
			operService.deletes(operatorIds);

			return new ResultModel(true,"修改");
		} catch (Exception e) {
			log.error("删除异常:"+e.getMessage(),e);
			return new ResultModel(false,"网络异常，修改失败");
		}

	}

	/** 去修改页�? */
	@RequestMapping(value = "/toUpdateOper",method={RequestMethod.POST,RequestMethod.GET})
	public ResultModel toUpdateOper(@RequestParam String operatorId) {
		Operator oper = operService.queryById(operatorId);
		oper.setLoginPwd(null);
		return sendSuccessMessage("成功").setData(oper);
	}


	/** 去修改密码页�? */
	@RequestMapping(value = "/toUpdOperPwd")
	public ModelAndView toUpdOperPwd(HttpServletRequest req,HttpServletResponse resp, HttpSession session) {
		Map<String, Object> context = getRootMap();
		Operator oper=SessionUtils.getUser(req);
		context.put("oper", oper);
		context.put("menuName1", "账号设置");
		context.put("menuName", "修改密码");
		return forword("sys/oper/updateOperPwd", context);
	}

	/**修改*/
	@LoginRequired(remark="操作员修改操作")
	@RequestMapping(value = "/updateOper",method={RequestMethod.POST,RequestMethod.GET})
	public ResultModel updateOper(@CurrentUser Operator operter, Operator oper){
		try {
			if(!StringUtil.isEmpty(oper.getLoginPwd())){
				oper.setLoginPwd(MethodUtil.MD5(oper.getLoginPwd()));
			}
			if(StringUtil.isEmpty(oper.getFailTimes())){
				oper.setFailTimes(null);
			}
			operService.update(oper);
			return new ResultModel(true,"修改成功");
		} catch (Exception e) {
			log.error("删除异常:"+e.getMessage(),e);
			return new ResultModel(false,"网络异常，修改失败");
		}

	}

	/**修改*/
	@LoginRequired(remark="操作员重置密码操作")
	@RequestMapping(value = "/updatePwd",method={RequestMethod.POST,RequestMethod.GET})
	public ResultModel updatePwd(@CurrentUser Operator operter,@RequestParam String oldPassword,@RequestParam String newPassword ){
		try {
			if(!operter.getLoginPwd().equals(MethodUtil.MD5(oldPassword))){
				return new ResultModel(false,"原密码错误");
			}
			operter.setLoginPwd(MethodUtil.MD5(newPassword));
			operService.update(operter);
			return new ResultModel(true,"修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("删除异常:"+e.getMessage(),e);
			return new ResultModel(false,"网络异常，修改失败");
		}

	}

	/** 去授权页�? */
	@RequestMapping(value = "/toGrant")
	public ModelAndView toGrant(HttpServletRequest req,HttpServletResponse resp, HttpSession session) {
		String operatorId = req.getParameter("operatorId");
		Map<String, Object> context = getRootMap();
		//获取�?有权�?
		Map<String, Object> columnMap = new HashMap<String, Object>();
		List<AdminRoleinfo> roleinfoall=roleInfoService.queryallRoleInfo();
		//查询用户的原权限
		Operator oper1 = operService.queryById(operatorId);

		context.put("checkRole", oper1.getRoleinfoId());
		context.put("loginName", oper1.getLoginName());
		context.put("roleinfoall", roleinfoall);

		context.put("operatorId", operatorId);

		Operator oper = SessionUtils.getUser(req);
		context.put("oper", oper);
		context.put("menuName1", "系统设置");
		context.put("menuName", "角色授权");
		return forword("sys/oper/accredit", context);
	}

	/** 授权*/
	@LoginRequired(remark="操作员授权操作")
	@RequestMapping(value = "/Grant",method={RequestMethod.POST,RequestMethod.GET})
	public ResultModel Grant(Operator oper) {
		try {
			String roleInfoId =oper.getRoleinfoId()+"";
			String operatorId = oper.getOperatorId()+"";
			Operator operator=operService.queryById(operatorId);
			if(operator==null){
				return new ResultModel(false,"无操作人员");
			}
			if(StringUtil.isEmpty(roleInfoId)){
				return new ResultModel(false,"未选择角色人员");
			}
			operator.setRoleinfoId(Integer.parseInt(roleInfoId));
			operService.update(operator);
			return new ResultModel(true,"授权成功");
		} catch (Exception e) {
			return new ResultModel(false,"网络故障授权失败");
		}

	}
	
	/** 重置密码*/
	@LoginRequired(remark="重置密码操作")
	@RequestMapping(value = "/resetPassword")
	public ResultModel resetPassword(Integer operatorId) {
		try {
			Operator oper = new Operator();
			oper.setOperatorId(operatorId);
			oper.setLoginPwd(MethodUtil.MD5("123456"));
			boolean flag = operService.update(oper);
			if(flag){
				return new ResultModel(true,"重置成功");
			}else{
				return new ResultModel(false,"重置失败，请重试");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("重置异常:"+e.getMessage(),e);
			return new ResultModel(false,"网络异常，操作失败");
		}

	}
	
	/**
	* 解除登陆限制
	* @param userinfo
	*/
	@LoginRequired(remark="解除登陆限制")
	@RequestMapping(value = "/removeLimit")
	public ResultModel removeLimit(Integer operatorId) {
		boolean flag=true;
		try {
			Operator oper = new Operator();
			oper.setOperatorId(operatorId);
			oper.setLastTime("empt");
			oper.setFailTimes("0");
			flag = operService.update(oper);
			if(flag){
				return new ResultModel(true,"解除成功");
			}else{
				return new ResultModel(false,"解除失败，请重试");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("解除异常:"+e.getMessage(),e);
			return new ResultModel(false,"网络异常，操作失败");
		}
	}

}
