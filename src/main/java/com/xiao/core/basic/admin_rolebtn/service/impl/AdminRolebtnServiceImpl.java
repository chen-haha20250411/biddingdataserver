package com.xiao.core.basic.admin_rolebtn.service.impl;

import com.xiao.base.BaseMapper;
import com.xiao.base.BaseServiceImpl;
import com.xiao.core.basic.admin_rolebtn.domain.AdminRolebtn;
import com.xiao.core.basic.admin_rolebtn.mapper.AdminRolebtnMapper;
import com.xiao.core.basic.admin_rolebtn.service.AdminRolebtnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色按钮表 服务实现类
 * </p>
 *
 * @author 裘晓伟
 * @since 2018-07-12
 */
@Service
public class AdminRolebtnServiceImpl extends BaseServiceImpl<AdminRolebtn> implements AdminRolebtnService {

	@Autowired
	AdminRolebtnMapper<AdminRolebtn> adminRolebtnMapper;

	@Override
	public BaseMapper<AdminRolebtn> getMap() {
		return this.adminRolebtnMapper;
	}

}
