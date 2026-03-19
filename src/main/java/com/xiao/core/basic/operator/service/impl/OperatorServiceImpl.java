package com.xiao.core.basic.operator.service.impl;

import com.xiao.base.BaseMapper;
import com.xiao.base.BaseServiceImpl;
import com.xiao.core.basic.admin_roleinfo.domain.AdminRoleinfo;
import com.xiao.core.basic.operator.domain.Operator;
import com.xiao.core.basic.operator.domain.OperatorDetailDTO;
import com.xiao.core.basic.operator.domain.OperatorListDTO;
import com.xiao.core.basic.operator.mapper.OperatorMapper;
import com.xiao.core.basic.operator.service.OperatorService;
import com.xiao.core.data_permission.constants.PermissionTypeConstants;
import com.xiao.core.data_permission.domain.BranchInfo;
import com.xiao.core.data_permission.domain.DataPermission;
import com.xiao.core.data_permission.domain.DataRole;
import com.xiao.core.data_permission.domain.DepartmentInfo;
import com.xiao.core.data_permission.domain.UserDataRole;
import com.xiao.core.data_permission.service.BranchInfoService;
import com.xiao.core.data_permission.service.DataPermissionService;
import com.xiao.core.data_permission.service.DataRoleService;
import com.xiao.core.data_permission.service.DepartmentInfoService;
import com.xiao.core.data_permission.service.UserDataRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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

	@Resource
	private DepartmentInfoService departmentInfoService;

	@Resource
	private BranchInfoService branchInfoService;

	@Resource
	private UserDataRoleService userDataRoleService;

	@Resource
	private DataRoleService dataRoleService;

	@Resource
	private DataPermissionService dataPermissionService;

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

	@Override
	public OperatorDetailDTO getOperatorDetail(Integer operatorId) {
		Operator operator = operatorMapper.queryById(String.valueOf(operatorId));
		if (operator == null) {
			return null;
		}

		OperatorDetailDTO dto = new OperatorDetailDTO();
		dto.setOperatorId(operator.getOperatorId());
		dto.setRealName(operator.getRealName());
		dto.setLoginName(operator.getLoginName());
		dto.setPhoneTel(operator.getPhoneTel());
		dto.setEmail(operator.getEmail());
		dto.setRoleinfoId(operator.getRoleinfoId());
		dto.setRoleName(operator.getRoleName());
		dto.setOper_code(operator.getOper_code());
		dto.setLastTime(operator.getLastTime());
		dto.setFailTimes(operator.getFailTimes());
		dto.setCompanyName(operator.getCompanyName());
		dto.setUnifiedCode(operator.getUnifiedCode());
		dto.setCorporate(operator.getCorporate());
		dto.setPhoneNo(operator.getPhoneNo());
		dto.setJbrxm(operator.getJbrxm());
		dto.setJbrphone(operator.getJbrphone());
		dto.setDeptId(operator.getDeptId());
		dto.setSugOrgId(operator.getSugOrgId());

		if (operator.getDeptId() != null) {
			DepartmentInfo department = departmentInfoService.getDepartmentById(operator.getDeptId());
			dto.setDepartment(department);
		}

		if (operator.getSugOrgId() != null) {
			BranchInfo branch = branchInfoService.getBranchById(operator.getSugOrgId());
			dto.setBranch(branch);
		}

		if (operator.getRoleinfoId() != null) {
			List<AdminRoleinfo> roleList = operatorMapper.queryAllRole(null);
			for (AdminRoleinfo role : roleList) {
				if (role.getRoleInfoId() == operator.getRoleinfoId()) {
					OperatorDetailDTO.AdminRoleInfoVO functionRole = new OperatorDetailDTO.AdminRoleInfoVO();
					functionRole.setRoleInfoId(role.getRoleInfoId());
					functionRole.setRoleName(role.getRoleName());
					functionRole.setRemark(role.getRemark());
					dto.setFunctionRole(functionRole);
					break;
				}
			}
		}

		List<UserDataRole> userDataRoles = userDataRoleService.getRolesByUserId(operatorId);
		if (userDataRoles != null && !userDataRoles.isEmpty()) {
			List<OperatorDetailDTO.DataRoleInfoVO> dataRoleVOList = new ArrayList<>();
			for (UserDataRole udr : userDataRoles) {
				DataRole dataRole = dataRoleService.getRoleById(udr.getRoleId());
				if (dataRole != null) {
					OperatorDetailDTO.DataRoleInfoVO roleVO = new OperatorDetailDTO.DataRoleInfoVO();
					roleVO.setId(dataRole.getId());
					roleVO.setRoleName(dataRole.getRoleName());
					roleVO.setRoleCode(dataRole.getRoleCode());
					roleVO.setDescription(dataRole.getDescription());

					List<DataPermission> permissions = dataPermissionService.getPermissionsByRoleId(dataRole.getId());
					if (permissions != null && !permissions.isEmpty()) {
						List<OperatorDetailDTO.DataPermissionInfoVO> permissionVOList = new ArrayList<>();
						for (DataPermission dp : permissions) {
							OperatorDetailDTO.DataPermissionInfoVO permVO = new OperatorDetailDTO.DataPermissionInfoVO();
							permVO.setId(dp.getId());
							permVO.setPermissionType(dp.getPermissionType());
							permVO.setPermissionValue(dp.getPermissionValue());
							permVO.setPermissionTypeName(getPermissionTypeName(dp.getPermissionType()));
							permissionVOList.add(permVO);
						}
						roleVO.setPermissions(permissionVOList);
					}

					dataRoleVOList.add(roleVO);
				}
			}
			dto.setDataRoles(dataRoleVOList);
		}

		return dto;
	}

	private String getPermissionTypeName(String permissionType) {
		if (permissionType == null) {
			return "";
		}
		switch (permissionType) {
			case PermissionTypeConstants.CUSTOMER:
				return "客户";
			case PermissionTypeConstants.INDUSTRY:
				return "行业";
			case PermissionTypeConstants.PRODUCT_TYPE:
				return "产品类型";
			case PermissionTypeConstants.PRODUCT_LINE:
				return "产品线";
			case PermissionTypeConstants.WAREHOUSE:
				return "仓库";
			case PermissionTypeConstants.BRANCH:
				return "分支机构";
			case PermissionTypeConstants.DEPARTMENT:
				return "部门";
			case PermissionTypeConstants.EMPLOYEE:
				return "员工";
			case PermissionTypeConstants.ALL:
				return "全部数据";
			case PermissionTypeConstants.CURRENT_USER:
				return "仅当前用户";
			default:
				return permissionType;
		}
	}

	@Override
	public List<OperatorListDTO> getOperatorListWithDetails(Map<String, Object> params) {
		List<Operator> operators = operatorMapper.queryByMap(params);
		List<OperatorListDTO> result = new ArrayList<>();

		Map<Integer, String> deptNameMap = new HashMap<>();
		Map<Integer, String> branchNameMap = new HashMap<>();

		for (Operator operator : operators) {
			OperatorListDTO dto = new OperatorListDTO();
			dto.setOperatorId(operator.getOperatorId());
			dto.setRealName(operator.getRealName());
			dto.setLoginName(operator.getLoginName());
			dto.setPhoneTel(operator.getPhoneTel());
			dto.setEmail(operator.getEmail());
			dto.setRoleinfoId(operator.getRoleinfoId());
			dto.setRoleName(operator.getRoleName());
			dto.setOper_code(operator.getOper_code());
			dto.setLastTime(operator.getLastTime());
			dto.setFailTimes(operator.getFailTimes());
			dto.setCompanyName(operator.getCompanyName());
			dto.setUnifiedCode(operator.getUnifiedCode());
			dto.setCorporate(operator.getCorporate());
			dto.setPhoneNo(operator.getPhoneNo());
			dto.setJbrxm(operator.getJbrxm());
			dto.setJbrphone(operator.getJbrphone());
			dto.setDeptId(operator.getDeptId());
			dto.setSugOrgId(operator.getSugOrgId());

			if (operator.getDeptId() != null) {
				String deptName = deptNameMap.get(operator.getDeptId());
				if (deptName == null) {
					DepartmentInfo dept = departmentInfoService.getDepartmentById(operator.getDeptId());
					if (dept != null) {
						deptName = dept.getDeptName();
						deptNameMap.put(operator.getDeptId(), deptName);
					}
				}
				dto.setDeptName(deptName);
			}

			if (operator.getSugOrgId() != null) {
				String branchName = branchNameMap.get(operator.getSugOrgId());
				if (branchName == null) {
					BranchInfo branch = branchInfoService.getBranchById(operator.getSugOrgId());
					if (branch != null) {
						branchName = branch.getBranchName();
						branchNameMap.put(operator.getSugOrgId(), branchName);
					}
				}
				dto.setBranchName(branchName);
			}

			List<UserDataRole> userDataRoles = userDataRoleService.getRolesByUserId(operator.getOperatorId());
			if (userDataRoles != null && !userDataRoles.isEmpty()) {
				List<OperatorListDTO.DataRoleSimpleVO> dataRoleVOList = new ArrayList<>();
				for (UserDataRole udr : userDataRoles) {
					DataRole dataRole = dataRoleService.getRoleById(udr.getRoleId());
					if (dataRole != null) {
						OperatorListDTO.DataRoleSimpleVO roleVO = new OperatorListDTO.DataRoleSimpleVO();
						roleVO.setId(dataRole.getId());
						roleVO.setRoleName(dataRole.getRoleName());
						roleVO.setRoleCode(dataRole.getRoleCode());
						dataRoleVOList.add(roleVO);
					}
				}
				dto.setDataRoles(dataRoleVOList);
			}

			result.add(dto);
		}

		return result;
	}
}
