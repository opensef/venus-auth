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

    private static final AuthStrategy checkLogin = (annotation, authManager) -> {
        authManager.checkLogin();
    };
    private static final AuthStrategy checkRole = (annotation, authManager) -> {
        CheckRole checkRoleAnnotation = (CheckRole) annotation;
        if (Logical.AND.equals(checkRoleAnnotation.logical())) {
            authManager.checkRoleAnd(Arrays.asList(checkRoleAnnotation.value()));
        } else {
            authManager.checkRoleOr(Arrays.asList(checkRoleAnnotation.value()));
        }
    };

    private static final AuthStrategy checkPermission = (annotation, authManager) -> {
        CheckPermission checkPermissionAnnotation = (CheckPermission) annotation;
        if (Logical.AND.equals(checkPermissionAnnotation.logical())) {
            authManager.checkPermissionAnd(Arrays.asList(checkPermissionAnnotation.value()));
        } else {
            authManager.checkPermissionOr(Arrays.asList(checkPermissionAnnotation.value()));
        }
    };

    static {
        STRATEGY_MAP.put(CheckLogin.class, checkLogin);
        STRATEGY_MAP.put(CheckRole.class, checkRole);
        STRATEGY_MAP.put(CheckPermission.class, checkPermission);
    }

}
