package com.xiao.core.basic.admin_menu.service;

import com.xiao.base.BaseService;
import com.xiao.core.basic.admin_btn.domain.AdminBtn;
import com.xiao.core.basic.admin_menu.domain.AdminMenu;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author 裘晓伟
 * @since 2018-07-12
 */
public interface AdminMenuService extends BaseService<AdminMenu> {
	/**
	 * 根据用户id查询父菜单
	 *
	 * @param roleId
	 * @return
	 */
	public List<AdminMenu> queryMenuByUser(Map<String, Object> params);

	/**
	 * 得到子 菜单
	 *
	 * @return
	 */
	public List<AdminMenu> queryChildMenuByUser(String roidInfoId);

	public List<AdminBtn> queryMenuBtn(String roidinfoId);

	/** 查询一级（头部）菜单 */
	public List<AdminMenu> queryMenuList(Map<String, Object> map);

	/**
	 * 获得子菜单按钮
	 *
	 * @param authinfoId
	 * @return
	 */
	public List<AdminBtn> queryAllAuthinfoBtn();

	/**
	 * 得到全部菜单
	 *
	 * @return
	 */
	public List<AdminMenu> queryChildMenuByUserSub(Map<String, Object> params);
}
