package com.opensef.auth.authz.strategy;

import com.opensef.auth.manager.AuthManager;

import java.lang.reflect.Method;

/**
 * 注解鉴权策略接口
 */
@FunctionalInterface
public interface AuthStrategy {

    /**
     * AOP鉴权
     *
     * @param method 注解
     */
    void checkAuth(Method method, AuthManager authManager);

}