package com.xiao.core.basic.operator.service;

import com.xiao.base.BaseService;
import com.xiao.core.basic.admin_roleinfo.domain.AdminRoleinfo;
import com.xiao.core.basic.operator.domain.Operator;
import com.xiao.core.basic.operator.domain.OperatorDetailDTO;
import com.xiao.core.basic.operator.domain.OperatorListDTO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台操作员 服务类
 * </p>
 *
 * @author 裘晓伟
 * @since 2018-07-12
 */
public interface OperatorService extends BaseService<Operator> {

	public Operator queryLogin(String login_name, String login_pwd);

	/** 查看用户账号是否重复 */
	public Integer queryRepeat(String userLogin);

	/** 删除指定用户信息 */
	public void deletes(List<String> operatorIds);

	/** 获取已知可用权限 */
	public List<AdminRoleinfo> queryAllRole(Map<String, Object> param);

	/** 获取所有用户 */
	public Map<String, String> queryAllUser();
	/**查询特定角色信息列表*/
	public List<Operator> queryUserForSup(String roleinfoId);
	/** 根据realName获取供应商 */
	public Operator queryUserByRealName(String realName);
	/** 获取所有用户 */
	public List<Operator>queryByAll();

	/** 获取用户详情（包含分支机构、部门、功能角色、数据权限信息） */
	public OperatorDetailDTO getOperatorDetail(Integer operatorId);

	/** 获取用户列表（包含分支机构、部门、数据角色信息） */
	public List<OperatorListDTO> getOperatorListWithDetails(Map<String, Object> params);

}
