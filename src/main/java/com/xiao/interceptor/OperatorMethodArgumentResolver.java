package com.xiao.interceptor;

import com.xiao.constans.Constans;
import com.xiao.core.basic.operator.domain.Operator;
import com.xiao.logannotation.CurrentUser;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

/**
 * @description:自定义解析器实现参数绑定
 * 增加方法注入，将含有 @CurrentMember 注解的方法参数注入当前登录用户
 * @author:裘晓伟
 */
public class OperatorMethodArgumentResolver implements HandlerMethodArgumentResolver {
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(CurrentUser.class)
				&&parameter.getParameterType().isAssignableFrom(Operator.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		Operator user = (Operator) webRequest.getAttribute(Constans.CURRENT_USER,RequestAttributes.SCOPE_REQUEST);
		if (user != null) {
			return user;
		}
		throw new MissingServletRequestPartException(Constans.CURRENT_USER);
	}
}
