package com.opensef.auth.authz.strategy;

import com.opensef.auth.annotation.CheckLogin;
import com.opensef.auth.annotation.CheckPermission;
import com.opensef.auth.annotation.CheckRole;
import com.opensef.auth.annotation.Logical;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AuthStrategyFactory {

    public static final Map<Class<?>, AuthStrategy> STRATEGY_MAP = new HashMap<>();


    public AuthStrategy get(Annotation annotation) {
        return STRATEGY_MAP.get(annotation.annotationType());
    }

    private static final AuthStrategy checkLogin = (method, authManager) -> {
        authManager.checkLogin();
    };
    private static final AuthStrategy checkRole = (method, authManager) -> {
        CheckRole annotation = method.getAnnotation(CheckRole.class);
        if (Logical.AND.equals(annotation.logical())) {
            authManager.checkRoleAnd(Arrays.asList(annotation.value()));
        } else {
            authManager.checkRoleOr(Arrays.asList(annotation.value()));
        }
    };

    private static final AuthStrategy checkPermission = (method, authManager) -> {
        CheckPermission annotation = method.getAnnotation(CheckPermission.class);
        if (Logical.AND.equals(annotation.logical())) {
            authManager.checkPermissionAnd(Arrays.asList(annotation.value()));
        } else {
            authManager.checkPermissionOr(Arrays.asList(annotation.value()));
        }
    };

    static {
        STRATEGY_MAP.put(CheckLogin.class, checkLogin);
        STRATEGY_MAP.put(CheckRole.class, checkRole);
        STRATEGY_MAP.put(CheckPermission.class, checkPermission);
    }

}
