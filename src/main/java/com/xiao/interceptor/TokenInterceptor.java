package com.xiao.interceptor;

import com.alibaba.fastjson.JSON;
import com.xiao.base.ResultModel;
import com.xiao.constans.Constans;
import com.xiao.constans.E;
import com.xiao.core.basic.operator.domain.Operator;
import com.xiao.core.basic.operator.service.OperatorService;
import com.xiao.core.basic.operator_log.service.OperatorLogService;
import com.xiao.logannotation.LoginRequired;
import com.xiao.tokenmagnager.TokenManager;
import com.xiao.util.RedisUtils;
import com.xiao.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @description:Token验证过滤器,判断是否已登录
 * @author:@裘晓伟
 */

@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {
    protected Logger log = LoggerFactory.getLogger(this.getClass());
    //private String regx = "'|;|)|--|+|.|select|update|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute";
    //private String regx = "(?:')|(?:\")|(?:;)|(?:\\+)|(?:\\))|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|" +
    //  		 "(\\b(select|update|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";
    @Autowired
    private OperatorService userService;

    @Autowired
    OperatorLogService operateLogService;

    @Autowired
    TokenManager tokenManager;
    @Resource
    RedisUtils redisUtils;
    @Value("${regx}")
    private String regx;

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //后端验证token
        boolean flag= hasLoginRequired(request,response, method);
        if(!flag){
            return false;
        }
        // //前台验证token
        // boolean webflag=hasWebLoginRequired( request,  response,  method);
        // if(!webflag){
        //     return false;
        // }

        Map parametersMap = request.getParameterMap();
        Iterator it = parametersMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String[] value = (String[]) entry.getValue();
            for (int i = 0; i < value.length; i++) {
                if (null != value[i] && !cleanSqlKeyWords(value[i].toLowerCase())) {
                //if (null != value[i] && value[i].matches(this.regx)) {
                	log.error("您输入的参数有非法字符，请输入正确的参数！");
                    flushApiResponseError(response, "您输入的参数有非法字符，请输入正确的参数！", E.ECODE5);
        	        return false;
                }
            }
        }
        return true;
    }

    /**
     * 后台登陆
     * @param request
     * @param response
     * @param method
     * @return
     */
    private boolean hasLoginRequired(HttpServletRequest request, HttpServletResponse response, Method method){
        // 判断接口是否需要登录
        LoginRequired methodAnnotation = method.getAnnotation(LoginRequired.class);
        // 有 @LoginRequired 注解，需要认证
        if (methodAnnotation != null) {
            String accessToken = request.getHeader("token");
            if (StringUtil.isEmpty(accessToken)) {
                // 判断req.parameter是否存在令牌信息，如果存在，则允许登录
                accessToken = request.getParameter(Constans.ACCESS_TOKEN);
                //return false;
            }
            if (StringUtil.isEmpty(accessToken)) {
                flushApiResponseError(response, "无此token，请重新登录", E.ECODE1);
                return false;
            }
            // 查询是否存在
            String member_id = tokenManager.getToken(accessToken);
            if (StringUtil.isEmpty(member_id)) {
                flushApiResponseError(response, "token已失效", E.ECODE2);
                return false;
            }
            //TODO 以后可以考虑从redis里面取用户
            Operator user = userService.queryById(member_id);
            if (user == null) {
                flushApiResponseError(response, "用户不存在，请重新登录", E.ECODE3);
                return false;
            }
            if (!StringUtil.isEmpty(methodAnnotation.remark())) {
                operateLogService.insertOperLog(methodAnnotation.remark(), user);
            }
            String url = request.getRequestURI().toString();
            if(redisUtils.hasKey("adminAuth_"+member_id)){
                List<String> list=(List<String>)redisUtils.get("adminAuth_"+member_id);
                String url2 = url.substring(0, url.lastIndexOf("/"));
                list.add("/admin/homePage/getAdminOper");
                if(!list.contains(url) && !list.contains(url2)){
                    flushApiResponseError(response, "用户无权限访问", E.ECODE3);
                    return false;
                }
            }
            // 当前登录用户@CurrentUser
            request.setAttribute(Constans.CURRENT_USER, user);
        }
        return true;
    }

    // /**
    //  * 前端登录
    //  * @param request
    //  * @param response
    //  * @param method
    //  * @return
    //  */
    // private boolean hasWebLoginRequired(HttpServletRequest request, HttpServletResponse response, Method method){
    //     // 判断接口是否需要登录
    //     WebLoginRequired methodAnnotation = method.getAnnotation(WebLoginRequired.class);
    //     // 有 @LoginRequired 注解，需要认证
    //     if (methodAnnotation != null) {
    //         String accessToken = request.getHeader("token");
    //         if (StringUtil.isEmpty(accessToken)) {
    //             // 判断req.parameter是否存在令牌信息，如果存在，则允许登录
    //             accessToken = request.getParameter(Constans.WEB_ACCESS_TOKEN);
    //             //return false;
    //         }
    //         if (StringUtil.isEmpty(accessToken)) {
    //             flushApiResponseError(response, "无此token，请重新登录", E.ECODE1);
    //             return false;
    //         }
    //         // 查询是否存在
    //         String member_id = tokenManager.getToken(accessToken).replace("web","");
    //         if (StringUtil.isEmpty(member_id)) {
    //             flushApiResponseError(response, "token已失效", E.ECODE2);
    //             return false;
    //         }
    //         //TODO 以后可以考虑从redis里面取用户
    //         Userinfo user = userinfoService.queryById(member_id);
    //         if (user == null) {
    //             flushApiResponseError(response, "用户不存在，请重新登录", E.ECODE3);
    //             return false;
    //         }
    //         if (!StringUtil.isEmpty(methodAnnotation.remark())) {
    //             UserinfoLog userinfoLog=new UserinfoLog();
    //             userinfoLog.setRemark(methodAnnotation.remark());
    //             userinfoLog.setUserName(user.getTel());
    //             userinfoLog.setLoginName(user.getUsername());
    //             userinfoLog.setUserinfoLogId(user.getUSERREGID());
    //             userinfoLogService.insert(userinfoLog);
    //         }
    //         // 当前登录用户@CurrentUser
    //         request.setAttribute(Constans.WEB_CURRENT_USER, user);
    //     }
    //     return true;
    // }

    public boolean cleanSqlKeyWords(String value) {
        String paramValue = value;
        String[] notAllowedKeyWords = this.regx.split("\\|");
        for (String keyword : notAllowedKeyWords) {
            if ((paramValue.length() > keyword.length() + 4) &&(paramValue.contains(" "+keyword) || paramValue.contains(keyword+" ") || paramValue.contains(" "+keyword+" "))) {
            	log.error("您输入的参数有非法字符，请输入正确的参数！"+paramValue+":"+keyword);
            	System.out.println(paramValue+":"+keyword);
                return false;
            }
        }
        return true;
    }
    /**
     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                           ModelAndView modelAndView) throws Exception {

    }

    /**
     * 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param e
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {

    }

    /**
     * 将某个对象转换成json格式并发送到客户端
     *
     * @param response
     * @throws Exception
     */
    private void flushApiResponseError(HttpServletResponse response, String msg, String code) {
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        try {
            ResultModel rm = new ResultModel();
            rm.setMsg(msg);
            rm.setErrcode(code);
            rm.setSuccess(Boolean.FALSE);
            response.getWriter().write(JSON.toJSONString(rm));
        } catch (IOException e) {
            log.error("response error", e);
        }
    }
}
