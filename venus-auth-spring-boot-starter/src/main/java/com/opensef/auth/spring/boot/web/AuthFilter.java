package com.opensef.auth.spring.boot.web;

import com.opensef.auth.AuthUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AuthFilter implements Filter {

    private final List<String> excludeUrlList = new ArrayList<>();

    public AuthFilter() {
    }

    public AuthFilter excludePath(String... url) {
        Collections.addAll(excludeUrlList, url);
        return this;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        // 将request、response加入线程变量中
        AuthContextHolder.put(new AuthContext(httpServletRequest, httpServletResponse));

        if (!isExcludeUrl(httpServletRequest.getRequestURI())) {
            if (!AuthUtil.isLogin()) {
                // 将错误信息交给spring处理
                httpServletResponse.sendError(401, "认证失败");
                // 使用上面一种方式即可，下面这种方式只能返回必须在@ControllerAdvice中处理，这里仅做记录
                // HandlerExceptionResolver handlerExceptionResolver = AuthApplicationContext.getBean("handlerExceptionResolver", HandlerExceptionResolver.class);
                // handlerExceptionResolver.resolveException(httpServletRequest, httpServletResponse, null, new NoAuthenticationException("认证失败"));
                return;
            }
            // 更新token和session的过期时间
            AuthUtil.updateTokenAndSessionTimeout();
        }

        chain.doFilter(request, response);
    }

    private boolean isExcludeUrl(String currentUri) {
        for (String url : excludeUrlList) {
            if (currentUri.startsWith(url)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
        // 清空线程变量
        AuthContextHolder.clear();
    }

}
