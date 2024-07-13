package com.opensef.auth.spring.boot.web;

import com.opensef.auth.AuthUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;


public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthUtil.checkLogin();
        // 更新token和session的过期时间
        AuthUtil.updateTokenAndSessionTimeout();
        return true;
    }

}
