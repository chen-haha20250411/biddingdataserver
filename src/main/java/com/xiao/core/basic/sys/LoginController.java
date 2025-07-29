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
import com.xiao.util.RedisUtils;
import com.xiao.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
//import sun.misc.BASE64Encoder;
import org.apache.commons.codec.binary.Base64;

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
		userName = new String(Base64.decodeBase64(userName.getBytes()),"utf-8");
		password = new String(Base64.decodeBase64(password.getBytes()),"utf-8");
		// 判断验证码是否正确
		if (StringUtil.isEmpty(userName)) {
			return sendFailureMessage( "账号不能为空.");
		}
		if (StringUtil.isEmpty(password)) {
			return sendFailureMessage( "密码不能为空.");
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

		return sendSuccessMessage("登录成功").putData("accessToken", token);
	}

	@LoginRequired(remark="获取用户信息操作")
	@RequestMapping(value="/getUserInfo",method = RequestMethod.GET)
	public ResultModel getUserInfo(@CurrentUser Operator oper) {
		List<AdminMenu> list=null;
		if(redisUtils.hasKey("permissions_"+oper.getOperatorId())){
			list=(List<AdminMenu>)redisUtils.get("permissions_"+oper.getOperatorId());
		}
		if(list!=null&&list.size()>0){
			oper.setLoginPwd(null);
			return sendSuccessMessage("获取用户信息成功").putData("user", oper).putData("permissions",list );
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
		if("e10adc3949ba59abbe56e057f20f883e".equals(oper.getLoginPwd())){
			flag = true;
		}
		//将权限放入redis中
		redisUtils.set("permissions_"+oper.getOperatorId(), list);
		redisUtils.set("adminAuth_"+oper.getOperatorId(), authList);
		return sendSuccessMessage("获取用户信息成功").putData("user", oper).putData("permissions", list).putData("flag", flag);
	}
	/**修改*/
	@LoginRequired(remark="")
	@RequestMapping(value = "/checkPwd",method={RequestMethod.POST,RequestMethod.GET})
	public ResultModel checkPwd(@CurrentUser Operator operter ){
		if("e10adc3949ba59abbe56e057f20f883e".equals(operter.getLoginPwd())){
			return sendSuccessMessage("true");
		}
		return sendSuccessMessage("false");
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
		//SessionUtils.removeUser(request);
		//return "redirect:/admin/toLogin.html";
		String accessToken = request.getHeader("token");
		String member_id=tokenManager.getToken(accessToken);
		//如果登录成功 生成token
		tokenManager.deleteToken(member_id);
		redisUtils.del("permissions_"+member_id);
		redisUtils.del("adminAuth_"+member_id);
		return sendSuccessMessage("退出成功");
		//response.sendRedirect(ConfUtils.get("adminUrl") + "/toLogin.html");
	}

	@LoginRequired
	@RequestMapping(value="/chpwd",method={RequestMethod.POST,RequestMethod.GET})
	public ResultModel chpwd(@CurrentUser Operator oper, @RequestParam String newPassword, @RequestParam String oldPassword, HttpServletRequest request, HttpServletResponse response)throws Exception {
		if(!oper.getLoginPwd().equals(MethodUtil.MD5(oldPassword))){
			return sendFailureMessage("原密码不匹配");
		}
		if(newPassword!=null && !"".equals(newPassword)){
			oper.setLoginPwd(MethodUtil.MD5(newPassword));
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
	public ResultModel captcha(HttpServletResponse response) throws ServletException, IOException {
		// 生成文字验证码
		String text = captchaProducer.createText();
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


}
