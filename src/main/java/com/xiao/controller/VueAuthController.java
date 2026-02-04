package com.xiao.controller;

import com.xiao.base.ResultModel;
import com.xiao.core.basic.admin_menu.domain.AdminMenu;
import com.xiao.core.basic.admin_menu.service.AdminMenuService;
import com.xiao.core.basic.operator.domain.Operator;
import com.xiao.core.basic.operator.service.OperatorService;
import com.xiao.core.basic.operator_log.service.OperatorLogService;
import com.xiao.logannotation.CurrentUser;
import com.xiao.logannotation.LoginRequired;
import com.xiao.tokenmagnager.TokenManager;
import com.xiao.util.MethodUtil;
import com.xiao.util.RedisUtils;
import com.xiao.util.StringUtil;
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
        user.put("avatar", ""); // 默认头像
        
        // 角色列表
        List<String> roles = new ArrayList<>();
        if (oper.getRoleinfoId() != null) {
            roles.add("role_" + oper.getRoleinfoId());
        }
        
        // 权限列表
        List<String> permissions = new ArrayList<>();
        if (list != null) {
            for (AdminMenu menu : list) {
                if (menu.getMenuUrl() != null && !menu.getMenuUrl().isEmpty()) {
                    permissions.add(menu.getMenuUrl());
                }
                if (menu.getSubMenuList() != null) {
                    for (AdminMenu subMenu : menu.getSubMenuList()) {
                        if (subMenu.getMenuUrl() != null && !subMenu.getMenuUrl().isEmpty()) {
                            permissions.add(subMenu.getMenuUrl());
                        }
                    }
                }
            }
        }
        
        userInfo.put("user", user);
        userInfo.put("roles", roles);
        userInfo.put("permissions", permissions);
        
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
}