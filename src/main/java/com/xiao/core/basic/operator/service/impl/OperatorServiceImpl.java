package com.xiao.core.basic.operator.service.impl;

import com.xiao.base.BaseMapper;
import com.xiao.base.BaseServiceImpl;
import com.xiao.core.basic.admin_roleinfo.domain.AdminRoleinfo;
import com.xiao.core.basic.operator.domain.Operator;
import com.xiao.core.basic.operator.mapper.OperatorMapper;
import com.xiao.core.basic.operator.service.OperatorService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台操作员 服务实现类
 * </p>
 *
 * @author 裘晓伟
 * @since 2018-07-12
 */
@Service
public class OperatorServiceImpl extends BaseServiceImpl<Operator> implements OperatorService {

	@Resource
    OperatorMapper<Operator> operatorMapper;

	@Override
	public BaseMapper<Operator> getMap() {
		return this.operatorMapper;
	}

	@Override
	public Operator queryLogin(String login_name, String login_pwd) {
		Map<String,String> params = new HashMap<String,String>();
		params.put("loginName", login_name);
		params.put("loginPwd", login_pwd);
		Operator oper = operatorMapper.queryLogin(params);
		return oper;
	}

	@Override
	public void deletes(List<String> operatorIds){
		operatorMapper.deletes(operatorIds);
	}

	/** 查看账号是否重复 */
	@Override
	public Integer queryRepeat(String userLogin) {

		return operatorMapper.queryRepeat(userLogin);
	}


	/** 获取已知可用权限 */
	@Override
	public List<AdminRoleinfo> queryAllRole(Map<String, Object> param) {
		return operatorMapper.queryAllRole(param);
	}


	/** 获取所有用户 */
	@Override
	public Map<String, String> queryAllUser() {
       Map<String, String> userMap = new HashMap<String, String>();
       List<Operator> list = operatorMapper.queryAllUser();
        if(list!=null && list.size()>0){
        	for(int i = 0 ;i<list.size();i++){
        		 userMap.put(list.get(i).getOperatorId()+"", list.get(i).getLoginName());
        	}
        }
		return userMap;
	}

	@Override
	public List<Operator> queryUserForSup(String roleinfoId) {
		return operatorMapper.queryUserForSup(roleinfoId);
	}

	@Override
	public Operator queryUserByRealName(String realName) {
		return operatorMapper.queryUserByRealName(realName);
	}

	@Override
	public List<Operator>queryByAll(){
		return operatorMapper.queryByAll();
	}
}
