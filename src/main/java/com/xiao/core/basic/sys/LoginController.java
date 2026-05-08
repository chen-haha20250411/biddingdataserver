package com.xiao.core.basic.sys;

import com.google.code.kaptcha.Producer;
import com.xiao.base.BaseController;
import com.xiao.base.ResultModel;
import com.xiao.core.basic.admin_btn.domain.AdminBtn;
import com.xiao.core.basic.admin_menu.domain.AdminMenu;
import com.xiao.core.basic.admin_menu.service.AdminMenuService;
import com.xiao.core.basic.admin_roleinfo.service.AdminRoleinfoService;
import com.xiao.core.basic.operator.domain.Operator;
import com.xiao.core.basic.operator.service.OperatorService;
import com.xiao.core.basic.operator_log.service.OperatorLogService;
import com.xiao.logannotation.CurrentUser;
import com.xiao.logannotation.LoginRequired;
import com.xiao.tokenmagnager.TokenManager;
import com.xiao.util.MethodUtil;
import com.xiao.util.PasswordEncoder;
import com.xiao.util.RateLimiter;
import com.xiao.util.RedisUtils;
import com.xiao.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.http.MediaType;
//import sun.misc.BASE64Encoder;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Date:2017年4月1日上午11:02:42
 * 杭州萨莫网络科技有限公司 All Rights Reserved
 * @author zhaozf
 */
@RestController
@RequestMapping("/admin")
public class LoginController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
    AdminRoleinfoService roleInfoService;
	@Autowired
    OperatorService operService;
	@Autowired
	OperatorLogService operateLogService;
	@Autowired
    TokenManager tokenManager;
	@Autowired
    AdminMenuService MenuService;
	@Autowired
    Producer captchaProducer;
	@Autowired
	RedisUtils redisUtils;
	@Autowired
	RateLimiter rateLimiter;

	@RequestMapping(value = "/toLogin")
	public ModelAndView toLogin(HttpServletRequest req, HttpServletResponse resp) {
		// 返回数据类型
		Map<String, Object> context = getRootMap();
		return forword("login", context);

	}

	/**
	 * 用户登录
	 *
	 * @param verifyCode
	 * @param request
	 * @param response
	 * @throws ParseException 
	 * @throws UnsupportedEncodingException 
	 * @throws Exception
	 */
	@RequestMapping(value="/login",method={RequestMethod.POST,RequestMethod.GET})
	public ResultModel login(String userName, String password, String verifyCode,String uucode,String yzm,String oldyzmuuid, HttpServletRequest request, HttpServletResponse response) throws ParseException, UnsupportedEncodingException {
		log.info("Login attempt - userName: {}, password length: {}, verifyCode: {}, uucode: {}, yzm: {}, oldyzmuuid: {}", 
			userName, password != null ? password.length() : 0, verifyCode, uucode, yzm, oldyzmuuid);
		// 兼容 vue-element-admin 的参数名
		if (StringUtil.isEmpty(userName)) {
			userName = request.getParameter("username");
		}
		if (StringUtil.isEmpty(password)) {
			password = request.getParameter("password");
		}
		if (StringUtil.isEmpty(verifyCode)) {
			verifyCode = request.getParameter("code");
		}
		// 检查必要参数
		if (StringUtil.isEmpty(userName)) {
			return sendFailureMessage( "账号不能为空.");
		}
		if (StringUtil.isEmpty(password)) {
			return sendFailureMessage( "密码不能为空.");
		}
		// Base64 解码
		try {
			userName = new String(Base64.decodeBase64(userName.getBytes()),"utf-8");
			password = new String(Base64.decodeBase64(password.getBytes()),"utf-8");
		} catch (Exception e) {
			return sendFailureMessage( "用户名或密码解码错误.");
		}
		// 打印解码后的参数
		log.info("Login parameters after decoding - userName: {}, verifyCode: {}, uucode: {}, yzm: {}, oldyzmuuid: {}", 
			userName, verifyCode, uucode, yzm, oldyzmuuid);
		return processLogin(userName, password, verifyCode, uucode, yzm, oldyzmuuid, request, response);
	}

	@PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResultModel loginJson(@RequestBody Map<String, String> params, HttpServletRequest request, HttpServletResponse response) throws ParseException, UnsupportedEncodingException {
		// 打印接收到的JSON参数
		log.info("Login JSON request received - params: {}", params != null ? maskSensitiveParams(params) : "null");
		String userName = params.get("username");
		String password = params.get("password");
		String verifyCode = params.get("code");
		String uucode = params.get("uucode");
		String yzm = params.get("yzm");
		String oldyzmuuid = params.get("oldyzmuuid");
		
		// Base64 解码用户名和密码
		if (!StringUtil.isEmpty(userName)) {
			try {
				userName = new String(Base64.decodeBase64(userName.getBytes()),"utf-8");
			} catch (Exception e) {
				// 如果不是Base64编码，则使用原值
				// 这里可以记录日志，但不返回错误
			}
		}
		if (!StringUtil.isEmpty(password)) {
			try {
				password = new String(Base64.decodeBase64(password.getBytes()),"utf-8");
			} catch (Exception e) {
				// 如果不是Base64编码，则使用原值
			}
		}
		
		// 打印解码后的参数
		log.info("Login JSON parameters after decoding - userName: {}, verifyCode: {}, uucode: {}, yzm: {}, oldyzmuuid: {}", 
			userName, verifyCode, uucode, yzm, oldyzmuuid);
		
		// 调用处理逻辑
		return processLogin(userName, password, verifyCode, uucode, yzm, oldyzmuuid, request, response);
	}

	private ResultModel processLogin(String userName, String password, String verifyCode, String uucode, String yzm, String oldyzmuuid, HttpServletRequest request, HttpServletResponse response) throws ParseException, UnsupportedEncodingException {
		log.info("Process login - userName: {}, password length: {}, verifyCode: {}, uucode: {}, yzm: {}, oldyzmuuid: {}", 
			userName, password != null ? password.length() : 0, verifyCode, uucode, yzm, oldyzmuuid);
		// 检查依赖注入
		if (redisUtils == null) {
			log.error("redisUtils is not injected!");
			return sendFailureMessage("系统初始化异常，请联系管理员.");
		}
		if (operService == null) {
			log.error("operService is not injected!");
			return sendFailureMessage("系统初始化异常，请联系管理员.");
		}
		if (tokenManager == null) {
			log.error("tokenManager is not injected!");
			return sendFailureMessage("系统初始化异常，请联系管理员.");
		}
		if (operateLogService == null) {
			log.error("operateLogService is not injected!");
			return sendFailureMessage("系统初始化异常，请联系管理员.");
		}
		// 判断验证码是否正确
		if (StringUtil.isEmpty(userName)) {
			return sendFailureMessage( "账号不能为空.");
		}
		if (StringUtil.isEmpty(password)) {
			return sendFailureMessage( "密码不能为空.");
		}

		if (StringUtil.isEmpty(uucode)) {
			return sendFailureMessage( "验证码已失效请刷新后重试.");
		}
		if (StringUtil.isEmpty(verifyCode)) {
			return sendFailureMessage( "验证码输入错误!");
		}
		String serverCode =(String)redisUtils.get(uucode);
		if (StringUtil.isEmpty(serverCode)) {
			return sendFailureMessage( "验证码已失效请刷新后重试.");
		}
		redisUtils.del(uucode);
		if (!verifyCode.equalsIgnoreCase(serverCode)) {
			return sendFailureMessage( "验证码输入错误!");
		}
		if("admin".equals(userName)){
			String sjyzm =(String)redisUtils.get(oldyzmuuid);
			if (StringUtil.isEmpty(sjyzm)) {
				return sendFailureMessage( "手机验证码已失效请刷新后重试.");
			}
			if (!yzm.equalsIgnoreCase(sjyzm)) {
				return sendFailureMessage( "手机验证码输入错误!");
			}
			redisUtils.del(oldyzmuuid);
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("loginNameQuery", userName);
		List<Operator> operList = operService.queryByMap(params);
		//判断账号是否存在
		if(operList==null || operList.size()<1){
			return sendFailureMessage( "账号或者密码输入错误！");
		}
		Operator operator = operList.get(0);
		//失败3次无法登陆
		if("3".equals(operator.getFailTimes())&&operator.getLastTime()!=null){
			long times = getDatePoor(operator.getLastTime());
			if(times<30){
				return sendFailureMessage("该账号已被锁定，请联系管理员或30分钟后再登录!");
			}else{
				operator.setFailTimes("0");
			}
		}
		String msg = "用户登录日志:";
		Operator oper = operService.queryLogin(userName,MethodUtil.MD5(password));
		if (oper == null) {
			//第一次失败,失败次数
			if("0".equals(operator.getFailTimes())){
				operator.setFailTimes("1");
			}else if("1".equals(operator.getFailTimes())){
				//第二次失败
				long lastime = getDatePoor(operator.getLastTime());
				if(lastime>2){
					operator.setFailTimes("1");
				}else{
					operator.setFailTimes("2");
				}
			}else if("2".equals(operator.getFailTimes())){
				//第3次失败
				long lastime = getDatePoor(operator.getLastTime());
				if(lastime>2){
					operator.setFailTimes("1");
				}else{
					operator.setFailTimes("3");
				}
			}else{
				operator.setFailTimes("0");
			}
			operator.setLastTime("fail");
			operService.update(operator);
			// 记录错误登录日志
			if("3".equals(operator.getFailTimes())){
				log.error(msg + "[" + userName + "]" + "连续登录失败！用户将被锁定30分钟!");
				return sendFailureMessage( "连续登录失败！用户将被锁定30分钟!");
			}else{
				log.error(msg + "[" + userName + "]" + "账号或者密码输入错误.");
				return sendFailureMessage( "账号或者密码输入错误.");
			}
		}
		if (oper.getRoleinfoId()==null||StringUtil.isEmpty(String.valueOf(oper.getRoleinfoId())) || 0 == oper.getRoleinfoId()) {
			return sendFailureMessage( "没有访问权限，请联系管理员.");
		}
		// //设置User到Session
		//SessionUtils.setUser(request, oper);

		String token=tokenManager.createToken(oper.getOperatorId()+"");
		if(StringUtil.isEmpty(token)){
			return sendFailureMessage("登录失败,联系管理员");
		}
		oper.setFailTimes("0");
		oper.setLastTime("empt");
		operService.update(oper);
		//登录日志记录
		operateLogService.insertOperLog("用户登录", oper);
		  //记住用户名、密码功能(注意：cookie存放密码会存在安全隐患)
//        String remFlag = request.getParameter("remFlag");
//        if("1".equals(remFlag)){ //"1"表示用户勾选记住密码
//            String loginInfo = userName+","+password;
//            Cookie userCookie=new Cookie("loginInfo",loginInfo);
//
//            userCookie.setMaxAge(30*24*60*60);   //存活期为一个月 30*24*60*60
//            userCookie.setPath("/");
//            response.addCookie(userCookie);
//        }

		// 兼容 vue-element-admin 格式，同时返回 token 和 accessToken
		Map<String, Object> tokenData = new HashMap<>();
		tokenData.put("token", token);
		tokenData.put("accessToken", token);
		return sendSuccessMessage("登录成功").setData(tokenData);
	}

	@LoginRequired(remark="获取用户信息操作")
	@RequestMapping(value="/getUserInfo",method = RequestMethod.GET)
	public ResultModel getUserInfo(@CurrentUser Operator oper) {
		log.info("Get user info request - operatorId: {}, loginName: {}, roleinfoId: {}", 
			oper.getOperatorId(), oper.getLoginName(), oper.getRoleinfoId());
		List<AdminMenu> list=null;
		if(redisUtils.hasKey("permissions_"+oper.getOperatorId())){
			list=(List<AdminMenu>)redisUtils.get("permissions_"+oper.getOperatorId());
		}
		if(list!=null&&list.size()>0){
			oper.setLoginPwd(null);
			// 构建 vue-element-admin 兼容的用户信息格式
			Map<String, Object> userInfo = buildUserInfo(oper, list);
			return sendSuccessMessage("获取用户信息成功").setData(userInfo);
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("roleInfoId", oper.getRoleinfoId());
		//查询页面能显示那些菜单
		list=MenuService.queryChildMenuByUserSub(params);
		List<String> authList = new ArrayList<String>();
		for(AdminMenu menu: list){
			for(AdminMenu sub:menu.getSubMenuList()){
				if(!StringUtil.isEmpty(sub.getMenu_auth())){
					String[] menu_auths = sub.getMenu_auth().split(";");
					for(String menu_auth:menu_auths){
						if(!authList.contains(menu_auth)){
							authList.add(menu_auth);
						}
					}
				}
			}
		}
		authList.add("/admin/getUserInfo");
		authList.add("/admin/checkPwd");
		authList.add("/admin/logout");
		authList.add("/admin/ck_orders/statistics");
		authList.add("/admin/rk_jasn/statistics");
		//显示那些按钮
		List<AdminBtn> menuBtnList = MenuService.queryMenuBtn(String.valueOf(oper.getRoleinfoId()));
		Map<String,List<String>> menuMap=new HashMap<String, List<String>>();
		for (AdminBtn adminBtn : menuBtnList) {
			List<String> str= menuMap.get(adminBtn.getMenuId()+"");
			if(str==null||str.size()<=0){
				str=new ArrayList<String>();
			}
			str.add(adminBtn.getBtnType());
			menuMap.put(adminBtn.getMenuId()+"", str);
		}
		showAllMenu(list,menuMap);
		boolean flag = false;
		if(PasswordEncoder.isDefaultPassword(oper.getLoginPwd())){
			flag = true;
		}
		//将权限放入redis中
		redisUtils.set("permissions_"+oper.getOperatorId(), list);
		redisUtils.set("adminAuth_"+oper.getOperatorId(), authList);
		// 构建 vue-element-admin 兼容的用户信息格式
		Map<String, Object> userInfo = buildUserInfo(oper, list);
		userInfo.put("flag", flag);
		return sendSuccessMessage("获取用户信息成功").setData(userInfo);
	}

	/**
	 * Vue-Element-Admin 专用获取用户信息接口
	 */
	@LoginRequired(remark="获取用户信息操作")
	@GetMapping(value = "/user/info")
	public ResultModel vueGetUserInfo(@CurrentUser Operator oper) {
		log.info("Vue get user info request - operatorId: {}, loginName: {}, roleinfoId: {}", 
			oper.getOperatorId(), oper.getLoginName(), oper.getRoleinfoId());
		List<AdminMenu> list=null;
		if(redisUtils.hasKey("permissions_"+oper.getOperatorId())){
			list=(List<AdminMenu>)redisUtils.get("permissions_"+oper.getOperatorId());
		}
		if(list!=null&&list.size()>0){
			oper.setLoginPwd(null);
			// 构建 vue-element-admin 兼容的用户信息格式
			Map<String, Object> userInfo = buildUserInfo(oper, list);
			return sendSuccessMessage("获取用户信息成功").setData(userInfo);
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("roleInfoId", oper.getRoleinfoId());
		//查询页面能显示那些菜单
		list=MenuService.queryChildMenuByUserSub(params);
		List<String> authList = new ArrayList<String>();
		for(AdminMenu menu: list){
			for(AdminMenu sub:menu.getSubMenuList()){
				if(!StringUtil.isEmpty(sub.getMenu_auth())){
					String[] menu_auths = sub.getMenu_auth().split(";");
					for(String menu_auth:menu_auths){
						if(!authList.contains(menu_auth)){
							authList.add(menu_auth);
						}
					}
				}
			}
		}
		authList.add("/admin/getUserInfo");
		authList.add("/admin/checkPwd");
		authList.add("/admin/logout");
		authList.add("/admin/ck_orders/statistics");
		authList.add("/admin/rk_jasn/statistics");
		//显示那些按钮
		List<AdminBtn> menuBtnList = MenuService.queryMenuBtn(String.valueOf(oper.getRoleinfoId()));
		Map<String,List<String>> menuMap=new HashMap<String, List<String>>();
		for (AdminBtn adminBtn : menuBtnList) {
			List<String> str= menuMap.get(adminBtn.getMenuId()+"");
			if(str==null||str.size()<=0){
				str=new ArrayList<String>();
			}
			str.add(adminBtn.getBtnType());
			menuMap.put(adminBtn.getMenuId()+"", str);
		}
		showAllMenu(list,menuMap);
		//将权限放入redis中
		redisUtils.set("permissions_"+oper.getOperatorId(), list);
		redisUtils.set("adminAuth_"+oper.getOperatorId(), authList);
		// 构建 vue-element-admin 兼容的用户信息格式
		Map<String, Object> userInfo = buildUserInfo(oper, list);
		return sendSuccessMessage("获取用户信息成功").setData(userInfo);
	}

	/**修改*/
	@LoginRequired(remark="")
	@RequestMapping(value = "/checkPwd",method={RequestMethod.POST,RequestMethod.GET})
	public ResultModel checkPwd(@CurrentUser Operator operter ){
		log.info("Check password request - operatorId: {}, loginName: {}, check default password", 
			operter.getOperatorId(), operter.getLoginName());
		if("e10adc3949ba59abbe56e057f20f883e".equals(operter.getLoginPwd())){
			return sendSuccessMessage("success").setData(true);
		}
		return sendSuccessMessage("success").setData(false);
	}
	/**
	 * 遍历按钮权限
	 * @param list
	 * @param menuMap
	 */
	private void showAllMenu(List<AdminMenu> list,Map<String,List<String>> menuMap){
		for (AdminMenu adminMenu : list) {
			if(menuMap.get(adminMenu.getMenuId()+"") != null){
				adminMenu.setBtnList(menuMap.get(adminMenu.getMenuId()+""));
			}
			if(adminMenu.getSubMenuList()!=null&&adminMenu.getSubMenuList().size()>0){
				showAllMenu(adminMenu.getSubMenuList(),menuMap);
			}
		}
	}

	/**
	 * 退出登录
	 *
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping("/logout")
	public ResultModel logout(HttpServletRequest request){
		log.info("Logout request - accessToken from header: {}", request.getHeader("token"));
		//SessionUtils.removeUser(request);
		//return "redirect:/admin/toLogin.html";
		String accessToken = request.getHeader("token");
		String member_id=tokenManager.getToken(accessToken);
		log.info("Logout processing - member_id: {}, token valid: {}", member_id, !StringUtil.isEmpty(member_id));
		//如果登录成功 生成token
		tokenManager.deleteToken(member_id);
		redisUtils.del("permissions_"+member_id);
		redisUtils.del("adminAuth_"+member_id);
		return sendSuccessMessage("退出成功");
		//response.sendRedirect(ConfUtils.get("adminUrl") + "/toLogin.html");
	}
	
	/**
	 * Vue-Element-Admin 专用登出接口
	 */
	@PostMapping("/user/logout")
	public ResultModel vueLogout(HttpServletRequest request){
		log.info("Vue logout request - accessToken from header: {}, from parameter: {}", 
			request.getHeader("token"), request.getParameter("token"));
		String accessToken = request.getHeader("token");
		if (StringUtil.isEmpty(accessToken)) {
			// 尝试从参数获取
			accessToken = request.getParameter("token");
		}
		if (!StringUtil.isEmpty(accessToken)) {
			String member_id=tokenManager.getToken(accessToken);
			log.info("Vue logout processing - member_id: {}, token valid: {}", member_id, !StringUtil.isEmpty(member_id));
			if (!StringUtil.isEmpty(member_id)) {
				tokenManager.deleteToken(member_id);
				redisUtils.del("permissions_"+member_id);
				redisUtils.del("adminAuth_"+member_id);
				log.info("Vue logout completed - cleaned token and permissions for member_id: {}", member_id);
			}
		}
		return ResultModel.success("退出成功");
	}

	@LoginRequired
	@RequestMapping(value="/chpwd",method={RequestMethod.POST,RequestMethod.GET})
	public ResultModel chpwd(@CurrentUser Operator oper, @RequestParam String newPassword, @RequestParam String oldPassword, HttpServletRequest request, HttpServletResponse response)throws Exception {
		log.info("Change password request - operatorId: {}, loginName: {}, oldPassword length: {}, newPassword length: {}", 
			oper.getOperatorId(), oper.getLoginName(), 
			oldPassword != null ? oldPassword.length() : 0, 
			newPassword != null ? newPassword.length() : 0);
		if(!PasswordEncoder.matches(oldPassword, oper.getLoginPwd())){
			return sendFailureMessage("原密码不匹配");
		}
		if(newPassword!=null && !"".equals(newPassword)){
			oper.setLoginPwd(PasswordEncoder.encodeMD5(newPassword));
		}
		tokenManager.deleteToken(oper.getOperatorId()+"");
		redisUtils.del("permissions_"+oper.getOperatorId());
		operService.update(oper);
		return sendSuccessMessage("更新成功");
	}

/*	@Auth(verifyLogin = false, verifyURL = false)
	@RequestMapping("/GetYzm")
    public void GetYzm(HttpServletResponse response, HttpServletRequest request) throws ServletException, IOException {

		response.setContentType("image/jpeg");// 设置相应类型,告诉浏览器输出的内容为图片
		response.setHeader("Pragma", "No-cache");// 设置响应头信息，告诉浏览器不要缓存此内容
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expire", 0);
		try {

			RandomValidateCode randomValidateCode = new RandomValidateCode();
			randomValidateCode.getRandcode(request, response);// 输出图片方法
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/


	@RequestMapping("/GetYzm")
    public void GetYzm(HttpServletResponse response, HttpServletRequest request) throws IOException {
//		response.setDateHeader("Expires", 0);
//        // Set standard HTTP/1.1 no-cache headers.
//        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
//        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
//        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
//        // Set standard HTTP/1.0 no-cache header.
//        response.setHeader("Pragma", "no-cache");
//        // return a jpeg
//        response.setContentType("image/jpeg");
//        // create the text for the image
//        String capText = captchaProducer.createText();
//        // store the text in the session
//        //放入会话中
//        SessionUtils.removeValidateCode(request,"0");
//        SessionUtils.setValidateCode(request, capText);
//        //request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
//        // create the image with the text
//        BufferedImage bi = captchaProducer.createImage(capText);
//        ServletOutputStream out = response.getOutputStream();
//        // write the data out
//        ImageIO.write(bi, "jpg", out);
//        try {
//            out.flush();
//        } finally {
//            out.close();
//        }

	}


	@ResponseBody
	@RequestMapping(value = "/captcha", method = RequestMethod.POST)
	public ResultModel captcha(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// IP 限流检查（60秒内最多10次）
		if (rateLimiter.isLimited(request, "captcha")) {
			return sendFailureMessage("请求过于频繁，请稍后再试");
		}

		// 生成文字验证码
		String text = captchaProducer.createText();
		log.info("Captcha generated - text: {}, length: {}", text, text.length());
		// 生成图片验证码
		ByteArrayOutputStream outputStream = null;
		BufferedImage image = captchaProducer.createImage(text);
		outputStream = new ByteArrayOutputStream();
		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(image, "jpg", outputStream);
		// 对字节数组Base64编码
		//BASE64Encoder encoder = new BASE64Encoder();
		// 生成captcha的token
		Map<String, Object> map = new HashMap<>();
		String uuid=UUID.randomUUID().toString();
		log.info("Captcha UUID generated: {}, stored in Redis with 5min TTL", uuid);
		map.put("uucode", uuid);
		map.put("img",  Base64.encodeBase64String(outputStream.toByteArray()));
		redisUtils.set(uuid,text,300);
		return sendSuccessMessage("成功").setData(map);
		//return map;
	}
	/**
	* 计算时间差、
	* @return
	 * @throws ParseException 
	 * @throws UnsupportedEncodingException 
	*/
	public static long getDatePoor(String startDates) throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = format.parse(startDates);
		Date nowDate = new Date();
	    long nd = 1000 * 24 * 60 * 60;
	    long nh = 1000 * 60 * 60;
	    long nm = 1000 * 60;
	    // long ns = 1000;
	    // 获得两个时间的毫秒时间差异
	    long diff = nowDate.getTime()-startDate.getTime();
	    /*// 计算差多少天
	    long day = diff / nd;
	    // 计算差多少小时
	    long hour = diff % nd / nh;*/
	    // 计算差多少分钟
	    long min = diff % nd / nm;
	    // 计算差多少秒//输出结果
	    // long sec = diff % nd % nh % nm / ns;
	    return  min;
	}

	/**
	 * 构建 vue-element-admin 兼容的用户信息格式
	 */
	private Map<String, Object> buildUserInfo(Operator oper, List<AdminMenu> permissions) {
		Map<String, Object> userInfo = new HashMap<>();
		
		// 用户基本信息
		Map<String, Object> user = new HashMap<>();
		user.put("userId", oper.getOperatorId());
		user.put("username", oper.getLoginName());
		user.put("realName", oper.getRealName());
		user.put("roleinfoId", oper.getRoleinfoId());
		user.put("avatar", ""); // 默认头像，可根据实际业务设置
		
		// 角色列表（根据 roleinfoId 构建）
		List<String> roles = new ArrayList<>();
		if (oper.getRoleinfoId() != null) {
			// 这里可以根据 roleinfoId 查询角色名称，暂时用角色ID作为角色标识
			roles.add("role_" + oper.getRoleinfoId());
		}
		
		// 权限列表（将菜单转换为权限字符串）
		List<String> permissionList = new ArrayList<>();
		if (permissions != null) {
			for (AdminMenu menu : permissions) {
				if (menu.getMenuUrl() != null && !menu.getMenuUrl().isEmpty()) {
					permissionList.add(menu.getMenuUrl());
				}
				if (menu.getSubMenuList() != null) {
					for (AdminMenu subMenu : menu.getSubMenuList()) {
						if (subMenu.getMenuUrl() != null && !subMenu.getMenuUrl().isEmpty()) {
							permissionList.add(subMenu.getMenuUrl());
						}
					}
				}
			}
		}
		
		userInfo.put("user", user);
		userInfo.put("roles", roles);
		userInfo.put("permissions", permissions); // 保持原菜单结构
		userInfo.put("permissionList", permissionList); // 扁平化权限列表
		
		return userInfo;
	}

	/**
	 * Vue-Element-Admin 专用登录处理逻辑
	 * 支持跳过验证码（开发环境）
	 */
	private ResultModel processVueLogin(String userName, String password, String verifyCode, String uucode, 
			HttpServletRequest request, HttpServletResponse response) throws ParseException, UnsupportedEncodingException {
		// 判断验证码是否正确（可选）
		if (StringUtil.isEmpty(userName)) {
			return sendFailureMessage( "账号不能为空.");
		}
		if (StringUtil.isEmpty(password)) {
			return sendFailureMessage( "密码不能为空.");
		}

		// 验证码检查（如果提供了验证码则验证，否则跳过）
		if (!StringUtil.isEmpty(uucode) && !StringUtil.isEmpty(verifyCode)) {
			String serverCode =(String)redisUtils.get(uucode);
			if (StringUtil.isEmpty(serverCode)) {
				return sendFailureMessage( "验证码已失效请刷新后重试.");
			}
			redisUtils.del(uucode);
			if (!verifyCode.equalsIgnoreCase(serverCode)) {
				return sendFailureMessage( "验证码输入错误!");
			}
		}
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("loginNameQuery", userName);
		List<Operator> operList = operService.queryByMap(params);
		//判断账号是否存在
		if(operList==null || operList.size()<1){
			return sendFailureMessage( "账号或者密码输入错误！");
		}
		Operator operator = operList.get(0);
		//失败3次无法登陆
		if("3".equals(operator.getFailTimes())&&operator.getLastTime()!=null){
			long times = getDatePoor(operator.getLastTime());
			if(times<30){
				return sendFailureMessage("该账号已被锁定，请联系管理员或30分钟后再登录!");
			}else{
				operator.setFailTimes("0");
			}
		}
		String msg = "用户登录日志:";
		Operator oper = operService.queryLogin(userName,MethodUtil.MD5(password));
		if (oper == null) {
			//第一次失败,失败次数
			if("0".equals(operator.getFailTimes())){
				operator.setFailTimes("1");
			}else if("1".equals(operator.getFailTimes())){
				//第二次失败
				long lastime = getDatePoor(operator.getLastTime());
				if(lastime>2){
					operator.setFailTimes("1");
				}else{
					operator.setFailTimes("2");
				}
			}else if("2".equals(operator.getFailTimes())){
				//第3次失败
				long lastime = getDatePoor(operator.getLastTime());
				if(lastime>2){
					operator.setFailTimes("1");
				}else{
					operator.setFailTimes("3");
				}
			}else{
				operator.setFailTimes("0");
			}
			operator.setLastTime("fail");
			operService.update(operator);
			// 记录错误登录日志
			if("3".equals(operator.getFailTimes())){
				log.error(msg + "[" + userName + "]" + "连续登录失败！用户将被锁定30分钟!");
				return sendFailureMessage( "连续登录失败！用户将被锁定30分钟!");
			}else{
				log.error(msg + "[" + userName + "]" + "账号或者密码输入错误.");
				return sendFailureMessage( "账号或者密码输入错误.");
			}
		}
		if (oper.getRoleinfoId()==null||StringUtil.isEmpty(String.valueOf(oper.getRoleinfoId())) || 0 == oper.getRoleinfoId()) {
			return sendFailureMessage( "没有访问权限，请联系管理员.");
		}

		String token=tokenManager.createToken(oper.getOperatorId()+"");
		if(StringUtil.isEmpty(token)){
			return sendFailureMessage("登录失败,联系管理员");
		}
		oper.setFailTimes("0");
		oper.setLastTime("empt");
		operService.update(oper);
		//登录日志记录
		operateLogService.insertOperLog("用户登录", oper);

		// 兼容 vue-element-admin 格式，同时返回 token 和 accessToken
		Map<String, Object> tokenData = new HashMap<>();
		tokenData.put("token", token);
		tokenData.put("accessToken", token);
		return sendSuccessMessage("登录成功").setData(tokenData);
	}

	/**
	 * 发送短信验证码
	 * POST /admin/sendmms
	 * 请求参数: userName (base64编码)
	 * 响应格式: {success: true, msg: "短信发送成功", data: "oldyzmuuid"}
	 */
	@PostMapping("/sendmms")
	public ResultModel sendmms(@RequestParam String userName, HttpServletRequest request) throws UnsupportedEncodingException {
		log.info("Send SMS verification code for user: {}", userName);
		
		// 解码用户名
		String decodedUserName;
		try {
			decodedUserName = new String(Base64.decodeBase64(userName.getBytes()), "utf-8");
		} catch (Exception e) {
			return sendFailureMessage("用户名解码错误");
		}
		
		// 检查用户是否存在（可选）
		Map<String, Object> params = new HashMap<>();
		params.put("loginNameQuery", decodedUserName);
		List<Operator> operList = operService.queryByMap(params);
		if (operList == null || operList.size() < 1) {
			return sendFailureMessage("用户不存在");
		}
		
		// 生成6位随机验证码
		Random random = new Random();
		StringBuilder yzm = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			yzm.append(random.nextInt(10));
		}
		String verificationCode = yzm.toString();
		
		// 生成UUID作为验证码标识
		String oldyzmuuid = UUID.randomUUID().toString();
		
		// 将验证码存储到Redis，有效期5分钟（300秒）
		redisUtils.set(oldyzmuuid, verificationCode, 300);
		
		log.info("SMS verification code generated for user {}: {}, uuid: {}", decodedUserName, verificationCode, oldyzmuuid);
		
		// 实际短信发送逻辑（此处为模拟，实际项目应调用短信服务）
		// 记录日志，表示短信已"发送"
		log.info("SMS verification code {} sent to user {}", verificationCode, decodedUserName);
		
		// 返回成功响应
		return sendSuccessMessage("短信发送成功").setData(oldyzmuuid);
	}

	/**
	 * 掩码敏感参数（用于日志打印）
	 */
	private Map<String, String> maskSensitiveParams(Map<String, String> params) {
		if (params == null) {
			return new HashMap<>();
		}
		Map<String, String> masked = new HashMap<>(params);
		// 掩码密码字段
		if (masked.containsKey("password")) {
			String password = masked.get("password");
			masked.put("password", password != null ? "***(" + password.length() + " chars)" : "null");
		}
		if (masked.containsKey("oldPassword")) {
			masked.put("oldPassword", "***");
		}
		if (masked.containsKey("newPassword")) {
			masked.put("newPassword", "***");
		}
		return masked;
	}
}
