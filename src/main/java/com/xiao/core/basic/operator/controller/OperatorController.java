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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
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

	/**
	 * 查询用户列表 - 适配 Vue3
	 * GET/POST /admin/oper/list?page=1&limit=10&loginName=xxx&roleinfoId=1
	 */
	@LoginRequired(remark="查询用户列表操作")
	@RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
	public ResultModel list(@CurrentUser Operator oper, 
						@RequestParam(defaultValue = "1") Integer page,
						@RequestParam(defaultValue = "10") Integer limit,
						@RequestParam(required = false) String loginName, 
						@RequestParam(required = false) String roleinfoId,
						@RequestParam(value = "currPageNo", required = false) Integer currPageNo,
						@RequestParam(required = false) Integer rowNum) {
		// 优先使用 currPageNo，兼容前端参数名
		if (currPageNo != null) {
			page = currPageNo;
		}
		if (rowNum != null) {
			limit = rowNum;
		}
		
		Page<Operator> pageObj = new Page<Operator>(page, limit);
		pageObj.putQueryParam("loginName", loginName);
		pageObj.putQueryParam("roleinfoId", roleinfoId);
		if(oper.getRoleinfoId() != 1 && oper.getRoleinfoId() != 3){
			pageObj.putQueryParam("operator_id", oper.getOperatorId());
		}
		//查询总数
		int rowCount = operService.queryByCount(pageObj.getQueryParams());
		pageObj.setTotal(rowCount);
		List<Operator> operList = operService.queryByMap(pageObj.getQueryParams());
		for(Operator operator : operList){
			operator.setLoginPwd(null);
		}
		pageObj.setList(operList);
		pageObj.putQueryParam("operRoleinfoId", oper.getRoleinfoId());
		// 使用 vue-element-admin 兼容的分页格式
		return com.xiao.util.PageUtils.pageSuccess(pageObj);
	}

	/**
	 * 查询用户列表（包含分支机构、部门、数据角色信息）- 适配 Vue3
	 * GET/POST /admin/oper/listWithDetails?page=1&limit=10&loginName=xxx&roleinfoId=1
	 */
	@LoginRequired(remark="查询用户列表详情操作")
	@RequestMapping(value = "/listWithDetails", method = {RequestMethod.GET, RequestMethod.POST})
	public ResultModel listWithDetails(@CurrentUser Operator oper, 
						@RequestParam(defaultValue = "1") Integer page,
						@RequestParam(defaultValue = "10") Integer limit,
						@RequestParam(required = false) String loginName, 
						@RequestParam(required = false) String roleinfoId,
						@RequestParam(value = "currPageNo", required = false) Integer currPageNo,
						@RequestParam(required = false) Integer rowNum) {
		if (currPageNo != null) {
			page = currPageNo;
		}
		if (rowNum != null) {
			limit = rowNum;
		}
		
		Page<com.xiao.core.basic.operator.domain.OperatorListDTO> pageObj = new Page<>(page, limit);
		pageObj.putQueryParam("loginName", loginName);
		pageObj.putQueryParam("roleinfoId", roleinfoId);
		if(oper.getRoleinfoId() != 1 && oper.getRoleinfoId() != 3){
			pageObj.putQueryParam("operator_id", oper.getOperatorId());
		}
		
		int rowCount = operService.queryByCount(pageObj.getQueryParams());
		pageObj.setTotal(rowCount);
		
		List<com.xiao.core.basic.operator.domain.OperatorListDTO> operList = operService.getOperatorListWithDetails(pageObj.getQueryParams());
		pageObj.setList(operList);
		pageObj.putQueryParam("operRoleinfoId", oper.getRoleinfoId());
		
		return com.xiao.util.PageUtils.pageSuccess(pageObj);
	}


	@RequestMapping(value = "/getjguser",method={RequestMethod.GET})
	public ResultModel getjguser() {
		Map<String,Object> map=new HashMap<>();
		map.put("roleinfoId","4");
		// 查询所有角色
		List<Operator> operList = operService.queryByMap(map);
//		map.put("roleinfoId","5");
//		// 查询所有角色
//		List<Operator> operList2 = operService.queryByMap(map);
//		operList.addAll(operList2);
		//角色列表
		return new ResultModel(true,"返回role数据").setData(operList);
	}

	@RequestMapping(value = "/getpguser",method={RequestMethod.GET})
	public ResultModel getpguser() {
		Map<String,Object> map=new HashMap<>();
		map.put("roleinfoId","5");
		// 查询所有角色
		List<Operator> operList = operService.queryByMap(map);
		//角色列表
		return new ResultModel(true,"返回role数据").setData(operList);
	}

	@LoginRequired(remark="查询用户角色操作")
	@RequestMapping(value = "/getroleinfoAll",method={RequestMethod.POST,RequestMethod.GET})
	public ResultModel getroleinfoAll() {
		// 查询所有角色
		List<AdminRoleinfo> roleInfoAll = operService.queryAllRole(null);//取到所有角色信息
		//角色列表
		return new ResultModel(true,"返回role数据").setData(roleInfoAll);
	}

	@LoginRequired(remark="查询客户操作")
	@RequestMapping(value = "/getroleinfoforgys",method={RequestMethod.POST,RequestMethod.GET})
	public ResultModel getroleinfoforgys() {
		// 所用用户
		List<Operator> operatorall = operService.queryUserForSup("4");//取到所有角色信息
		for(Operator oper :operatorall){
			oper.setLoginPwd(null);
		}
		//角色列表
		return new ResultModel(true,"返回role数据").setData(operatorall);
	}

	/**
	 * 获取考核对象数据
	 * GET /admin/oper/getAssessmentTargets
	 */
	@LoginRequired(remark="获取考核对象数据操作")
	@RequestMapping(value = "/getAssessmentTargets",method={RequestMethod.GET})
	public ResultModel getAssessmentTargets() {
		Map<String,Object> map=new HashMap<>();
		map.put("isAssessmentTarget", true);
		// 查询所有考核对象
		List<Operator> operList = operService.queryByMap(map);
		for(Operator oper : operList){
			oper.setLoginPwd(null);
		}
		//返回考核对象列表
		return new ResultModel(true,"返回考核对象数据").setData(operList);
	}

	/** 去添加页 */
	@RequestMapping(value = "/toAddOper")
	public ModelAndView toAddOper(HttpServletRequest req,HttpServletResponse resp, HttpSession session) {
		Map<String, Object> context = getRootMap();
		Operator oper=SessionUtils.getUser(req);
		context.put("oper", oper);

		return forword("sys/oper/addOper", context);
	}

	/**
	 * 添加用户 - 适配 Vue3 (JSON 请求)
	 * POST /admin/oper
	 */
	@LoginRequired(remark="添加用户操作")
	@PostMapping
	public ResultModel addOper(@RequestBody Operator oper){
		return addOperLogic(oper);
	}

	/**
	 * 添加用户 - POST 兼容旧版前端
	 * POST /admin/oper/addOper
	 */
	@LoginRequired(remark="添加用户操作")
	@PostMapping("/addOper")
	public ResultModel addOperPost(HttpServletRequest request){
		Operator oper = new Operator();
		oper.setRealName(request.getParameter("realName"));
		oper.setLoginName(request.getParameter("loginName"));
		oper.setLoginPwd(request.getParameter("loginPwd"));
		oper.setPhoneTel(request.getParameter("phoneTel"));
		oper.setEmail(request.getParameter("email"));
		String roleinfoId = request.getParameter("roleinfoId");
		if(!StringUtil.isEmpty(roleinfoId)){
			oper.setRoleinfoId(Integer.parseInt(roleinfoId));
		}
		return addOperLogic(oper);
	}

	private ResultModel addOperLogic(Operator oper){
		try {
			if(oper == null){
				return ResultModel.failure("数据为空不允许添加");
			}
			int operRepeat = operService.queryRepeat(oper.getLoginName());
			if(operRepeat > 0){
				return ResultModel.failure("该登录名已添加，请勿重复添加");
			}
			if(StringUtil.isEmpty(oper.getRealName())){
				return ResultModel.failure("真实姓名不能为空");
			}
			if(StringUtil.isEmpty(oper.getLoginName())){
				return ResultModel.failure("登录名不能为空");
			}
			if(StringUtil.isEmpty(oper.getLoginPwd())){
				return ResultModel.failure("密码不能为空");
			}
			if(StringUtil.isEmpty(oper.getFailTimes())){
				oper.setFailTimes(null);
			}
			oper.setLoginPwd(MethodUtil.MD5(oper.getLoginPwd()));
			operService.insert(oper);
			return ResultModel.success("添加成功");
		} catch (Exception e) {
			log.error("添加异常:" + e.getMessage(), e);
			return ResultModel.failure("添加失败");
		}
	}

	/**
	 * 删除用户 - 适配 Vue3 (支持批量删除)
	 * DELETE /admin/oper/{operatorId}
	 * 或 POST /admin/oper/delete (批量)
	 */
	@LoginRequired(remark="删除用户操作")
	@DeleteMapping("/{operatorId}")
	public ResultModel delOper(@PathVariable @NotNull(message = E.E0) String operatorId){
		try {
			List<String> operatorIds = new ArrayList<>();
			if(!StringUtil.isEmpty(operatorId)){
				operatorIds.add(operatorId);
			}
			operService.deletes(operatorIds);
			return ResultModel.success("删除成功");
		} catch (Exception e) {
			log.error("删除异常:" + e.getMessage(), e);
			return ResultModel.failure("删除失败");
		}
	}

	/**
	 * 批量删除用户 - 适配 Vue3
	 * POST /admin/oper/batch-delete
	 */
	@LoginRequired(remark="批量删除用户操作")
	@PostMapping("/batch-delete")
	public ResultModel batchDeleteOper(@RequestBody List<String> operatorIds){
		try {
			if(operatorIds == null || operatorIds.isEmpty()){
				return ResultModel.failure("请选择要删除的用户");
			}
			operService.deletes(operatorIds);
			return ResultModel.success("批量删除成功");
		} catch (Exception e) {
			log.error("批量删除异常:" + e.getMessage(), e);
			return ResultModel.failure("批量删除失败");
		}
	}

	/**
	 * 获取用户详情 - 适配 Vue3
	 * GET /admin/oper/{operatorId}
	 */
	@LoginRequired(remark="获取用户详情操作")
	@GetMapping("/{operatorId}")
	public ResultModel getOperDetail(@PathVariable String operatorId) {
		Operator oper = operService.queryById(operatorId);
		if(oper != null){
			oper.setLoginPwd(null);
		}
		return ResultModel.success("获取成功", oper);
	}

	/**
	 * 获取用户完整详情（包含分支机构、部门、功能角色、数据权限信息）
	 * GET /admin/oper/{operatorId}/detail
	 */
	@LoginRequired(remark="获取用户完整详情操作")
	@GetMapping("/{operatorId}/detail")
	public ResultModel getOperatorFullDetail(@PathVariable Integer operatorId) {
		try {
			com.xiao.core.basic.operator.domain.OperatorDetailDTO detail = operService.getOperatorDetail(operatorId);
			if (detail == null) {
				return ResultModel.failure("用户不存在");
			}
			return ResultModel.success("获取成功", detail);
		} catch (Exception e) {
			log.error("获取用户详情异常:" + e.getMessage(), e);
			return ResultModel.failure("获取用户详情失败");
		}
	}


	/** 去修改密码页 */
	@RequestMapping(value = "/toUpdOperPwd")
	public ModelAndView toUpdOperPwd(HttpServletRequest req,HttpServletResponse resp, HttpSession session) {
		Map<String, Object> context = getRootMap();
		Operator oper=SessionUtils.getUser(req);
		context.put("oper", oper);
		context.put("menuName1", "账号设置");
		context.put("menuName", "修改密码");
		return forword("sys/oper/updateOperPwd", context);
	}

	/**
	 * 修改用户 - 适配 Vue3 (JSON 请求)
	 * PUT /admin/oper
	 */
	@LoginRequired(remark="修改用户操作")
	@PutMapping
	public ResultModel updateOper(@RequestBody Operator oper){
		return updateOperLogic(oper);
	}

	/**
	 * 修改用户 - POST 兼容旧版前端
	 * POST /admin/oper/updateOper
	 */
	@LoginRequired(remark="修改用户操作")
	@PostMapping(value = "/updateOper", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResultModel updateOperPost(@ModelAttribute Operator oper){
		return updateOperLogic(oper);
	}

	private ResultModel updateOperLogic(Operator oper){
		try {
			if(!StringUtil.isEmpty(oper.getLoginPwd())){
				oper.setLoginPwd(MethodUtil.MD5(oper.getLoginPwd()));
			}
			if(StringUtil.isEmpty(oper.getFailTimes())){
				oper.setFailTimes(null);
			}
			if (oper.getDeptId() != null && oper.getDeptId() == 0) {
				oper.setDeptId(null);
				oper.setClearDept(true);
			}
			if (oper.getSugOrgId() != null && oper.getSugOrgId() == 0) {
				oper.setSugOrgId(null);
				oper.setClearBranch(true);
			}
			operService.update(oper);
			return ResultModel.success("修改成功");
		} catch (Exception e) {
			log.error("修改异常:" + e.getMessage(), e);
			return ResultModel.failure("修改失败");
		}
	}

	/**
	 * 修改密码
	 * POST /admin/chpwd
	 */
	@LoginRequired(remark="修改密码操作")
	@RequestMapping(value = "/chpwd",method={RequestMethod.POST})
	public ResultModel chpwd(@CurrentUser Operator operter, @RequestBody Map<String, String> params) {
		try {
			String oldPassword = params.get("oldPassword");
			String newPassword = params.get("newPassword");
			if(StringUtil.isEmpty(oldPassword) || StringUtil.isEmpty(newPassword)) {
				return new ResultModel(false, "密码不能为空");
			}
			Operator dbOper = operService.queryById(String.valueOf(operter.getOperatorId()));
			if(dbOper == null) {
				return new ResultModel(false, "用户不存在");
			}
			if(!dbOper.getLoginPwd().equals(MethodUtil.MD5(oldPassword))){
				return new ResultModel(false, "原密码错误");
			}
			dbOper.setLoginPwd(MethodUtil.MD5(newPassword));
			operService.update(dbOper);
			return new ResultModel(true, "修改成功");
		} catch (Exception e) {
			log.error("修改密码异常:" + e.getMessage(), e);
			return new ResultModel(false, "网络异常，修改失败");
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

	/** 去授权页 */
	@RequestMapping(value = "/toGrant")
	public ModelAndView toGrant(HttpServletRequest req,HttpServletResponse resp, HttpSession session) {
		String operatorId = req.getParameter("operatorId");
		Map<String, Object> context = getRootMap();
		//获取所有权限
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

	/**
	 * 用户授权 - 适配 Vue3
	 * PUT /admin/oper/{operatorId}/grant
	 */
	@LoginRequired(remark="用户授权操作")
	@PutMapping("/{operatorId}/grant")
	public ResultModel grantOper(@PathVariable String operatorId, @RequestBody Map<String, Object> params) {
		return grantOperLogic(operatorId, params);
	}

	/**
	 * 用户授权 - POST 兼容旧版前端
	 * POST /admin/oper/Grant
	 */
	@LoginRequired(remark="用户授权操作")
	@PostMapping("/Grant")
	public ResultModel grantOperPost(HttpServletRequest request) {
		String operatorId = request.getParameter("operatorId");
		String roleInfoId = request.getParameter("roleInfoId");
		if (roleInfoId == null) {
			roleInfoId = request.getParameter("roleinfoId");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("roleInfoId", roleInfoId);
		return grantOperLogic(operatorId, params);
	}

	private ResultModel grantOperLogic(String operatorId, Map<String, Object> params) {
		try {
			String roleInfoId = params.get("roleInfoId") != null ? params.get("roleInfoId").toString() : null;
			Operator operator = operService.queryById(operatorId);
			if(operator == null){
				return ResultModel.failure("无此操作人员");
			}
			if(StringUtil.isEmpty(roleInfoId)){
				return ResultModel.failure("未选择角色");
			}
			operator.setRoleinfoId(Integer.parseInt(roleInfoId));
			operService.update(operator);
			return ResultModel.success("授权成功");
		} catch (Exception e) {
			log.error("授权异常:" + e.getMessage(), e);
			return ResultModel.failure("授权失败");
		}
	}
	
	/**
	 * 重置密码 - 适配 Vue3
	 * PUT /admin/oper/{operatorId}/reset-password
	 */
	@LoginRequired(remark="重置密码操作")
	@PutMapping("/{operatorId}/reset-password")
	public ResultModel resetPassword(@PathVariable Integer operatorId) {
		return resetPasswordLogic(operatorId);
	}

	/**
	 * 重置密码 - POST 兼容旧版前端
	 * POST /admin/oper/resetPassword
	 */
	@LoginRequired(remark="重置密码操作")
	@PostMapping("/resetPassword")
	public ResultModel resetPasswordPost(@RequestParam Integer operatorId) {
		return resetPasswordLogic(operatorId);
	}

	private ResultModel resetPasswordLogic(Integer operatorId) {
		try {
			Operator oper = new Operator();
			oper.setOperatorId(operatorId);
			oper.setLoginPwd(MethodUtil.MD5("123456"));
			boolean flag = operService.update(oper);
			if(flag){
				Map<String, Object> resultData = new HashMap<>();
				resultData.put("defaultPwd", "123456");
				return ResultModel.success("重置成功", resultData);
			}else{
				return ResultModel.failure("重置失败，请重试");
			}
		} catch (Exception e) {
			log.error("重置异常:" + e.getMessage(), e);
			return ResultModel.failure("网络异常，操作失败");
		}
	}
	
	/**
	 * 解除登录限制 - 适配 Vue3
	 * PUT /admin/oper/{operatorId}/unlock
	 */
	@LoginRequired(remark="解除登录限制")
	@PutMapping("/{operatorId}/unlock")
	public ResultModel removeLimit(@PathVariable Integer operatorId) {
		try {
			Operator oper = new Operator();
			oper.setOperatorId(operatorId);
			oper.setLastTime("empt");
			oper.setFailTimes("0");
			boolean flag = operService.update(oper);
			if(flag){
				return ResultModel.success("解除成功");
			}else{
				return ResultModel.failure("解除失败，请重试");
			}
		} catch (Exception e) {
			log.error("解除异常:" + e.getMessage(), e);
			return ResultModel.failure("网络异常，操作失败");
		}
	}

}
