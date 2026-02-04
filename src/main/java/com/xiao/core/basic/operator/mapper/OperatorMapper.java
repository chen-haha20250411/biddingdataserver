package com.xiao.core.basic.operator.mapper;

import com.xiao.base.BaseMapper;
import com.xiao.core.basic.admin_roleinfo.domain.AdminRoleinfo;
import com.xiao.core.basic.operator.domain.Operator;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台操作员 Mapper 接口
 * </p>
 *
 * @author 裘晓伟
 * @since 2018-07-12
 */
public interface OperatorMapper<T> extends BaseMapper<Operator> {
	/** 查看账号是否重复 */
	public Integer queryRepeat(String userLogin);

	/**
	 * 登录校验
	 *
	 */
	public Operator queryLogin(Map<String, String> params);

	/** 获取已知可用权限 */
    public List<AdminRoleinfo> queryAllRole(Map<String, Object> params);

    /** 获取所有用户 */
    public List<Operator> queryAllUser();

	/** 获取所有用户 */
    public List<Operator>queryByAll();

    /**批量删除*/
    public void deletes(List<String> operatorIds);
	/**查询特定角色信息列表*/
	public List<Operator> queryUserForSup(String roleinfoId);
	/** 根据realName获取供应商 */
	public T queryUserByRealName(String realName);
}
