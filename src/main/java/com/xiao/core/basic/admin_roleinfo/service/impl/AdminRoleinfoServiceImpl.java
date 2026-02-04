package com.xiao.core.basic.admin_roleinfo.service.impl;

import com.xiao.base.BaseMapper;
import com.xiao.base.BaseServiceImpl;
import com.xiao.core.basic.admin_menu.domain.AdminMenu;
import com.xiao.core.basic.admin_menu.mapper.AdminMenuMapper;
import com.xiao.core.basic.admin_rolebtn.domain.AdminRolebtn;
import com.xiao.core.basic.admin_roleinfo.domain.AdminRoleinfo;
import com.xiao.core.basic.admin_roleinfo.mapper.AdminRoleinfoMapper;
import com.xiao.core.basic.admin_roleinfo.service.AdminRoleinfoService;
import com.xiao.core.basic.admin_rolemenu.domain.AdminRolemenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author 裘晓伟
 * @since 2018-07-12
 */
@Service
public class AdminRoleinfoServiceImpl extends BaseServiceImpl<AdminRoleinfo> implements AdminRoleinfoService {

	@Autowired
    AdminMenuMapper<AdminMenu> menuMapper;
	@Autowired
    AdminRoleinfoMapper<AdminRoleinfo> roleInfoMapper;

	@Override
	public BaseMapper<AdminRoleinfo> getMap() {
		// TODO Auto-generated method stub
		return this.roleInfoMapper;
	}

	@Override
	public List<AdminRoleinfo> queryallRoleInfo() {
		// TODO Auto-generated method stub
		return roleInfoMapper.queryallRoleInfo();
	}

	@Override
	public void delJRoleAuthInfo(int roleinfoid) {
		roleInfoMapper.delJRoleAuthInfo(roleinfoid);
	}

	@Override
	public void delJRoleAuthBtn(int roleinfoid) {
		roleInfoMapper.delJRoleAuthBtn(roleinfoid);
	}

	@Override
	public void addRoleAuthInfo(String str, List<AdminRolemenu> roleauthrels) {
		this.delJRoleAuthInfo(Integer.parseInt(str));
		roleInfoMapper.addRoleAuthInfo(roleauthrels);
	}

	@Override
	public void addRolBtn(String str, List<AdminRolebtn> roleBtns) {
		this.delJRoleAuthBtn(Integer.parseInt(str));
		if(roleBtns!=null && roleBtns.size()>0){
			roleInfoMapper.addRoleBtn(roleBtns);
		}
	}

	@Override
	public List<AdminRolemenu> queryRoleAuthInfo(String roleinfoid) {
		return roleInfoMapper.queryRoleAuthInfo(roleinfoid);
	}

	@Override
	public List<AdminRolebtn> queryRoleAuthBtn(String roleinfoid) {
		return roleInfoMapper.queryRoleAuthBtn(roleinfoid);
	}



}
