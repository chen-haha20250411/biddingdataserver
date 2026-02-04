package com.xiao.aop;

import com.alibaba.fastjson.JSON;
import eu.bitwalker.useragentutils.UserAgent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

@Aspect
@Component
public class AopLog {

    protected Logger log = LoggerFactory.getLogger(AopLog.class);
    private static final String START_TIME = "request-start";
    /**
     * 切入点
     */
    @Pointcut("execution(public * com.xiao.core..*.*(..))")
    public void log() {

    }
//
//    /**
//     * 前置操作
//     *
//     * @param point 切入点
//     */
//    @Before("log()")
//    public void beforeLog(JoinPoint point) {
//
//    }

    /**
     * 环绕操作
     *
     * @param point 切入点
     * @return 原方法返回值
     * @throws Throwable 异常信息
     */
    //@Around("log()")
    public Object aroundLog(ProceedingJoinPoint point) throws Throwable {
        Object result =null;
        long beginTime = System.currentTimeMillis();
        try {
            result = point.proceed();
            long endTime = System.currentTimeMillis();
           // log.info("【返回值】：{}", JSON.toJSONString(result));
            insertLog(point,endTime-beginTime,JSON.toJSONString(result));
        } catch (Throwable e) {
            //e.printStackTrace();
            throw e;
        }
        return result;
    }

//    /**
//     * 后置操作
//     */
//    @AfterReturning("log()")
//    public void afterReturning() {
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
//
//        Long start = (Long) request.getAttribute(START_TIME);
//        Long end = System.currentTimeMillis();
//        log.info("【请求耗时】：{}毫秒", end - start);
//
//        String header = request.getHeader("User-Agent");
//        UserAgent userAgent = UserAgent.parseUserAgentString(header);
//        log.info("【浏览器类型】：{}，【操作系统】：{}，【原始User-Agent】：{}", userAgent.getBrowser().toString(), userAgent.getOperatingSystem().toString(), header);
//    }


    private void insertLog(ProceedingJoinPoint point ,long time,String result) {
        MethodSignature signature = (MethodSignature)point.getSignature();
        Method method = signature.getMethod();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String userAestr="";
        String accessToken ="";
        Map<String, String[]> parameterMap =null;
        String url="";
        String remote="";
        if(attributes!= null){
	        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
	        String header = request.getHeader("User-Agent");
	        UserAgent userAgent = UserAgent.parseUserAgentString(header);
	       // log.info("【浏览器类型】：{}，【操作系统】：{}，【原始User-Agent】：{}", userAgent.getBrowser().toString(), userAgent.getOperatingSystem().toString(), header);
            userAestr="【浏览器类型】："+userAgent.getBrowser().toString()+"，【操作系统】："+userAgent.getOperatingSystem().toString();//+"，【原始User-Agent】："+header;
            // 请求的方法参数值
            parameterMap = request.getParameterMap();
            //从session中获取当前登陆人id
            accessToken = request.getHeader("token");

            url=request.getRequestURL().toString();

            remote=request.getRemoteAddr();
        }
        // 请求的类名
        String className = point.getTarget().getClass().getName();
        // 请求的方法名
        String methodName = signature.getName();

        log.info("当前token:{},请求 IP:{},请求 URL:{},类名:{},方法名:{},参数：{},执行时间：{},浏览器参数：{},返回值：{}",accessToken,url,remote, className, methodName, JSON.toJSONString(parameterMap), time,userAestr,result);

    }

}
