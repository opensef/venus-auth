package com.opensef.auth.spring.boot.handler;

import com.opensef.auth.authc.TokenAnalysisHandler;
import com.opensef.auth.spring.boot.web.AuthApplicationContext;
import com.opensef.auth.spring.boot.web.AuthContextHolder;

import javax.servlet.http.HttpServletRequest;

public class SpringTokenAnalysisHandler implements TokenAnalysisHandler {

    private final String tokenName;

    public SpringTokenAnalysisHandler(String tokenName) {
        this.tokenName = tokenName;
    }

    @Override
    public String getToken() {
        HttpServletRequest httpServletRequest = AuthApplicationContext.getHttpServletRequest();
        if (httpServletRequest == null) {
            httpServletRequest = AuthContextHolder.get().getRequest();
        }
        String token = httpServletRequest.getHeader(tokenName);
        if (token == null || token.isBlank()) {
            token = httpServletRequest.getParameter(tokenName);
        }
        return token;
    }

}
