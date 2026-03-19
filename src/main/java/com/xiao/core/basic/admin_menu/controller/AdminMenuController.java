package com.xiao.core.basic.admin_menu.controller;


import com.xiao.base.BaseController;
import com.xiao.base.Page;
import com.xiao.base.ResultModel;
import com.xiao.constans.E;
import com.xiao.core.basic.admin_menu.domain.AdminMenu;
import com.xiao.core.basic.admin_menu.service.AdminMenuService;
import com.xiao.core.basic.operator.domain.Operator;
import com.xiao.logannotation.CurrentUser;
import com.xiao.logannotation.LoginRequired;
import com.xiao.util.PageUtils;
import com.xiao.util.SessionUtils;
import com.xiao.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
 * 菜单表 前端控制器
 * </p>
 *
 * @author 裘晓伟
 * @since 2018-07-12
 */
@RestController
@RequestMapping("/admin/menu")
public class AdminMenuController extends BaseController{
	@Autowired
    AdminMenuService menuService;

	@LoginRequired(remark="菜单-查询菜单列表操作")
	@RequestMapping(value = "/list",method={RequestMethod.POST,RequestMethod.GET})
	public ResultModel list(@CurrentUser Operator oper, String menuName, Integer limit, Integer currPageNo) {
		Page<AdminMenu> page=new Page<AdminMenu>(currPageNo,limit);
		page.putQueryParam("menuName", menuName);
		int rowCount = menuService.queryByCount(page.getQueryParams());
		page.setTotal(rowCount);
		page.setList(menuService.queryByMap(page.getQueryParams()));
		return PageUtils.pageSuccess(page);
	}

	/**
	 * 获取菜单详情
	 */
	@LoginRequired(remark="获取菜单详情")
	@PostMapping("/getMenuInfo")
	public ResultModel getMenuInfo(@RequestParam Integer menuId) {
		AdminMenu menu = menuService.queryById(menuId + "");
		if (menu != null) {
			return ResultModel.success(menu);
		}
		return ResultModel.failure("菜单不存在");
	}

	/** 去添加页面 */
	@RequestMapping(value = "/toAddMenu")
	public ModelAndView toAddMenu(HttpServletRequest req,HttpServletResponse resp, HttpSession session) {
		Map<String, Object> context = getRootMap();
		Operator oper = SessionUtils.getUser(req);
		context.put("oper", oper);
		context.put("menuName1", "系统设置");
		context.put("menuName", "添加菜单");
		return forword("sys/menu/addMenu", context);
	}

	/**添加*/
	@LoginRequired(remark="菜单-添加菜单")
	@RequestMapping(value = "/addMenu",method={RequestMethod.POST,RequestMethod.GET})
	public ResultModel addMenu(@CurrentUser Operator operator, AdminMenu menu){
		try {
			int operatorId = operator.getOperatorId();
			menu.setOperatoNo(operatorId);
			menuService.insert(menu);
			return sendSuccessMessage("添加成功");
		} catch (Exception e) {
			log.error("添加异常:"+e.getMessage(),e);
			return sendFailureMessage("网络原因,添加失败");
		}
	}

	/**删除*/
	@LoginRequired(remark="菜单-删除菜单")
	@RequestMapping(value = "/delMenu",method={RequestMethod.POST,RequestMethod.GET})
	public ResultModel delMenu(@RequestParam @NotNull(message="菜单ID"+ E.E0) Integer menuId){
		try {
			menuService.deleteById(menuId+"");
			return sendSuccessMessage("删除成功");
		} catch (Exception e) {
			log.error("删除异常:"+e.getMessage(),e);
			return sendFailureMessage("网络异常，删除失败");
		}
	}

	/**修改菜单信息*/
	@LoginRequired(remark="菜单-修改菜单")
	@PostMapping(value = "/updateMenu", consumes = "application/json")
	public ResultModel updateMenu(@RequestBody AdminMenu menu) {
		if (menu == null || StringUtil.isEmpty(menu.getMenuId() + "")) {
			return sendFailureMessage("参数不完整，menuId不能为空");
		}

		try {
			menuService.update(menu);
			return sendSuccessMessage("修改成功");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return sendFailureMessage("网络异常，修改失败");
		}
	}

	/**
	 * 获取所有菜单（用于下拉选择）
	 */
	@LoginRequired(remark="获取所有菜单")
	@RequestMapping(value = "/getAllMenus",method={RequestMethod.POST,RequestMethod.GET})
	public ResultModel getAllMenus() {
		List<AdminMenu> menus = menuService.queryByMap(new HashMap<>());
		return ResultModel.success(menus);
	}

	/**
	 * 获取多级菜单（不含最底层菜单，用于添加子菜单时选择父菜单）
	 */
	@LoginRequired(remark="获取多级菜单")
	@RequestMapping(value = "/getRootMenus",method={RequestMethod.POST,RequestMethod.GET})
	public ResultModel getRootMenus() {
		// 获取所有菜单
		List<AdminMenu> allMenus = menuService.queryByMap(new HashMap<>());
		
		// 构建菜单树并移除最底层菜单
		List<AdminMenu> menuTree = buildMenuTree(allMenus);
		
		return ResultModel.success(menuTree);
	}

	/**
	 * 构建菜单树并移除最底层菜单
	 * @param allMenus 所有菜单
	 * @return 不含最底层菜单的菜单树
	 */
	private List<AdminMenu> buildMenuTree(List<AdminMenu> allMenus) {
		List<AdminMenu> rootMenus = new ArrayList<>();
		
		// 先构建完整的菜单树
		Map<Integer, AdminMenu> menuMap = new HashMap<>();
		for (AdminMenu menu : allMenus) {
			menuMap.put(menu.getMenuId(), menu);
			menu.setSubMenuList(new ArrayList<>());
		}
		
		for (AdminMenu menu : allMenus) {
			if (menu.getParentNo() == null) {
				// 一级菜单
				rootMenus.add(menu);
			} else {
				// 子菜单
				AdminMenu parentMenu = menuMap.get(menu.getParentNo());
				if (parentMenu != null) {
					parentMenu.getSubMenuList().add(menu);
				}
			}
		}
		
		// 移除最底层菜单
		removeLeafMenus(rootMenus);
		
		return rootMenus;
	}

	/**
	 * 递归移除最底层菜单
	 * @param menus 菜单列表
	 */
	private void removeLeafMenus(List<AdminMenu> menus) {
		if (menus == null || menus.isEmpty()) {
			return;
		}
		
		for (int i = menus.size() - 1; i >= 0; i--) {
			AdminMenu menu = menus.get(i);
			List<AdminMenu> subMenus = menu.getSubMenuList();
			
			if (subMenus == null || subMenus.isEmpty()) {
				// 最底层菜单，移除
				menus.remove(i);
			} else {
				// 递归处理子菜单
				removeLeafMenus(subMenus);
			}
		}
	}
}
