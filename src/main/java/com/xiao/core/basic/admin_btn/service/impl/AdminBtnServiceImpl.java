package com.xiao.core.basic.admin_btn.service.impl;

import com.xiao.base.BaseMapper;
import com.xiao.base.BaseServiceImpl;
import com.xiao.core.basic.admin_btn.domain.AdminBtn;
import com.xiao.core.basic.admin_btn.mapper.AdminBtnMapper;
import com.xiao.core.basic.admin_btn.service.AdminBtnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 权限按钮表 服务实现类
 * </p>
 *
 * @author 裘晓伟
 * @since 2018-07-12
 */
@Service
public class AdminBtnServiceImpl extends BaseServiceImpl<AdminBtn> implements AdminBtnService {

	@Autowired
	private AdminBtnMapper<AdminBtn> adminBtnMapper;

	@Override
	public BaseMapper<AdminBtn> getMap() {
		return this.adminBtnMapper;
	}
}
