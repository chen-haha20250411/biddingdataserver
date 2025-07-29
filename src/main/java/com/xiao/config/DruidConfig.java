package com.xiao.config;

import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Servlet;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DruidConfig {

 // 内置没有web.xml文件，使用 SpringBoot 注册 Servlet 方式配置
    @Bean
    public ServletRegistrationBean<Servlet> statViewServlet() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");

        // 这些参数可以在StatViewServlet 的父类 ResourceServlet 中找到
        Map<String, String> initParams = new HashMap<>();
        initParams.put("loginUsername", "admin"); //后台管理界面的登录账号
        initParams.put("loginPassword", "1qazxsw2"); //后台管理界面的登录密码

     	// 页面允许某用户可以访问
        // initParams.put("allow", "localhost")：表示只有本机可以访问
        
        // 第二参数为空或者为null时，表示允许所有访问
        //initParams.put("allow", "");
        
        // 页面拒绝xxx访问
        // initParams.put("xxx", "127.0.0.1")：表示禁止此ip访问

        // 设置初始化参数
        bean.setInitParameters(initParams);
        return bean;
    }
}
