package com.opensef.auth.spring.boot.web;

public class AuthContextHolder {

    public static ThreadLocal<AuthContext> LOCAL = new InheritableThreadLocal<>();

    public static void put(AuthContext context) {
        LOCAL.set(context);
    }

    public static AuthContext get() {
        return LOCAL.get();
    }

    public static void clear() {
        LOCAL.remove();
    }

}
