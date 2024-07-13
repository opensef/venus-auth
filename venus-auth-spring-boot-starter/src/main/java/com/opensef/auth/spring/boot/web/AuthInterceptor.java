package com.opensef.auth.spring.boot.web;

import com.opensef.auth.AuthUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthUtil.checkLogin();
        // 更新token和session的过期时间
        AuthUtil.updateTokenAndSessionTimeout();
        return true;
    }

}
