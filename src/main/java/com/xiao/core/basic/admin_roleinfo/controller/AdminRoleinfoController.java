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
public class AdminRoleinfoController extends BaseController{
	@Autowired
    AdminRoleinfoService roleInfoService;
	@Autowired
    AdminMenuService menuService;
	@Resource
	OperatorService operatorService;
	@Autowired
	RedisUtils redisUtils;

	@LoginRequired(remark="角色-查询角色列表操作")
	@RequestMapping(value = "/list",method={RequestMethod.POST,RequestMethod.GET})
	public ResultModel left(@CurrentUser Operator oper, String roleName, Integer limit, Integer currPageNo) {
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
	 * POST /admin/role/getRoleInfo
	 */
	@LoginRequired(remark="获取角色详情")
	@PostMapping("/getRoleInfo")
	public ResultModel getRoleInfo(@RequestParam Integer roleInfoId) {
		AdminRoleinfo roleInfo = roleInfoService.queryById(roleInfoId + "");
		if (roleInfo != null) {
			return ResultModel.success(roleInfo);
		}
		return ResultModel.failure("角色不存在");
	}

	/** 去添加页�? */
	@RequestMapping(value = "/toAddRole")
	public ModelAndView toAddRole(HttpServletRequest req,HttpServletResponse resp, HttpSession session) {
		Map<String, Object> context = getRootMap();
		Operator oper = SessionUtils.getUser(req);
		context.put("oper", oper);
		context.put("menuName1", "系统设置");
		context.put("menuName", "添加角色");
		return forword("sys/role/addRole", context);
	}

	/**添加*/
	@LoginRequired(remark="角色-添加权限")
	@RequestMapping(value = "/addRole",method={RequestMethod.POST,RequestMethod.GET})
	public ResultModel addRole(@CurrentUser Operator operator, AdminRoleinfo role){
		try {
			//添加角色的操作
			int operatorId = operator.getOperatorId();
			role.setOperatorId(operatorId);
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("roleName", role.getRoleName());

			int count = roleInfoService.queryByCount(map);
			if(count==0){
				roleInfoService.insert(role);
				return sendSuccessMessage("添加成功");
			}else{
				return sendFailureMessage("添加失败");
			}

		} catch (Exception e) {
			log.error("添加异常:"+e.getMessage(),e);
			return sendFailureMessage("网络原因,添加失败");
		}

	}

	/**删除*/
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
			return sendFailureMessage("网络异常，删除失败?");
		}

	}

	/**批量删除*/
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

	/** 去授权页�? */
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

	@LoginRequired(remark="角色-查询角色菜单树")
	@RequestMapping(value = "/getRoleTreeEdit",method={RequestMethod.POST,RequestMethod.GET})
	public ResultModel getRoleTreeForEdit(@RequestParam @NotNull(message="角色ID"+ E.E0) Integer roleInfoId) {
		List<AdminMenu> rootMenus = new ArrayList<AdminMenu>();
		List<AdminMenu> childMenus = new ArrayList<AdminMenu>();
		List<AdminBtn> menuBtns = new ArrayList<AdminBtn>();// 按钮
		Map<String, Object> context = getRootMap();
		// 根据用户�?属角色查�? 关联的权�?
		Map<String, Object> params = new HashMap<String, Object>();
		rootMenus = menuService.queryMenuList(params);
		//�?级菜单列�?
		if(rootMenus != null || rootMenus.size() > 0){
			for (AdminMenu menu : rootMenus) {
				List<AdminMenu> menus = menuService.queryChildMenuByUser(menu.getMenuId()+"");// 查询�?有子节点（一级菜单）
				if(menus != null ||  menus.size() > 0){
					childMenus.addAll(menus);
				}
			}
		}
		menuBtns = menuService.queryAllAuthinfoBtn();//取到�?有的but

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
		// 先给按钮拍下�? ，增强�?�能
		Map<String, List<TreeNode>> btns = new HashMap<String, List<TreeNode>>();
		//20200701修改by--裘晓伟
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
//		for (int i = 0; i < menuBtns.size(); i++) {
//			AdminBtn menuBtn = menuBtns.get(i);
//			if (btns.get(String.valueOf(menuBtn.getMenuId())) != null) {
//				continue;
//			}
//			List<TreeNode> btnList = new ArrayList<TreeNode>();
//			setBtn(btnList, menuBtn, "",authinfoBtnMap);
//			for (int j = i + 1; j < menuBtns.size(); j++) {
//				AdminBtn authinfoBtn2 = menuBtns.get(j);
//				if (menuBtn.getMenuId().equals(authinfoBtn2.getMenuId())) {
//					setBtn(btnList, authinfoBtn2, "", authinfoBtnMap);
//				}
//			}
//			btns.put(String.valueOf(menuBtn.getMenuId()), btnList);
//		}
		List<TreeNode> rootList = new ArrayList<TreeNode>();
		TreeNode rootNode = new TreeNode("0", "0", "权限菜单", false, true, null);
		//rootNode.setIcon(context.get("imgUrl") + "/root.gif");
		// 菜单
		for (AdminMenu menu : rootMenus) {

			TreeNode node = new TreeNode();//根节点（Top菜单�?
			node.setId(String.valueOf(menu.getMenuId()));
			node.setName(menu.getMenuName());
			if (autherMap.get(String.valueOf(menu.getMenuId())) != null) {
				node.setChecked(true);
			}
			node.setOpen(true);
			//node.setIconOpen(context.get("imgUrl") + "/1_open.png");
			//node.setIconClose(context.get("imgUrl") + "/1_close.png");
			List<TreeNode> childlist = new ArrayList<TreeNode>();//�?级节�?
			node.setpId("0");
			for (AdminMenu childMenu : childMenus) {
				if (menu.getMenuId().equals(childMenu.getParentNo())) {
					TreeNode childNode = getChildToTreeNode(childMenu, btns, autherMap);//取到二级节点
					//TreeNode childNode = new TreeNode();
					childlist.add(childNode);
				}
			}
			node.setChildren(childlist);
			rootList.add(node);
		}
		rootNode.setChildren(rootList);
		AdminRoleinfo roleInfo = roleInfoService.queryById(roleInfoId+"");

		return sendSuccessMessage("获取数据成功").putData("rootList",rootList).putData("roleInfo", roleInfo);
		//String date = JSON.toJSONString(rootNode);
		//writerJson(resp,date);
	}

	/**
	 * 构建�?级菜单树
	 * @param menu
	 * @param subMenus
	 * @param btns
	 * @param autherMap
	 * @return
	 */
	private TreeNode getSubTreeNode(AdminMenu menu, List<AdminMenu> subMenus, Map<String, List<TreeNode>> btns, Map<String, AdminRolemenu> autherMap) {
		TreeNode node = new TreeNode();
		node.setId(String.valueOf(menu.getMenuId()));
		node.setName(menu.getMenuName());
		//node.setChildren(btns.get(authinfo.getMenu_Id()));
		node.setpId(String.valueOf(menu.getParentNo()));
		node.setOpen(true);
		if (autherMap != null) {
			if (autherMap.get(String.valueOf(menu.getMenuId())) != null) {
				node.setChecked(true);
			} else {
				node.setChecked(false);
			}
		}

		List<TreeNode> sublist = new ArrayList<TreeNode>();
		node.setpId("0");
		for (AdminMenu subMenu : subMenus) {
			if(subMenu.getParentNo().equals(menu.getMenuId())){
				TreeNode childNode = getChildToTreeNode(subMenu, btns, autherMap);
				sublist.add(childNode);
			}
		}
		node.setChildren(sublist);
		return node;
	}

	/**
	 * 构建二级菜单�?
	 * @param menu
	 * @param btns
	 * @param autherMap
	 * @return
	 */
	private TreeNode getChildToTreeNode(AdminMenu menu, Map<String, List<TreeNode>> btns, Map<String, AdminRolemenu> autherMap) {
		TreeNode node = new TreeNode();
		node.setId(String.valueOf(menu.getMenuId()));
		node.setName(menu.getMenuName());
		node.setChildren(btns.get(String.valueOf(menu.getMenuId())));
		node.setpId(String.valueOf(menu.getParentNo()));
		node.setOpen(true);
		if (autherMap != null) {
			if (autherMap.get(String.valueOf(menu.getMenuId())) != null) {
				node.setChecked(true);
			} else {
				node.setChecked(false);
			}
		}
		return node;
	}

	/**
	 * 构建按钮节点
	 * @param btns
	 * @param authinfoBtn
	 * @param imageUrl
	 * @param authinfoBtnMap
	 */
	private void setBtn(List<TreeNode> btns, AdminBtn authinfoBtn, String imageUrl, Map<String, AdminRolebtn> authinfoBtnMap) {
		// 为了区分菜单权限 对id做处�?,超过5千的则为按钮 ,因为按钮表和菜单�? 的id可能造成重复  �?以需要加5�?
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
				// 默认权限树�?�中功能
				node.setChecked(true);
			} else {
				node.setChecked(false);
			}
		}
		btns.add(node);
	}

	/**
	 * 修改角色信息 - JSON Body 格式
	 *
	 * @throws ParseException
	 */
	@LoginRequired(remark="角色-修改权限")
	@PostMapping(value = "/updateroleinfo", consumes = "application/json")
	public ResultModel updateroleinfo(@RequestBody Map<String, Object> map) {
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
	 */
	@LoginRequired(remark="角色-修改权限")
	@RequestMapping(value = "/updateroleinfo", method = {RequestMethod.POST, RequestMethod.GET})
	public ResultModel updateroleinfoForm(@RequestParam Map<String, Object> map) {
		return updateroleinfo(new java.util.HashMap<>(map));
	}

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

				if (authid != null && !"".equals(authid) && !"0".equals(authid)) {
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
