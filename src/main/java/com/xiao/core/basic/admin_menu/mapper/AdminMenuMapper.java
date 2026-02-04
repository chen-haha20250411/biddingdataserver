package com.xiao.core.basic.admin_menu.mapper;

import com.xiao.base.BaseMapper;
import com.xiao.core.basic.admin_btn.domain.AdminBtn;
import com.xiao.core.basic.admin_menu.domain.AdminMenu;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author 裘晓伟
 * @since 2018-07-12
 */
public interface AdminMenuMapper<T> extends BaseMapper<AdminMenu> {
    public List<AdminMenu> queryMenuList(Map<String, Object> map);

    /**查询系统菜单
	 * @param userId
	 * @return
	 */
	public List<AdminMenu> queryMenuByUser(Map<String, Object> params);

	/***
	 * 获取子菜单
	 * @param roidinfoId
	 * @return
	 */
	public List<AdminMenu> queryChildMenu(String parentNo);


	/**获得子菜单按钮
	 * @param authinfoId
	 * @return
	 */
	public List<AdminBtn> queryAllAuthinfoBtn();

	public List<AdminBtn> queryMenuBtn(String roidinfoId);
	/**
	 * 得到子 菜单
	 *
	 * @return
	 */
	public List<AdminMenu> queryChildMenuByUser(String roidInfoId);

	/**
	 * 得到全部菜单
	 *
	 * @return
	 */
	public List<AdminMenu> queryChildMenuByUserSub(Map<String, Object> params);

}
