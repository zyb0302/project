package com.school.manager.common;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 过滤器，拦截所有请求。
 * 仅当session中包含登录成功后的登录名后才放行请求
 */
public class CommonFilter implements HandlerInterceptor {

    /**
     * 进入controller层之前拦截请求
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        System.out.println("preHandle");
        HttpSession session = httpServletRequest.getSession();
        String username = (String)session.getAttribute("username");
        if (StringUtils.isNotBlank(username)) {
            return true;
        }else {
            httpServletResponse.sendRedirect("/login");
        }
        return true;
    }
    //访问controller之后 访问视图之前被调用
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle");
    }
    //访问视图之后被调用
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }

}
