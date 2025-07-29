package com.xiao.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;


/**
 * Function: 基础控制器类
 *
 * date: 2016年8月29日 下午5:40:21 杭州萨莫网络科技有限公司 All Rights Reserved
 *
 * @author zhaozf
 */
public class BaseController {

	public final static String SUCCESS = "success";

	protected Logger log = LoggerFactory.getLogger(this.getClass());

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ModelAndView forword(String viewName, Map context) {
		return new ModelAndView(viewName, context);
	}

	public Map<String, Object> getRootMap() {
		Map<String, Object> rootMap = new HashMap<String, Object>();
		return rootMap;
	}

	/**
	 *
	 * 提示失败信息
	 *
	 * @param message
	 *
	 */
	public static ResultModel sendFailureMessage(String message) {
		return new ResultModel(false,message);
	}

	/**
	 *
	 * 提示成功信息
	 *
	 * @param message
	 * @return
	 *
	 */
	public static ResultModel sendSuccessMessage(String message) {
		return new ResultModel(true,message);
	}

}
