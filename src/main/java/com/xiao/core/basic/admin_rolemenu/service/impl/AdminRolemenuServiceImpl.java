package com.xiao.core.basic.admin_rolemenu.service.impl;

import com.xiao.base.BaseMapper;
import com.xiao.base.BaseServiceImpl;
import com.xiao.core.basic.admin_rolemenu.domain.AdminRolemenu;
import com.xiao.core.basic.admin_rolemenu.mapper.AdminRolemenuMapper;
import com.xiao.core.basic.admin_rolemenu.service.AdminRolemenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色菜单表 服务实现类
 * </p>
 *
 * @author 裘晓伟
 * @since 2018-07-12
 */
@Service
public class AdminRolemenuServiceImpl extends BaseServiceImpl<AdminRolemenu> implements AdminRolemenuService {

	@Autowired
    AdminRolemenuMapper<AdminRolemenu> adminRolemenuMapper;

	@Override
	public BaseMapper<AdminRolemenu> getMap() {
		return this.adminRolemenuMapper;
	}

}
