package com.xiao.core.basic.admin_roleinfo.mapper;

import com.xiao.base.BaseMapper;
import com.xiao.core.basic.admin_rolebtn.domain.AdminRolebtn;
import com.xiao.core.basic.admin_roleinfo.domain.AdminRoleinfo;
import com.xiao.core.basic.admin_rolemenu.domain.AdminRolemenu;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author 裘晓伟
 * @since 2018-07-12
 */
public interface AdminRoleinfoMapper<T> extends BaseMapper<AdminRoleinfo> {
	/**
	 * 保存一个对象
	 *
	 * @param t
	 */
	public void addRoleAuthInfo(List<AdminRolemenu> roleAuthInfo);

	/**
	 * 保存一个对象
	 *
	 * @param t
	 */
	public void addRoleBtn(List<AdminRolebtn> roleAuthBtn);
	/**
	 * 保存一个对象
	 *
	 * @param t
	 */
	public void delJRoleAuthInfo(int roleinfoid);

	/**
	 * 保存一个对象
	 *
	 * @param t
	 */
	public void delJRoleAuthBtn(int roleinfoid);

	/** 获取角色原来的权限 */
	public List<AdminRolemenu> queryRoleAuthInfo(String roleinfoid);

	/** 获取角色原来的权限 */
	public List<AdminRolebtn> queryRoleAuthBtn(String roleinfoid);

	public int queryByCount1(Map<String, Object> map);

	public List<AdminRoleinfo>queryallRoleInfo();
}
