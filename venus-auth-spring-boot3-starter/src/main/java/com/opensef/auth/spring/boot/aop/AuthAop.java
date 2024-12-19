package com.opensef.auth.spring.boot.aop;

import com.opensef.auth.authz.strategy.AuthStrategy;
import com.opensef.auth.authz.strategy.AuthStrategyFactory;
import com.opensef.auth.manager.AuthManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Order(9999)
@Aspect
public class AuthAop {

    private final AuthStrategyFactory authStrategyFactory;

    private final AuthManager authManager;

    public AuthAop(AuthStrategyFactory authStrategyFactory, AuthManager authManager) {
        this.authStrategyFactory = authStrategyFactory;
        this.authManager = authManager;
    }


    @Around("@within(com.opensef.auth.annotation.CheckLogin) || @annotation(com.opensef.auth.annotation.CheckLogin) ||" +
            "@within(com.opensef.auth.annotation.CheckRole) || @annotation(com.opensef.auth.annotation.CheckRole) ||" +
            "@within(com.opensef.auth.annotation.CheckPermission) || @annotation(com.opensef.auth.annotation.CheckPermission)"
    )
    public Object auth(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Method method = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();
        // 先扫描类上的注解
        for (Annotation annotation : method.getDeclaringClass().getAnnotations()) {
            AuthStrategy authStrategy = authStrategyFactory.get(annotation);
            if (null != authStrategy) {
                authStrategy.checkAuth(annotation, authManager);
            }
        }
        // 再扫描方法上的注解
        for (Annotation annotation : method.getAnnotations()) {
            AuthStrategy authStrategy = authStrategyFactory.get(annotation);
            if (null != authStrategy) {
                authStrategy.checkAuth(annotation, authManager);
            }
        }
        return proceedingJoinPoint.proceed();
    }

}
