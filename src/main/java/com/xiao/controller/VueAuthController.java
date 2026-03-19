package com.xiao.controller;

import com.xiao.base.ResultModel;
import com.xiao.core.basic.admin_btn.domain.AdminBtn;
import com.xiao.core.basic.admin_menu.domain.AdminMenu;
import com.xiao.core.basic.admin_menu.service.AdminMenuService;
import com.xiao.core.basic.operator.domain.Operator;
import com.xiao.core.basic.operator.service.OperatorService;
import com.xiao.core.basic.operator_log.service.OperatorLogService;
import com.xiao.core.data_permission.domain.DataPermission;
import com.xiao.core.data_permission.domain.DataRole;
import com.xiao.core.data_permission.domain.DepartmentInfo;
import com.xiao.core.data_permission.domain.UserDataRole;
import com.xiao.core.data_permission.service.DataPermissionService;
import com.xiao.core.data_permission.service.DataRoleService;
import com.xiao.core.data_permission.service.DepartmentInfoService;
import com.xiao.core.data_permission.service.UserDataRoleService;
import com.xiao.logannotation.CurrentUser;
import com.xiao.logannotation.LoginRequired;
import com.xiao.tokenmagnager.TokenManager;
import com.xiao.util.MethodUtil;
import com.xiao.util.RedisUtils;
import com.xiao.util.StringUtil;
import com.xiao.core.basic.admin_roleinfo.service.AdminRoleinfoService;
import com.xiao.core.basic.admin_roleinfo.domain.AdminRoleinfo;
import com.xiao.core.basic.admin_rolemenu.domain.AdminRolemenu;
import com.xiao.core.basic.admin_rolebtn.domain.AdminRolebtn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Vue-Element-Admin 专用认证控制器
 * 提供与 vue-element-admin 完全兼容的接口
 */
@RestController
@RequestMapping("/api")
public class VueAuthController {
    
    @Autowired
    private OperatorService operService;
    
    @Autowired
    private OperatorLogService operateLogService;
    
    @Autowired
    private TokenManager tokenManager;
    
    @Autowired
    private RedisUtils redisUtils;
    
    @Autowired
    private AdminMenuService menuService;
    
    @Autowired
    private UserDataRoleService userDataRoleService;
    
    @Autowired
    private DataRoleService dataRoleService;
    
    @Autowired
    private DataPermissionService dataPermissionService;
    
    @Autowired
    private DepartmentInfoService departmentInfoService;
    
    @Autowired
    private AdminRoleinfoService roleInfoService;
    
    /**
     * 登录接口
     * POST /api/auth/login
     */
    @PostMapping("/auth/login")
    public ResultModel login(@RequestBody Map<String, String> params, 
                            HttpServletRequest request, 
                            HttpServletResponse response) {
        String username = params.get("username");
        String password = params.get("password");
        String code = params.get("code"); // 验证码
        String captchaKey = params.get("captchaKey"); // 验证码key
        
        // 检查必要参数
        if (StringUtil.isEmpty(username)) {
            return ResultModel.error("账号不能为空.");
        }
        if (StringUtil.isEmpty(password)) {
            return ResultModel.error("密码不能为空.");
        }
        
        // 验证码检查（可选）
        if (!StringUtil.isEmpty(captchaKey) && !StringUtil.isEmpty(code)) {
            String serverCode = (String) redisUtils.get(captchaKey);
            if (StringUtil.isEmpty(serverCode)) {
                return ResultModel.error("验证码已失效请刷新后重试.");
            }
            redisUtils.del(captchaKey);
            if (!code.equalsIgnoreCase(serverCode)) {
                return ResultModel.error("验证码输入错误!");
            }
        }
        
        // 查询用户
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("loginNameQuery", username);
        List<Operator> operList = operService.queryByMap(queryParams);
        
        if (operList == null || operList.size() < 1) {
            return ResultModel.error("账号或者密码输入错误！");
        }
        
        Operator operator = operList.get(0);
        
        // 检查账号锁定状态
        if ("3".equals(operator.getFailTimes()) && operator.getLastTime() != null) {
            long times = getDatePoor(operator.getLastTime());
            if (times < 30) {
                return ResultModel.error("该账号已被锁定，请联系管理员或30分钟后再登录!");
            } else {
                operator.setFailTimes("0");
            }
        }
        
        // 验证密码
        Operator oper = operService.queryLogin(username, MethodUtil.MD5(password));
        if (oper == null) {
            // 登录失败处理
            handleLoginFailure(operator);
            return ResultModel.error("账号或者密码输入错误.");
        }
        
        // 检查权限
        if (oper.getRoleinfoId() == null || StringUtil.isEmpty(String.valueOf(oper.getRoleinfoId())) || 0 == oper.getRoleinfoId()) {
            return ResultModel.error("没有访问权限，请联系管理员.");
        }
        
        // 生成token
        String token = tokenManager.createToken(oper.getOperatorId() + "");
        if (StringUtil.isEmpty(token)) {
            return ResultModel.error("登录失败,联系管理员");
        }
        
        // 更新登录状态
        oper.setFailTimes("0");
        oper.setLastTime("empt");
        operService.update(oper);
        
        // 记录登录日志
        operateLogService.insertOperLog("用户登录", oper);
        
        // 返回 vue-element-admin 格式
        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("token", token);
        tokenData.put("accessToken", token);
        return ResultModel.success("登录成功", tokenData);
    }
    
    /**
     * 获取用户信息
     * GET /api/user/info
     */
    @LoginRequired(remark="获取用户信息")
    @GetMapping("/user/info")
    public ResultModel getUserInfo(@CurrentUser Operator oper) {
        List<AdminMenu> list = null;
        if (redisUtils.hasKey("permissions_" + oper.getOperatorId())) {
            list = (List<AdminMenu>) redisUtils.get("permissions_" + oper.getOperatorId());
        }
        
        if (list == null || list.size() == 0) {
            Map<String, Object> params = new HashMap<>();
            params.put("roleInfoId", oper.getRoleinfoId());
            list = menuService.queryChildMenuByUserSub(params);
            
            // 缓存权限
            redisUtils.set("permissions_" + oper.getOperatorId(), list);
        }
        
        // 构建响应数据
        Map<String, Object> userInfo = new HashMap<>();
        
        // 用户基本信息
         Map<String, Object> user = new HashMap<>();
         user.put("userId", oper.getOperatorId());
         user.put("username", oper.getLoginName());
         user.put("realName", oper.getRealName());
         user.put("roleinfoId", oper.getRoleinfoId());
         user.put("roleName", oper.getRoleName());
         user.put("avatar", ""); // 默认头像
        
        // 角色列表
        List<String> roles = new ArrayList<>();
        if (oper.getRoleinfoId() != null) {
            roles.add("role_" + oper.getRoleinfoId());
        }
        
        // 权限列表 - 完整的父子关系菜单结构，包含按钮权限
        List<AdminMenu> permissions = list;
        
        // 递归获取所有级别的菜单
        if (permissions != null && !permissions.isEmpty()) {
            for (AdminMenu menu : permissions) {
                // 查询当前菜单的子菜单（如果有）
                loadChildMenus(menu, oper.getRoleinfoId() + "");
                
                // 查询当前菜单的按钮权限
                List<AdminBtn> menuBtns = menuService.queryMenuBtn(oper.getRoleinfoId() + "");
                if (menuBtns != null && !menuBtns.isEmpty()) {
                    List<String> btnList = new ArrayList<>();
                    for (AdminBtn btn : menuBtns) {
                        if (btn.getMenuURL() != null && btn.getMenuURL().equals(menu.getMenuUrl())) {
                            btnList.add(btn.getBtnType());
                        }
                    }
                    menu.setBtnList(btnList);
                }
            }
        }
        
        // 获取用户数据权限
        List<Map<String, Object>> dataPermissions = new ArrayList<>();
        List<UserDataRole> userDataRoles = userDataRoleService.getRolesByUserId(oper.getOperatorId());
        if (userDataRoles != null && !userDataRoles.isEmpty()) {
            for (UserDataRole userDataRole : userDataRoles) {
                DataRole dataRole = dataRoleService.getRoleById(userDataRole.getRoleId());
                if (dataRole != null) {
                    Map<String, Object> dataRoleMap = new HashMap<>();
                    dataRoleMap.put("roleId", dataRole.getId());
                    dataRoleMap.put("roleName", dataRole.getRoleName());
                    dataRoleMap.put("roleCode", dataRole.getRoleCode());
                    dataRoleMap.put("description", dataRole.getDescription());
                    
                    // 获取数据角色的权限
                    List<DataPermission> dataRolePermissions = dataPermissionService.getPermissionsByRoleId(dataRole.getId());
                    if (dataRolePermissions != null && !dataRolePermissions.isEmpty()) {
                        // 优化：合并相同类型的权限，基于具体ID去重
                        Map<String, Set<String>> permissionTypeValuesMap = new HashMap<>();
                        Map<String, Integer> permissionTypeIdsMap = new HashMap<>();
                        
                        for (DataPermission permission : dataRolePermissions) {
                            String type = permission.getPermissionType();
                            if (!permissionTypeValuesMap.containsKey(type)) {
                                permissionTypeValuesMap.put(type, new HashSet<>());
                                permissionTypeIdsMap.put(type, permission.getId());
                            }
                            
                            // 拆分权限值并添加到对应类型的集合中
                            String[] values = permission.getPermissionValue().split(",");
                            for (String value : values) {
                                value = value.trim();
                                if (!value.isEmpty()) {
                                    permissionTypeValuesMap.get(type).add(value);
                                }
                            }
                        }
                        
                        // 构建合并后的权限列表
                        List<Map<String, Object>> permissionList = new ArrayList<>();
                        for (Map.Entry<String, Set<String>> entry : permissionTypeValuesMap.entrySet()) {
                            String type = entry.getKey();
                            Set<String> values = entry.getValue();
                            
                            if (!values.isEmpty()) {
                                Map<String, Object> permissionMap = new HashMap<>();
                                permissionMap.put("permissionId", permissionTypeIdsMap.get(type));
                                permissionMap.put("permissionType", type);
                                
                                // 合并权限值，用逗号分隔
                                String mergedValue = String.join(",", values);
                                permissionMap.put("permissionValue", mergedValue);
                                permissionMap.put("roleId", dataRole.getId());
                                
                                // 根据权限类型和合并后的权限值查询对应的名称
                                String permissionName = getPermissionName(type, mergedValue);
                                permissionMap.put("permissionName", permissionName);
                                
                                permissionList.add(permissionMap);
                            }
                        }
                        dataRoleMap.put("permissions", permissionList);
                    }
                    
                    dataPermissions.add(dataRoleMap);
                }
            }
        }
        
        userInfo.put("user", user);
        userInfo.put("roles", roles);
        userInfo.put("permissions", permissions);
        userInfo.put("dataPermissions", dataPermissions);
        
        return ResultModel.success(userInfo);
    }
    
    /**
     * 退出登录
     * POST /api/user/logout
     */
    @PostMapping("/user/logout")
    public ResultModel logout(HttpServletRequest request) {
        String accessToken = request.getHeader("token");
        if (StringUtil.isEmpty(accessToken)) {
            accessToken = request.getParameter("token");
        }
        
        if (!StringUtil.isEmpty(accessToken)) {
            String memberId = tokenManager.getToken(accessToken);
            if (!StringUtil.isEmpty(memberId)) {
                tokenManager.deleteToken(memberId);
                redisUtils.del("permissions_" + memberId);
                redisUtils.del("adminAuth_" + memberId);
            }
        }
        
        return ResultModel.success("退出成功");
    }
    
    /**
     * 获取验证码
     * GET /api/auth/captcha
     */
    @GetMapping("/auth/captcha")
    public ResultModel getCaptcha() {
        // 生成简单的验证码（数字）
        String captcha = generateCaptcha();
        String captchaKey = UUID.randomUUID().toString();
        
        // 存储验证码，5分钟有效
        redisUtils.set(captchaKey, captcha, 300);
        
        Map<String, Object> result = new HashMap<>();
        result.put("captchaKey", captchaKey);
        result.put("captcha", captcha); // 返回明文验证码，前端可以显示
        
        return ResultModel.success(result);
    }
    
    private void handleLoginFailure(Operator operator) {
        String failTimes = operator.getFailTimes();
        if ("0".equals(failTimes)) {
            operator.setFailTimes("1");
        } else if ("1".equals(failTimes)) {
            long lastTime = getDatePoor(operator.getLastTime());
            if (lastTime > 2) {
                operator.setFailTimes("1");
            } else {
                operator.setFailTimes("2");
            }
        } else if ("2".equals(failTimes)) {
            long lastTime = getDatePoor(operator.getLastTime());
            if (lastTime > 2) {
                operator.setFailTimes("1");
            } else {
                operator.setFailTimes("3");
            }
        } else {
            operator.setFailTimes("0");
        }
        operator.setLastTime("fail");
        operService.update(operator);
    }
    
    private long getDatePoor(String startDates) {
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startDate = format.parse(startDates);
            Date nowDate = new Date();
            long nd = 1000 * 24 * 60 * 60;
            long nm = 1000 * 60;
            long diff = nowDate.getTime() - startDate.getTime();
            return diff % nd / nm;
        } catch (Exception e) {
            return 0;
        }
    }
    
    private String generateCaptcha() {
        // 生成6位随机数字
        Random random = new Random();
        StringBuilder captcha = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            captcha.append(random.nextInt(10));
        }
        return captcha.toString();
    }
    
    /**
     * 根据权限类型和权限值查询对应的名称
     */
    private String getPermissionName(String permissionType, String permissionValue) {
        if (permissionType == null || permissionValue == null) {
            return "";
        }
        
        try {
            // 处理逗号分隔的多个权限值
            String[] values = permissionValue.split(",");
            List<String> names = new ArrayList<>();
            
            for (String value : values) {
                value = value.trim();
                if (value.isEmpty()) {
                    continue;
                }
                
                switch (permissionType) {
                    case "DEPARTMENT":
                        // 查询部门名称
                        Integer deptId = Integer.parseInt(value);
                        DepartmentInfo department = departmentInfoService.getDepartmentById(deptId);
                        if (department != null) {
                            names.add(department.getDeptName());
                        } else {
                            names.add(value);
                        }
                        break;
                    case "EMPLOYEE":
                        // 查询员工真实姓名
                        Integer employeeId = Integer.parseInt(value);
                        Operator employee = operService.queryById(employeeId.toString());
                        if (employee != null) {
                            names.add(employee.getRealName());
                        } else {
                            names.add(value);
                        }
                        break;
                    // 可以根据需要添加其他权限类型的处理
                    default:
                        names.add(value);
                        break;
                }
            }
            
            if (!names.isEmpty()) {
                return String.join(", ", names);
            }
        } catch (Exception e) {
            // 处理所有异常，确保方法不会抛出错误
        }
        
        return permissionValue;
    }
    
    /**
     * 递归加载所有级别的子菜单
     */
    private void loadChildMenus(AdminMenu menu, String roleInfoId) {
        if (menu == null) {
            return;
        }
        
        // 查询当前菜单的子菜单
        Map<String, Object> params = new HashMap<>();
        params.put("roleInfoId", roleInfoId);
        params.put("parentNo", menu.getMenuId());
        List<AdminMenu> childMenus = menuService.queryMenuByUser(params);
        
        if (childMenus != null && !childMenus.isEmpty()) {
            menu.setSubMenuList(childMenus);
            
            // 递归加载子菜单的子菜单
            for (AdminMenu childMenu : childMenus) {
                loadChildMenus(childMenu, roleInfoId);
                
                // 查询子菜单的按钮权限
                List<AdminBtn> childMenuBtns = menuService.queryMenuBtn(roleInfoId);
                if (childMenuBtns != null && !childMenuBtns.isEmpty()) {
                    List<String> childBtnList = new ArrayList<>();
                    for (AdminBtn btn : childMenuBtns) {
                        if (btn.getMenuURL() != null && btn.getMenuURL().equals(childMenu.getMenuUrl())) {
                            childBtnList.add(btn.getBtnType());
                        }
                    }
                    childMenu.setBtnList(childBtnList);
                }
            }
        }
    }

    /**
     * 获取数据权限
     * GET /vue-element-admin/data-permissions
     */
    @LoginRequired(remark="获取数据权限")
    @GetMapping("/vue-element-admin/data-permissions")
    public ResultModel getDataPermissions() {
        Map<String, Object> data = new HashMap<>();
        data.put("ids", new ArrayList<>());
        return ResultModel.success(data);
    }
    
    /**
     * 删除用户
     * POST /api/user/delOpr
     */
    @LoginRequired(remark="删除用户")
    @PostMapping("/user/delOpr")
    public ResultModel delOpr(@RequestBody Map<String, Object> params, @CurrentUser Operator oper) {
        Integer userId = (Integer) params.get("userId");
        if (userId == null) {
            return ResultModel.error("用户ID不能为空");
        }
      
        
        try {
            // 检查是否是当前用户，不能删除自己
            if (userId.equals(oper.getOperatorId())) {
                return ResultModel.error("不能删除当前登录用户");
            }
            
            // 执行删除操作
            List<String> operatorIds = new ArrayList<>();
            operatorIds.add(userId.toString());
            operService.deletes(operatorIds);
            
            // 清除用户权限缓存
            redisUtils.del("permissions_" + userId);
            redisUtils.del("adminAuth_" + userId);
            
            // 记录操作日志
            operateLogService.insertOperLog("删除用户", oper);
            
            return ResultModel.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("删除失败：" + e.getMessage());
        }
    }
    
    /**
     * 重置密码
     * POST /api/user/resetPwd
     */
    @LoginRequired(remark="重置密码")
    @PostMapping("/user/resetPwd")
    public ResultModel resetPwd(@RequestBody Map<String, Object> params, @CurrentUser Operator oper) {
        Integer userId = (Integer) params.get("userId");
        if (userId == null) {
            return ResultModel.error("用户ID不能为空");
        }
     
        
        try {
            // 查询用户
            Operator user = operService.queryById(userId.toString());
            if (user == null) {
                return ResultModel.error("用户不存在");
            }
            
            // 重置密码为默认密码 123456
            user.setLoginPwd(MethodUtil.MD5("123456"));
            user.setFailTimes("0");
            user.setLastTime("empt");
            operService.update(user);
            
            // 记录操作日志
            operateLogService.insertOperLog("重置密码", oper);
            
            return ResultModel.success("密码重置成功，默认密码：123456");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("密码重置失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取角色列表
     * GET /api/roles
     */
    @LoginRequired(remark="获取角色列表")
    @GetMapping("/roles")
    public ResultModel getRoles() {
        try {
            List<AdminRoleinfo> roles = roleInfoService.queryallRoleInfo();
            return ResultModel.success(roles);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("获取角色列表失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取角色详情
     * GET /api/roles/{id}
     */
    @LoginRequired(remark="获取角色详情")
    @GetMapping("/roles/{id}")
    public ResultModel getRole(@PathVariable Integer id) {
        try {
            AdminRoleinfo role = roleInfoService.queryById(id.toString());
            if (role == null) {
                return ResultModel.error("角色不存在");
            }
            return ResultModel.success(role);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("获取角色详情失败：" + e.getMessage());
        }
    }
    
    /**
     * 添加角色
     * POST /api/roles
     */
    @LoginRequired(remark="添加角色")
    @PostMapping("/roles")
    public ResultModel addRole(HttpServletRequest request, @CurrentUser Operator oper) {
        String roleName = request.getParameter("roleName");
        String remark = request.getParameter("remark");
        
        if (StringUtil.isEmpty(roleName)) {
            return ResultModel.error("角色名称不能为空");
        }
        
        try {
            AdminRoleinfo role = new AdminRoleinfo();
            role.setRoleName(roleName);
            role.setRemark(remark);
            role.setOperatorId(oper.getOperatorId());
            
            // 检查角色名称是否重复
            Map<String, Object> params = new HashMap<>();
            params.put("roleName", roleName);
            if (roleInfoService.queryByCount(params) > 0) {
                return ResultModel.error("角色名称已存在");
            }
            
            roleInfoService.insert(role);
            return ResultModel.success("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("添加角色失败：" + e.getMessage());
        }
    }
    
    /**
     * 修改角色
     * PUT /api/roles/{id}
     */
    @LoginRequired(remark="修改角色")
    @PutMapping("/roles/{id}")
    public ResultModel updateRole(@PathVariable Integer id, HttpServletRequest request) {
        String roleName = request.getParameter("roleName");
        String remark = request.getParameter("remark");
        
        if (StringUtil.isEmpty(roleName)) {
            return ResultModel.error("角色名称不能为空");
        }
        
        try {
            AdminRoleinfo role = roleInfoService.queryById(id.toString());
            if (role == null) {
                return ResultModel.error("角色不存在");
            }
            
            // 检查角色名称是否重复（排除当前角色）
            Map<String, Object> params = new HashMap<>();
            params.put("roleName", roleName);
            params.put("notRoleInfoId", id);
            if (roleInfoService.queryByCount(params) > 0) {
                return ResultModel.error("角色名称已存在");
            }
            
            role.setRoleName(roleName);
            role.setRemark(remark);
            roleInfoService.update(role);
            
            // 清除相关用户的权限缓存
            List<Operator> operList = operService.queryUserForSup(id.toString());
            for (Operator operator : operList) {
                redisUtils.del("permissions_" + operator.getOperatorId());
                redisUtils.del("adminAuth_" + operator.getOperatorId());
            }
            
            return ResultModel.success("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("修改角色失败：" + e.getMessage());
        }
    }
    
    /**
     * 删除角色
     * DELETE /api/roles/{id}
     */
    @LoginRequired(remark="删除角色")
    @DeleteMapping("/roles/{id}")
    public ResultModel deleteRole(@PathVariable Integer id) {
        try {
            AdminRoleinfo role = roleInfoService.queryById(id.toString());
            if (role == null) {
                return ResultModel.error("角色不存在");
            }
            
            // 检查是否有用户使用此角色
            List<Operator> operList = operService.queryUserForSup(id.toString());
            if (operList != null && !operList.isEmpty()) {
                return ResultModel.error("该角色已被用户使用，无法删除");
            }
            
            // 删除角色相关的权限
            roleInfoService.delJRoleAuthInfo(id);
            roleInfoService.delJRoleAuthBtn(id);
            
            // 删除角色
            roleInfoService.deleteById(id.toString());
            
            return ResultModel.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("删除角色失败：" + e.getMessage());
        }
    }
    
    /**
     * 角色授权
     * POST /api/roles/{id}/permissions
     */
    @LoginRequired(remark="角色授权")
    @PostMapping("/roles/{id}/permissions")
    public ResultModel grantPermissions(@PathVariable Integer id, HttpServletRequest request) {
        String authStr = request.getParameter("authStr");
        
        if (StringUtil.isEmpty(authStr)) {
            return ResultModel.error("权限信息不能为空");
        }
        
        try {
            AdminRoleinfo role = roleInfoService.queryById(id.toString());
            if (role == null) {
                return ResultModel.error("角色不存在");
            }
            
            List<AdminRolemenu> roleauthrels = new ArrayList<>();
            List<AdminRolebtn> roleBtns = new ArrayList<>();
            
            // 解析权限字符串
            getAuthinfo(roleauthrels, roleBtns, authStr, id.toString());
            
            // 更新权限
            roleInfoService.addRoleAuthInfo(id.toString(), roleauthrels);
            roleInfoService.addRolBtn(id.toString(), roleBtns);
            
            // 清除相关用户的权限缓存
            List<Operator> operList = operService.queryUserForSup(id.toString());
            for (Operator operator : operList) {
                redisUtils.del("permissions_" + operator.getOperatorId());
                redisUtils.del("adminAuth_" + operator.getOperatorId());
            }
            
            return ResultModel.success("授权成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("授权失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取角色当前权限
     * GET /api/roles/{id}/permissions
     */
    @LoginRequired(remark="获取角色权限")
    @GetMapping("/roles/{id}/permissions")
    public ResultModel getRolePermissions(@PathVariable Integer id) {
        try {
            AdminRoleinfo role = roleInfoService.queryById(id.toString());
            if (role == null) {
                return ResultModel.error("角色不存在");
            }
            
            List<AdminRolemenu> menuPermissions = roleInfoService.queryRoleAuthInfo(id.toString());
            List<AdminRolebtn> btnPermissions = roleInfoService.queryRoleAuthBtn(id.toString());
            
            Map<String, Object> permissions = new HashMap<>();
            permissions.put("menuPermissions", menuPermissions);
            permissions.put("btnPermissions", btnPermissions);
            
            return ResultModel.success(permissions);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("获取角色权限失败：" + e.getMessage());
        }
    }
    
    /**
     * 解析权限字符串
     */
    private void getAuthinfo(List<AdminRolemenu> roleauthrels, List<AdminRolebtn> roleBtns, String authStr, String roleInfoId) {
        if (StringUtil.isEmpty(authStr)) {
            return;
        }
        
        String[] args;
        if (authStr.contains("\n")) {
            args = authStr.split("\n");
        } else if (authStr.contains(",")) {
            args = authStr.split(",");
        } else {
            args = new String[]{authStr};
        }
        
        if (args != null && args.length > 0) {
            for (String item : args) {
                item = item.trim();
                if (item.isEmpty()) continue;
                
                String authid;
                if (item.contains("-")) {
                    String[] authindId = item.split("-");
                    authid = authindId.length > 1 ? authindId[1] : authindId[0];
                } else {
                    authid = item;
                }
                
                if (authid != null && !"0".equals(authid)) {
                    try {
                        int authi = Integer.parseInt(authid);
                        if (authi > 5000) {
                            // 按钮权限
                            AdminRolebtn roleBtn = new AdminRolebtn();
                            roleBtn.setBtnId(authi - 5000);
                            roleBtn.setRoleInfoId(Integer.parseInt(roleInfoId));
                            roleBtns.add(roleBtn);
                        } else {
                            // 菜单权限
                            AdminRolemenu roleauthrel = new AdminRolemenu();
                            roleauthrel.setMenuId(authi);
                            roleauthrel.setRoleInfoId(Integer.parseInt(roleInfoId));
                            roleauthrels.add(roleauthrel);
                        }
                    } catch (NumberFormatException e) {
                        // 忽略无效的权限ID
                    }
                }
            }
        }
    }
}