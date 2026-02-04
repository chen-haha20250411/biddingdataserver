package com.xiao.core.basic.operator.domain;

import com.xiao.base.BaseDomain;

/**
 * <p>
 * 后台操作员
 * </p>
 *
 * @author 裘晓伟
 * @since 2018-07-12
 */
public class Operator extends BaseDomain {
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
    /**
     * 操作员Id
     */
	private Integer operatorId;
    /**
     * 真实姓名
     */
	private String realName;
    /**
     * 登录名
     */
	private String loginName;
    /**
     * 登录密码
     */
	private String loginPwd;
    /**
     * 电话
     */
	private String phoneTel;
    /**
     * 邮箱
     */
	private String email;
    /**
     * 角色Id
     */
	private Integer roleinfoId;


	/**角色名称（显示时使用）*/
    private String roleName;
    
    //商户编码
    private String oper_code;
    
    private String lastTime;
    
    private String failTimes;
    //企业名称
    private String companyName;
    //信用代码
    private String unifiedCode;
    //法人姓名
    private String corporate;
    //法人手机号
    private String phoneNo;
    //经办人姓名
    private String jbrxm;
    //经办人联系方式
	private String jbrphone;
    
	public Integer getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginPwd() {
		return loginPwd;
	}

	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	public String getPhoneTel() {
		return phoneTel;
	}

	public void setPhoneTel(String phoneTel) {
		this.phoneTel = phoneTel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getRoleinfoId() {
		return roleinfoId;
	}

	public void setRoleinfoId(Integer roleinfoId) {
		this.roleinfoId = roleinfoId;
	}
	

	public String getOper_code() {
		return oper_code;
	}

	public void setOper_code(String oper_code) {
		this.oper_code = oper_code;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getUnifiedCode() {
		return unifiedCode;
	}

	public void setUnifiedCode(String unifiedCode) {
		this.unifiedCode = unifiedCode;
	}

	public String getCorporate() {
		return corporate;
	}

	public void setCorporate(String corporate) {
		this.corporate = corporate;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	@Override
	public String toString() {
		return "Operator{" +
			"operatorId=" + operatorId +
			", realName=" + realName +
			", loginName=" + loginName +
			", loginPwd=" + loginPwd +
			", phoneTel=" + phoneTel +
			", email=" + email +
			", roleinfoId=" + roleinfoId +
			"}";
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getLastTime() {
		return lastTime;
	}

	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}

	public String getFailTimes() {
		return failTimes;
	}

	public void setFailTimes(String failTimes) {
		this.failTimes = failTimes;
	}

	public String getJbrxm() {
		return jbrxm;
	}

	public void setJbrxm(String jbrxm) {
		this.jbrxm = jbrxm;
	}

	public String getJbrphone() {
		return jbrphone;
	}

	public void setJbrphone(String jbrphone) {
		this.jbrphone = jbrphone;
	}
	
}
