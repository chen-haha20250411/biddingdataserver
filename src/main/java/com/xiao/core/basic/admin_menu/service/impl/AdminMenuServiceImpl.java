package com.xiao.core.basic.admin_menu.service.impl;

import com.xiao.base.BaseMapper;
import com.xiao.base.BaseServiceImpl;
import com.xiao.core.basic.admin_btn.domain.AdminBtn;
import com.xiao.core.basic.admin_menu.domain.AdminMenu;
import com.xiao.core.basic.admin_menu.mapper.AdminMenuMapper;
import com.xiao.core.basic.admin_menu.service.AdminMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author 裘晓伟
 * @since 2018-07-12
 */
@Service
public class AdminMenuServiceImpl extends BaseServiceImpl<AdminMenu> implements AdminMenuService {
	@Resource
    private AdminMenuMapper<AdminMenu> menuMapper;


	@Override
	public BaseMapper<AdminMenu> getMap() {
		return this.menuMapper;
	}


	@Override
	public List<AdminMenu> queryMenuList(Map<String, Object> map) {
		return menuMapper.queryMenuList(map);
	}

	@Override
	public List<AdminBtn> queryAllAuthinfoBtn() {
		return menuMapper.queryAllAuthinfoBtn();
	}


	@Override
	public List<AdminMenu> queryMenuByUser(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return menuMapper.queryMenuByUser(params);
	}


	@Override
	public List<AdminMenu> queryChildMenuByUser(String roidInfoId) {
		// TODO Auto-generated method stub
		return menuMapper.queryChildMenu(roidInfoId);
	}


	@Override
	public List<AdminBtn> queryMenuBtn(String roidinfoId) {
		// TODO Auto-generated method stub
		return menuMapper.queryMenuBtn(roidinfoId);
	}


	@Override
	public List<AdminMenu> queryChildMenuByUserSub(Map<String, Object> params) {
		return menuMapper.queryChildMenuByUserSub(params);
	}

}
