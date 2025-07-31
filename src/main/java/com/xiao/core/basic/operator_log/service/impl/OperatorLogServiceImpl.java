package com.xiao.core.basic.operator_log.service.impl;

import com.xiao.base.BaseMapper;
import com.xiao.base.BaseServiceImpl;
import com.xiao.core.basic.operator.domain.Operator;
import com.xiao.core.basic.operator_log.mapper.OperatorLogMapper;
import com.xiao.core.basic.operator_log.service.OperatorLogService;
import com.xiao.core.basic.operator_log.domain.OperatorLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 后台操作日志表 服务实现类
 * </p>
 *
 * @author 裘晓伟
 * @since 2018-07-12
 */
@Service
public class OperatorLogServiceImpl extends BaseServiceImpl<OperatorLog> implements OperatorLogService {
	@Autowired
	private OperatorLogMapper<OperatorLog> operateLogMapper;

	@Override
	public BaseMapper<OperatorLog> getMap() {
		return this.operateLogMapper;
	}

	@Override
	public void insertOperLog(String remark, Operator op) {
		String loginName = "";
		String userName = "";
		//获取登录人信息
		if(op != null){
			loginName = op.getLoginName();
			userName = op.getRealName();
			OperatorLog operateLog = new OperatorLog();
			operateLog.setRoleId(op.getRoleinfoId());
			operateLog.setLoginName(loginName);
			operateLog.setUserName(userName);
			operateLog.setRemark(remark);
			operateLogMapper.insert(operateLog);
		}

	}


}
