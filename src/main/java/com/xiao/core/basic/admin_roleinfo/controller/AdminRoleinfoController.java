package com.xiao.core.basic.admin_roleinfo.controller;


import com.alibaba.fastjson.JSON;
import com.xiao.base.BaseController;
import com.xiao.base.Page;
import com.xiao.base.ResultModel;
import com.xiao.constans.E;
import com.xiao.core.basic.admin_btn.domain.AdminBtn;
import com.xiao.core.basic.admin_menu.domain.AdminMenu;
import com.xiao.core.basic.admin_menu.service.AdminMenuService;
import com.xiao.core.basic.admin_rolebtn.domain.AdminRolebtn;
import com.xiao.core.basic.admin_roleinfo.domain.AdminRoleinfo;
import com.xiao.core.basic.admin_roleinfo.service.AdminRoleinfoService;
import com.xiao.core.basic.admin_rolemenu.domain.AdminRolemenu;
import com.xiao.core.basic.operator.domain.Operator;
import com.xiao.core.basic.operator.service.OperatorService;
import com.xiao.logannotation.CurrentUser;
import com.xiao.logannotation.LoginRequired;
import com.xiao.util.PageUtils;
import com.xiao.util.RedisUtils;
import com.xiao.util.SessionUtils;
import com.xiao.util.StringUtil;
import com.xiao.util.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author 裘晓伟
 * @since 2018-07-12
 */
@RestController
@RequestMapping("/admin/role")
/**
 * 角色管理控制器
 * 提供角色的CRUD操作和权限分配功能
 */
public class AdminRoleinfoController extends BaseController{
	@Autowired
    private AdminRoleinfoService roleInfoService;
	@Autowired
    private AdminMenuService menuService;
	@Resource
	private OperatorService operatorService;
	@Autowired
	private RedisUtils redisUtils;

	/**
	 * 查询角色列表
	 * @param oper 当前操作人
	 * @param roleName 角色名称（可选，用于模糊查询）
	 * @param limit 每页条数
	 * @param currPageNo 当前页码
	 * @return 分页角色列表
	 */
	@LoginRequired(remark="角色-查询角色列表操作")
	@RequestMapping(value = "/list",method={RequestMethod.POST,RequestMethod.GET})
	public ResultModel listRoles(@CurrentUser Operator oper, String roleName, Integer limit, Integer currPageNo) {
		Page<AdminRoleinfo> page=new Page<AdminRoleinfo>(currPageNo,limit);
		page.putQueryParam("roleName", roleName);
		int rowCount = roleInfoService.queryByCount(page.getQueryParams());
		page.setTotal(rowCount);
		//角色列表
		page.setList(roleInfoService.queryByMap(page.getQueryParams()));
		return PageUtils.pageSuccess(page);
	}

	/**
	 * 获取角色详情
	 * @param request 请求参数，包含roleInfoId
	 * @return 角色详情
	 */
	@LoginRequired(remark="获取角色详情")
	@PostMapping("/getRoleInfo")
	public ResultModel getRoleInfo(@RequestBody Map<String, Object> request) {
		Integer roleInfoId = (Integer) request.get("roleInfoId");
		if (roleInfoId == null) {
			return ResultModel.failure("角色ID不能为空");
		}
		AdminRoleinfo roleInfo = roleInfoService.queryById(roleInfoId + "");
		if (roleInfo != null) {
			return ResultModel.success(roleInfo);
		}
		return ResultModel.failure("角色不存在");
	}

	/** 去添加页面 */
	@RequestMapping(value = "/toAddRole")
	public ModelAndView toAddRole(HttpServletRequest req,HttpServletResponse resp, HttpSession session) {
		Map<String, Object> context = getRootMap();
		Operator oper = SessionUtils.getUser(req);
		context.put("oper", oper);
		context.put("menuName1", "系统设置");
		context.put("menuName", "添加角色");
		return forword("sys/role/addRole", context);
	}

	/**
	 * 添加角色
	 * @param operator 当前操作人
	 * @param request 请求参数，包含role（角色信息）和authStr（权限字符串）
	 * @return 操作结果
	 */
	@LoginRequired(remark="角色-添加权限")
	@PostMapping(value = "/addRole", consumes = "application/json")
	public ResultModel addRole(@CurrentUser Operator operator, @RequestBody Map<String, Object> request){
		try {
			// 解析角色信息
			AdminRoleinfo role = new AdminRoleinfo();
			if (request.get("role") != null) {
				String json = JSON.toJSONString(request.get("role"), true);
				String obj = (String) JSON.parse(json);
				role = JSON.parseObject(obj, AdminRoleinfo.class);
			} else {
				String roleName = (String) request.get("roleName");
				String remark = (String) request.get("remark");
				role.setRoleName(roleName);
				role.setRemark(remark);
			}
			
			// 添加操作人ID
			int operatorId = operator.getOperatorId();
			role.setOperatorId(operatorId);
			
			// 检查角色名称是否已存在
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("roleName", role.getRoleName());
			int count = roleInfoService.queryByCount(map);
			
			if(count==0){
				// 插入角色
				roleInfoService.insert(role);
				
				// 分配权限
				String authStr = (String) request.get("authStr");
				if (authStr != null && !authStr.isEmpty()) {
					List<AdminRolemenu> roleauthrels = new ArrayList<AdminRolemenu>();
					List<AdminRolebtn> roleBtns = new ArrayList<AdminRolebtn>();
					getAuthinfo(roleauthrels, roleBtns, authStr, role.getRoleInfoId() + "");
					roleInfoService.addRolBtn(role.getRoleInfoId() + "", roleBtns);
					roleInfoService.addRoleAuthInfo(role.getRoleInfoId() + "", roleauthrels);
				}
				
				return sendSuccessMessage("添加成功");
			}else{
				return sendFailureMessage("添加失败");
			}

		} catch (Exception e) {
			log.error("添加异常:"+e.getMessage(),e);
			return sendFailureMessage("网络原因,添加失败");
		}

	}

	/**
	 * 添加角色 - 表单格式兼容
	 * @param operator 当前操作人
	 * @param role 角色信息
	 * @param authStr 权限ID字符串（逗号分隔）
	 * @return 操作结果
	 */
	@LoginRequired(remark="角色-添加权限")
	@RequestMapping(value = "/addRole", method = {RequestMethod.POST, RequestMethod.GET})
	public ResultModel addRoleForm(@CurrentUser Operator operator, AdminRoleinfo role, String authStr){
		try {
			//添加角色的操作
			int operatorId = operator.getOperatorId();
			role.setOperatorId(operatorId);
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("roleName", role.getRoleName());

			int count = roleInfoService.queryByCount(map);
			if(count==0){
				roleInfoService.insert(role);
				
				// 分配权限
				if (authStr != null && !authStr.isEmpty()) {
					List<AdminRolemenu> roleauthrels = new ArrayList<AdminRolemenu>();
					List<AdminRolebtn> roleBtns = new ArrayList<AdminRolebtn>();
					getAuthinfo(roleauthrels, roleBtns, authStr, role.getRoleInfoId() + "");
					roleInfoService.addRolBtn(role.getRoleInfoId() + "", roleBtns);
					roleInfoService.addRoleAuthInfo(role.getRoleInfoId() + "", roleauthrels);
				}
				
				return sendSuccessMessage("添加成功");
			}else{
				return sendFailureMessage("添加失败");
			}

		} catch (Exception e) {
			log.error("添加异常:"+e.getMessage(),e);
			return sendFailureMessage("网络原因,添加失败");
		}

	}

	/**
	 * 删除角色
	 * @param roleInfoId 角色ID
	 * @return 操作结果
	 */
	@LoginRequired(remark="角色-删除权限")
	@RequestMapping(value = "/delRole",method={RequestMethod.POST,RequestMethod.GET})
	public ResultModel delRole(@RequestParam @NotNull(message="角色ID"+ E.E0) Integer roleInfoId){
		try {
			roleInfoService.deleteById(roleInfoId+"");
			roleInfoService.delJRoleAuthBtn(roleInfoId);
			roleInfoService.delJRoleAuthInfo(roleInfoId);
			return sendSuccessMessage("删除成功");
		} catch (Exception e) {
			log.error("删除异常:"+e.getMessage(),e);
			return sendFailureMessage("网络异常，删除失败");
		}

	}

	/**
	 * 批量删除角色
	 * @param req HTTP请求
	 * @param resp HTTP响应
	 * @return 操作结果
	 */
	@RequestMapping(value = "/delBatch",method={RequestMethod.POST,RequestMethod.GET})
	public ResultModel delBatch(HttpServletRequest req, HttpServletResponse resp){
		try {
			String ids = req.getParameter("ids");
//			roleInfoService.delBatch(ids);
			return new ResultModel(true,"删除");
		} catch (Exception e) {
			log.error("删除异常:"+e.getMessage(),e);
			return new ResultModel(false,"删除");
		}

	}

	/** 去授权页面 */
	@RequestMapping(value = "/toEdit")
	public ModelAndView toGrant(HttpServletRequest req,HttpServletResponse resp, HttpSession session) {
		String roleInfoId = req.getParameter("roleinfoId");
		Map<String, Object> context = getRootMap();
		AdminRoleinfo roleInfo = roleInfoService.queryById(roleInfoId);
		context.put("roleInfo", roleInfo);
		Operator oper = SessionUtils.getUser(req);
		context.put("oper", oper);
		return forword("sys/role/editRolePage", context);
	}

	/**
	 * 获取角色菜单树
	 * @param roleInfoId 角色ID
	 * @return 菜单树结构和角色信息
	 */
	@LoginRequired(remark="角色-查询角色菜单树")
	@RequestMapping(value = "/getRoleTreeEdit",method={RequestMethod.POST,RequestMethod.GET})
	public ResultModel getRoleTreeEdit(@RequestParam @NotNull(message="角色ID"+ E.E0) Integer roleInfoId) {
		List<AdminMenu> allMenus = new ArrayList<AdminMenu>();
		List<AdminBtn> menuBtns = new ArrayList<AdminBtn>();// 按钮
		
		// 获取所有菜单
		allMenus = menuService.queryByMap(new HashMap<>());
		menuBtns = menuService.queryAllAuthinfoBtn();//取到所有的按钮

		List<AdminRolemenu> listauther = roleInfoService.queryRoleAuthInfo(roleInfoId+"");
		List<AdminRolebtn> menuBtnList = roleInfoService.queryRoleAuthBtn(roleInfoId+"");

		Map<String, AdminRolebtn> authinfoBtnMap = new HashMap<String, AdminRolebtn>();//权限按钮map
		Map<String, AdminRolemenu> autherMap = new HashMap<String, AdminRolemenu>();//权限菜单map
		if (menuBtnList != null) {
			for (AdminRolebtn roleBtn : menuBtnList) {
				authinfoBtnMap.put(String.valueOf(roleBtn.getBtnId()), roleBtn);
			}
		}
		if (listauther != null) {
			for (AdminRolemenu roleauthrel : listauther) {
				autherMap.put(String.valueOf(roleauthrel.getMenuId()), roleauthrel);
			}
		}
		
		// 先给按钮拍下序 ，增强性能
		Map<String, List<TreeNode>> btns = new HashMap<String, List<TreeNode>>();
		for (int i = 0; i < menuBtns.size(); i++) {
			AdminBtn menuBtn = menuBtns.get(i);
			if(!StringUtil.isEmpty(menuBtn.getMenuId()+"")){
				List<TreeNode> btnList =btns.get(String.valueOf(menuBtn.getMenuId()));
				if(btnList==null){
					btnList=new ArrayList<TreeNode>();
				}
				setBtn(btnList, menuBtn, "",authinfoBtnMap);
				btns.put(String.valueOf(menuBtn.getMenuId()), btnList);
			}
		}
		
		// 构建菜单树
		List<TreeNode> rootList = new ArrayList<TreeNode>();
		
		// 递归构建多级菜单树
		for (AdminMenu menu : allMenus) {
			if (menu.getParentNo() == null) {
				// 一级菜单
				TreeNode node = buildMenuTreeNode(menu, allMenus, btns, autherMap);
				rootList.add(node);
			}
		}
		
		AdminRoleinfo roleInfo = roleInfoService.queryById(roleInfoId+"");

		return sendSuccessMessage("获取数据成功").putData("rootList",rootList).putData("roleInfo", roleInfo);
	}

	/**
	 * 递归构建菜单树节点
	 * @param menu 当前菜单
	 * @param allMenus 所有菜单
	 * @param btns 按钮映射
	 * @param autherMap 权限映射
	 * @return 菜单树节点
	 */
	private TreeNode buildMenuTreeNode(AdminMenu menu, List<AdminMenu> allMenus, Map<String, List<TreeNode>> btns, Map<String, AdminRolemenu> autherMap) {
		TreeNode node = new TreeNode();
		node.setId(String.valueOf(menu.getMenuId()));
		node.setName(menu.getMenuName());
		node.setpId(menu.getParentNo() == null ? "0" : String.valueOf(menu.getParentNo()));
		node.setOpen(true);
		
		// 设置按钮
		node.setChildren(btns.get(String.valueOf(menu.getMenuId())));
		
		// 设置权限状态
		if (autherMap != null && autherMap.get(String.valueOf(menu.getMenuId())) != null) {
			node.setChecked(true);
		} else {
			node.setChecked(false);
		}
		
		// 递归构建子菜单
		List<TreeNode> childNodes = new ArrayList<TreeNode>();
		for (AdminMenu childMenu : allMenus) {
			if (childMenu.getParentNo() != null && childMenu.getParentNo().equals(menu.getMenuId())) {
				TreeNode childNode = buildMenuTreeNode(childMenu, allMenus, btns, autherMap);
				childNodes.add(childNode);
			}
		}
		node.setChildren(childNodes);
		
		return node;
	}

	

	/**
	 * 构建按钮节点
	 * @param btns 按钮列表
	 * @param authinfoBtn 按钮信息
	 * @param imageUrl 图标URL
	 * @param authinfoBtnMap 权限按钮映射
	 */
	private void setBtn(List<TreeNode> btns, AdminBtn authinfoBtn, String imageUrl, Map<String, AdminRolebtn> authinfoBtnMap) {
		// 为了区分菜单权限 对id做处理,超过5千的则为按钮 ,因为按钮表和菜单表 的id可能造成重复 所以需要加5000
		TreeNode node = new TreeNode();
		Integer id = 0;
		if(authinfoBtn != null ){
			try{
				 id = authinfoBtn.getBtnId();
				 id = id+5000;
			}catch(Exception e){
				log.error("获取权限Id 失败 "+ e.getMessage(),e);
			}
		}
		node.setId(id.toString());
		node.setName(authinfoBtn.getBtnName());
		node.setpId(String.valueOf(authinfoBtn.getMenuId()));
		node.setIcon(imageUrl);
		if (authinfoBtnMap != null) {
			if (authinfoBtnMap.get(String.valueOf(authinfoBtn.getBtnId())) != null) {
				// 默认权限树选中功能
				node.setChecked(true);
			} else {
				node.setChecked(false);
			}
		}
		btns.add(node);
	}

	/**
	 * 修改角色信息 - JSON Body 格式
	 * @param map 请求参数，包含role（角色信息）和authStr（权限字符串）
	 * @return 操作结果
	 */
	@LoginRequired(remark="角色-修改权限")
	@PostMapping(value = "/updateRole", consumes = "application/json")
	public ResultModel updateRole(@RequestBody Map<String, Object> map) {
		String authStr = (String) map.get("authStr");

		AdminRoleinfo role = new AdminRoleinfo();
		if (map.get("role") != null) {
			String json = JSON.toJSONString(map.get("role"), true);
			String obj = (String) JSON.parse(json);
			role = JSON.parseObject(obj, AdminRoleinfo.class);
		} else {
			String roleName = (String) map.get("roleName");
			String remark = (String) map.get("remark");
			Object roleInfoIdObj = map.get("roleInfoId");
			if (roleInfoIdObj != null) {
				if (roleInfoIdObj instanceof Integer) {
					role.setRoleInfoId((Integer) roleInfoIdObj);
				} else {
					role.setRoleInfoId(Integer.parseInt(roleInfoIdObj.toString()));
				}
			}
			role.setRoleName(roleName);
			role.setRemark(remark);
		}

		if (role == null || StringUtil.isEmpty(role.getRoleInfoId() + "")) {
			return sendFailureMessage("参数不完整，roleInfoId不能为空");
		}

		List<AdminRolemenu> roleauthrels = new ArrayList<AdminRolemenu>();
		List<AdminRolebtn> roleBtns = new ArrayList<AdminRolebtn>();
		getAuthinfo(roleauthrels, roleBtns, authStr, role.getRoleInfoId() + "");
		try {
			roleInfoService.update(role);
			roleInfoService.addRolBtn(role.getRoleInfoId() + "", roleBtns);
			roleInfoService.addRoleAuthInfo(role.getRoleInfoId() + "", roleauthrels);
			List<Operator> operList = operatorService.queryUserForSup(role.getRoleInfoId() + "");
			for (Operator operator : operList) {
				redisUtils.del("permissions_" + operator.getOperatorId());
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return sendFailureMessage("网络异常，修改失败");
		}
		return sendSuccessMessage("修改成功");
	}

	/**
	 * 修改角色信息 - 表单格式兼容
	 * @param map 请求参数，包含roleName、remark、roleInfoId和authStr
	 * @return 操作结果
	 */
	@LoginRequired(remark="角色-修改权限")
	@RequestMapping(value = "/updateRole", method = {RequestMethod.POST, RequestMethod.GET})
	public ResultModel updateRoleForm(@RequestParam Map<String, Object> map) {
		return updateRole(new java.util.HashMap<>(map));
	}

	/**
	 * 解析权限字符串，构建权限对象列表
	 * @param roleauthrels 角色菜单权限列表
	 * @param RroleBtns 角色按钮权限列表
	 * @param authStr 权限字符串，格式为逗号分隔的权限ID或换行分隔的权限ID
	 * @param roleInfoId 角色ID
	 */
	private void getAuthinfo(List<AdminRolemenu> roleauthrels, List<AdminRolebtn> RroleBtns, String authStr, String roleInfoId) {
		if (authStr == null || authStr.isEmpty()) {
			return;
		}
		String[] args;
		if (authStr.contains("\n")) {
			args = authStr.split("\n");
		} else if (authStr.contains(",")) {
			args = authStr.split(",");
		} else {
			args = new String[]{authStr};
		}

		if (args != null && args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				String item = args[i].trim();
				if (item.isEmpty()) continue;

				String authid;
				if (item.contains("-")) {
					String[] authindId = item.split("-");
					authid = authindId.length > 1 ? authindId[1] : authindId[0];
				} else {
					authid = item;
				}

				if (authid != null && !""
						.equals(authid) && !"0".equals(authid)) {
					int authi = Integer.parseInt(authid);
					if (authi > 5000) {
						AdminRolebtn roleBtn = new AdminRolebtn();
						roleBtn.setBtnId(authi - 5000);
						if (roleInfoId != null) {
							roleBtn.setRoleInfoId(Integer.parseInt(roleInfoId));
						}
						RroleBtns.add(roleBtn);
					} else {
						AdminRolemenu roleauthrel = new AdminRolemenu();
						roleauthrel.setMenuId(authi);
						if (roleInfoId != null) {
							roleauthrel.setRoleInfoId(Integer.parseInt(roleInfoId));
						}
						roleauthrels.add(roleauthrel);
					}
				}
			}
		}
	}
}
