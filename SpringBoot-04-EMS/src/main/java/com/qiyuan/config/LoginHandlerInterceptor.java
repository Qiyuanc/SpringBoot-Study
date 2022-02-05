package com.qiyuan.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName LoginHandlerInterceptor
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/10/4 21:09
 * @Version 1.0
 **/
public class LoginHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 登录成功后应设置有用户的 Session
        Object loginUser = request.getSession().getAttribute("loginUser");
        // 找不到对应的 Session，说明没登录
        if(loginUser==null){
            request.setAttribute("msg","请先登录！");
            // 转发后，由配置的视图解析器跳转
            request.getRequestDispatcher("/index.html").forward(request,response);
            // 请求不放行
            return false;
        }else {
            // 登录成功，放行
            return true;
        }
    }
}
