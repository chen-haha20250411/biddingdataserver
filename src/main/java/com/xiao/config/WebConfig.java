package com.xiao.config;

import com.xiao.interceptor.OperatorMethodArgumentResolver;
import com.xiao.interceptor.TokenInterceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {
	@Value("${cors.origins.domain}")
	private String corsOriginsDomain;
	
	@Value("${realPath}")
	private String UPLOAD_FOLDER;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins(corsOriginsDomain)
				//.allowedOrigins("http://218.108.205.49:7171","http://112.35.1.155:1992","http://zjyz.zjzwfw.gov.cn:9999","https://zjyz.zjzwfw.gov.cn:9999","https://112.35.10.201:28888","https://www.5050plan.com", "https://hhtz.5050plan.com","http://yz.hangzhou.gov.cn","https://yz.hangzhou.gov.cn","http://39.170.67.167")
				.allowedMethods("GET", "POST")
				.allowedHeaders("key", "method", "timestamp", "sign", "token", "Content-Type","x-requested-with")
				.allowCredentials(true)
				.maxAge(3600);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		registry.addResourceHandler("/static/**").addResourceLocations("classpath:static/").addResourceLocations("file:static/");
//		registry.addResourceHandler("/swagger-ui.html");
//		registry.addResourceHandler("/webjars/**");
		registry.addResourceHandler("/static/**").addResourceLocations("file:" + UPLOAD_FOLDER);
	}

	/**
	 * registry.addInterceptor可以通过此方法添加拦截器, 可以是spring提供的或者自己添加的
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//registry.addInterceptor(new AuthInterceptor()).addPathPatterns("/admin/**");
		//registry.addInterceptor(new AuthAfterInterceptor()).addPathPatterns("/admin/**");
		//registry.addInterceptor(new ErrorPageInterceptor());
		registry.addInterceptor(addTokenInterceptor()).addPathPatterns("/**");
		super.addInterceptors(registry);
	}

	/**
	 * 想要注入的拦截器中的接口不为null必须先定义@Bean否则将为空
	 * @return
	 */
	@Bean
	public TokenInterceptor addTokenInterceptor() {
		return new TokenInterceptor();
	}

	/***
	 * 将方法中的Memberinfo实例化
	 */
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(operatorMethodArgumentResolver());
		super.addArgumentResolvers(argumentResolvers);
	}

	@Bean
	public OperatorMethodArgumentResolver operatorMethodArgumentResolver() {
		return new OperatorMethodArgumentResolver();
	}
	
}
