package com.xiao.core.basic.operator_log.domain;

import com.xiao.base.BaseDomain;
import com.xiao.util.StringUtil;

/**
 * <p>
 * 后台操作日志表
 * </p>
 *
 * @author 裘晓伟
 * @since 2018-07-12
 */
public class OperatorLog extends BaseDomain{

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Integer operatorLogId;
    /**
     * 操作日志
     */
	private String remark;
    /**
     * 帐号
     */
	private String loginName;
    /**
     * 姓名
     */
	private String userName;
    /**
     * 所属角色
     */
	private Integer roleId;
    /**
     * 操作时间
     */
	private String createTime;


	private String roleName;


	public Integer getOperatorLogId() {
		return operatorLogId;
	}

	public void setOperatorLogId(Integer operatorLogId) {
		this.operatorLogId = operatorLogId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}


	public String getCreateTime() {
		return StringUtil.isEmpty(createTime)?createTime:createTime.substring(0,19);
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "OperatorLog{" +
			"operatorLogId=" + operatorLogId +
			", remark=" + remark +
			", loginName=" + loginName +
			", userName=" + userName +
			", roleId=" + roleId +
			", createTime=" + createTime +
			"}";
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
