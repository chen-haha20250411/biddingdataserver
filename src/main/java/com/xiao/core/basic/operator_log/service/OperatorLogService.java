package com.xiao.core.basic.operator_log.service;

import com.xiao.base.BaseService;
import com.xiao.core.basic.operator.domain.Operator;
import com.xiao.core.basic.operator_log.domain.OperatorLog;

/**
 * <p>
 * 后台操作日志表 服务类
 * </p>
 *
 * @author 裘晓伟
 * @since 2018-07-12
 */
public interface OperatorLogService extends BaseService<OperatorLog> {
	/**
	*  日志操作
	* @param    操作信息
	* @return
	*/
	public void insertOperLog(String remark, Operator operator);
}
