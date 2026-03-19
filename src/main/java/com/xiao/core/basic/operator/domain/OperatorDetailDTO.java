package com.xiao.core.basic.operator.domain;

import com.xiao.core.data_permission.domain.BranchInfo;
import com.xiao.core.data_permission.domain.DataPermission;
import com.xiao.core.data_permission.domain.DataRole;
import com.xiao.core.data_permission.domain.DepartmentInfo;

import java.util.List;

public class OperatorDetailDTO {

    private Integer operatorId;
    private String realName;
    private String loginName;
    private String phoneTel;
    private String email;
    private Integer roleinfoId;
    private String roleName;
    private String oper_code;
    private String lastTime;
    private String failTimes;
    private String companyName;
    private String unifiedCode;
    private String corporate;
    private String phoneNo;
    private String jbrxm;
    private String jbrphone;
    private Integer deptId;
    private Integer sugOrgId;

    private DepartmentInfo department;
    private BranchInfo branch;
    private AdminRoleInfoVO functionRole;
    private List<DataRoleInfoVO> dataRoles;

    public static class AdminRoleInfoVO {
        private Integer roleInfoId;
        private String roleName;
        private String remark;

        public Integer getRoleInfoId() {
            return roleInfoId;
        }

        public void setRoleInfoId(Integer roleInfoId) {
            this.roleInfoId = roleInfoId;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }

    public static class DataRoleInfoVO {
        private Integer id;
        private String roleName;
        private String roleCode;
        private String description;
        private List<DataPermissionInfoVO> permissions;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public String getRoleCode() {
            return roleCode;
        }

        public void setRoleCode(String roleCode) {
            this.roleCode = roleCode;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<DataPermissionInfoVO> getPermissions() {
            return permissions;
        }

        public void setPermissions(List<DataPermissionInfoVO> permissions) {
            this.permissions = permissions;
        }
    }

    public static class DataPermissionInfoVO {
        private Integer id;
        private String permissionType;
        private String permissionValue;
        private String permissionTypeName;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getPermissionType() {
            return permissionType;
        }

        public void setPermissionType(String permissionType) {
            this.permissionType = permissionType;
        }

        public String getPermissionValue() {
            return permissionValue;
        }

        public void setPermissionValue(String permissionValue) {
            this.permissionValue = permissionValue;
        }

        public String getPermissionTypeName() {
            return permissionTypeName;
        }

        public void setPermissionTypeName(String permissionTypeName) {
            this.permissionTypeName = permissionTypeName;
        }
    }

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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getOper_code() {
        return oper_code;
    }

    public void setOper_code(String oper_code) {
        this.oper_code = oper_code;
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

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public Integer getSugOrgId() {
        return sugOrgId;
    }

    public void setSugOrgId(Integer sugOrgId) {
        this.sugOrgId = sugOrgId;
    }

    public DepartmentInfo getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentInfo department) {
        this.department = department;
    }

    public BranchInfo getBranch() {
        return branch;
    }

    public void setBranch(BranchInfo branch) {
        this.branch = branch;
    }

    public AdminRoleInfoVO getFunctionRole() {
        return functionRole;
    }

    public void setFunctionRole(AdminRoleInfoVO functionRole) {
        this.functionRole = functionRole;
    }

    public List<DataRoleInfoVO> getDataRoles() {
        return dataRoles;
    }

    public void setDataRoles(List<DataRoleInfoVO> dataRoles) {
        this.dataRoles = dataRoles;
    }
}
