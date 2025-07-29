package com.xiao.core.basic.admin_roleinfo.service;

import com.xiao.base.BaseService;
import com.xiao.core.basic.admin_rolebtn.domain.AdminRolebtn;
import com.xiao.core.basic.admin_roleinfo.domain.AdminRoleinfo;
import com.xiao.core.basic.admin_rolemenu.domain.AdminRolemenu;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author 裘晓伟
 * @since 2018-07-12
 */
public interface AdminRoleinfoService extends BaseService<AdminRoleinfo> {
	public List<AdminRoleinfo> queryallRoleInfo();

	public void delJRoleAuthInfo(int roleinfoid);

	public void delJRoleAuthBtn(int roleinfoid);

	public void addRoleAuthInfo(String str, List<AdminRolemenu> roleauthrels);

	public void addRolBtn(String str, List<AdminRolebtn> roleBtns);

	/** 获取角色原来的权限 */
	public List<AdminRolemenu> queryRoleAuthInfo(String roleinfoid);

	/** 获取角色原来的权限 */
	public List<AdminRolebtn> queryRoleAuthBtn(String roleinfoid);
}
