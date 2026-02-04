package com.xiao.util;


import com.xiao.core.basic.operator.domain.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
* 说明：回话管理工具类
* @author  赵增丰
* @version  1.0
* 2014-8-18 上午10:14:09
*/
public final class SessionUtils {

	protected static final Logger logger = LoggerFactory.getLogger(SessionUtils.class);

	private static final String SESSION_USER = "session_user";//后台用户

	private static final String SESSION_VALIDATECODE = "session_validatecode";//验证码

	private static final String SESSION_ACCESS_URLS = "session_access_urls"; //系统能够访问的URL

	private static final String SESSION_AUTHION_BTNS = "session_authion_btns"; //系统能够访问的页面菜单

	private static final String SESSION_MEMBER = "session_member"; //前台用户

	private static final String SESSION_SHOW_MENU = "session_show_menu"; //前台用户


	/**
	  * 设置session的值
	  * @param request
	  * @param key
	  * @param value
	  */
	 public static void setAttr(HttpServletRequest request,String key,Object value){
		 request.getSession(true).setAttribute(key, value);
	 }


	 /**
	  * 获取session的值
	  * @param request
	  * @param key
	  * @param value
	  */
	 public static Object getAttr(HttpServletRequest request,String key){
		 return request.getSession(true).getAttribute(key);
	 }

	 /**
	  * 删除Session值
	  * @param request
	  * @param key
	  */
	 public static void removeAttr(HttpServletRequest request,String key){
		 request.getSession(true).removeAttribute(key);
	 }

	 /**
	  * 设置用户信息 到session
	  * @param request
	  * @param user
	  */
	 public static void setUser(HttpServletRequest request,Operator operator){
		 request.getSession(true).setAttribute(SESSION_USER, operator);
	 }


	 /**
	  * 从session中获取用户信息
	  * @param request
	  * @return SysUser
	  */
	 public static Operator getUser(HttpServletRequest request){
		return (Operator)request.getSession(true).getAttribute(SESSION_USER);
	 }


	 /**
	  * 从session中获取用户信息
	  * @param request
	  * @return SysUser
	  */
	 public static void removeUser(HttpServletRequest request){
		removeAttr(request, SESSION_USER);
	 }

	 /**
	  * 从session中获取用户信息
	  * @param request
	  * @return SysUser
	  */
	 public static void removeMember(HttpServletRequest request){
		 removeAttr(request, SESSION_MEMBER);
	 }


	 /**
	  * 设置验证码 到session
	  * @param request
	  * @param user
	  */
	 public static void setValidateCode(HttpServletRequest request,String validateCode){
		 request.getSession(true).setAttribute(SESSION_VALIDATECODE, validateCode);
	 }


	 /**
	  * 从session中获取验证码
	  * @param request
	  * @return SysUser
	  */
	 public static String getValidateCode(HttpServletRequest request){
		return (String)request.getSession(true).getAttribute(SESSION_VALIDATECODE);
	 }


	 /**
	  * 从session中获删除验证码
	  * @param request
	  * @return SysUser
	  */
	 public static void removeValidateCode(HttpServletRequest request,String state){
		 removeAttr(request, SESSION_VALIDATECODE);
	 }


	 /**
	  * 判断当前登录用户是否超级管理员
	  * @param request
	  * @return
	  */
	 public static void setAccessUrl(HttpServletRequest request,List<String> accessUrls){ //判断登录用户是否超级管理员
		 setAttr(request, SESSION_ACCESS_URLS, accessUrls);
	 }



	 /**
	  * 判断URL是否可访问
	  * @param request
	  * @return
	  */
	@SuppressWarnings("unchecked")
	public static boolean isAccessUrl(HttpServletRequest request,String url){
		List<String> accessUrls = (List<String>)getAttr(request, SESSION_ACCESS_URLS);
		 if(accessUrls == null ||accessUrls.isEmpty() || !accessUrls.contains(url)){
			 return false;
		 }
		 return true;
	 }

	 /**
	  * 判断当前登录用户是否超级管理员
	  * @param request
	  * @return
	  */
	public static void setAuthionBtns(HttpServletRequest request,Map<String, BtnType> authionBtns){ //菜单权限放入
		 setAttr(request, SESSION_AUTHION_BTNS, authionBtns);
	 }

	 /**
	  * 判断当前登录用户是否超级管理员
	  * @param request
	  * @return
	  */
	@SuppressWarnings("unchecked")
	public static Map<String, BtnType>  getAuthionBtns(HttpServletRequest request){ //菜单权限拿出
		 return (Map<String, BtnType>)request.getSession(true).getAttribute(SESSION_AUTHION_BTNS);
	 }


}
