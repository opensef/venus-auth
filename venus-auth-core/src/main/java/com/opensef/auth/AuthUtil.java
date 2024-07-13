package com.opensef.auth;

import com.opensef.auth.authc.AuthToken;
import com.opensef.auth.authc.AuthTokenValue;
import com.opensef.auth.manager.AuthManager;
import com.opensef.auth.session.AuthSession;

import java.util.List;
import java.util.Map;

public class AuthUtil {

    private static volatile AuthManager authManager;

    public static void setAuthManager(AuthManager authManager) {
        AuthUtil.authManager = authManager;
    }

    /**
     * 登录
     *
     * @param loginId 登录唯一标识
     * @return token信息
     */
    public static AuthToken login(String loginId) {
        return authManager.login(loginId);
    }

    /**
     * 登录
     *
     * @param loginId 登录唯一标识
     * @param addInfo 附加信息，附加信息，可将此信息存储到token缓存中
     * @return token信息
     */
    public static AuthToken login(String loginId, Map<String, Object> addInfo) {
        return authManager.login(loginId, addInfo);
    }

    /**
     * 退出登录<br/>
     * 自动从Header或URL中获取token值
     */
    public static void logout() {
        authManager.logout();
    }

    /**
     * 退出登录
     *
     * @param loginId 登录唯一标识
     */
    public static void logout(String loginId) {
        authManager.logout(loginId);
    }

    /**
     * 退出登录
     *
     * @param token token
     */
    public static void logoutByToken(String token) {
        authManager.logoutByToken(token);
    }

    /**
     * 获取当前登录的token值（登录）信息
     *
     * @return token值信息
     */
    public static AuthTokenValue getTokenValue() {
        return authManager.getTokenValue();
    }

    /**
     * 获取token值信息
     *
     * @param token token
     * @return token值信息
     */
    public static AuthTokenValue getTokenValue(String token) {
        return authManager.getTokenValue(token);
    }

    /**
     * 获取用户传入的token值
     *
     * @return token值
     */
    public static String getToken() {
        return authManager.getToken();
    }

    /**
     * 获取token剩余过期时间<br/>
     * 内存缓存和redis缓存时，数据不存在返回-2；永不过期返回-1；其他值表示剩余过期时间
     *
     * @param token token
     * @return token剩余过期时间（毫秒）
     */
    public static Long getTokenExpire(String token) {
        return authManager.getTokenExpire(token);
    }

    /**
     * 获取session信息
     *
     * @param loginId 登录唯一标识
     * @return session信息
     */
    public static AuthSession getSession(String loginId) {
        return authManager.getSession(loginId);
    }

    /**
     * 获取session信息
     *
     * @param token token
     * @return session信息
     */
    public static AuthSession getSessionByToken(String token) {
        return authManager.getSessionByToken(token);
    }

    /**
     * 将数据存放到session中
     *
     * @param data 数据
     */
    public static void setSessionData(String loginId, Object data) {
        authManager.setSessionData(loginId, data);
    }

    /**
     * 将数据存放到session中
     *
     * @param token token
     * @param data  数据
     */
    public static <T> void setSessionDataByToken(String token, T data) {
        authManager.setSessionDataByToken(token, data);
    }

    /**
     * 从session中获取数据
     */
    public static <T> T getSessionData() {
        return authManager.getSessionData();
    }

    /**
     * 更新token和session的过期时间
     *
     * @param token   token
     * @param loginId 登录唯一标识
     * @param timeout 过期时间（毫秒）
     */
    public static void updateTokenAndSessionTimeout(String token, String loginId, long timeout) {
        authManager.updateTokenAndSessionTimeout(token, loginId, timeout);
    }

    /**
     * 更新token和session的过期时间
     */
    public static void updateTokenAndSessionTimeout() {
        authManager.updateTokenAndSessionTimeout();
    }

    /**
     * 是否通过认证<br/>
     * 自动从Header或URL中获取token值，根据token做判断
     *
     * @return true/false
     */
    public static boolean isLogin() {
        return authManager.isLogin();
    }

    /**
     * 是否通过认证
     *
     * @param loginId 登录唯一标识
     * @return true/false
     */
    public static boolean isLogin(String loginId) {
        return authManager.isLogin(loginId);
    }

    /**
     * 是否通过认证
     *
     * @param token token
     * @return true/false
     */
    public static boolean isLoginByToken(String token) {
        return authManager.isLoginByToken(token);
    }

    /**
     * 是否有某个角色
     *
     * @param role 角色
     * @return true/false
     */
    public static boolean isHasRole(String role) {
        return authManager.isHasRole(role);
    }

    /**
     * 是否有输入的全部角色
     *
     * @param roles 角色集合
     * @return true/false
     */
    public static boolean isHasRoleAnd(List<String> roles) {
        return authManager.isHasRoleAnd(roles);
    }

    /**
     * 是否有输入的全部角色中的其中一个
     *
     * @param roles 角色集合
     * @return true/false
     */
    public static boolean isHasRoleOr(List<String> roles) {
        return authManager.isHasRoleOr(roles);
    }

    /**
     * 是否有某个权限
     *
     * @param permission 权限
     * @return true/false
     */
    public static boolean isHasPermission(String permission) {
        return authManager.isHasPermission(permission);
    }

    /**
     * 是否有输入的全部权限
     *
     * @param permissions 权限集合
     * @return true/false
     */
    public static boolean isHasPermissionAnd(List<String> permissions) {
        return authManager.isHasPermissionAnd(permissions);
    }

    /**
     * 是否有输入的全部权限的其中一个
     *
     * @param permissions 权限集合
     * @return true/false
     */
    public static boolean isHasPermissionOr(List<String> permissions) {
        return authManager.isHasPermissionOr(permissions);
    }

    /**
     * 登录检查，检查不通过抛出异常<br/>
     * 自动从Header或URL中获取token值，根据token做判断
     */
    public static void checkLogin() {
        authManager.checkLogin();
    }

    /**
     * 登录检查，检查不通过抛出异常<br/>
     *
     * @param loginId 登录唯一标识
     */
    public static void checkLogin(String loginId) {
        authManager.checkLogin(loginId);
    }

    /**
     * 登录检查，检查不通过抛出异常
     *
     * @param token token
     */
    public static void checkLoginByToken(String token) {
        authManager.checkLoginByToken(token);
    }

    /**
     * 角色检查，检查不通过抛出异常
     *
     * @param role 角色
     */
    public static void checkRole(String role) {
        authManager.checkRole(role);
    }

    /**
     * 角色检查，是否有输入的全部角色，检查不通过抛出异常
     *
     * @param roles 角色集合
     */
    public static void checkRoleAnd(List<String> roles) {
        authManager.checkRoleAnd(roles);
    }

    /**
     * 角色检查，是否有输入的角色中的其中一个角色，检查不通过抛出异常
     *
     * @param roles 角色集合
     */
    public static void checkRoleOr(List<String> roles) {
        authManager.checkRoleOr(roles);
    }

    /**
     * 权限检查，检查不通过抛出异常
     *
     * @param permission 权限
     */
    public static void checkPermission(String permission) {
        authManager.checkPermission(permission);
    }

    /**
     * 权限检查，是否有输入的全部权限，检查不通过抛出异常
     *
     * @param permissions 权限集合
     */
    public static void checkPermissionAnd(List<String> permissions) {
        authManager.checkPermissionAnd(permissions);
    }

    /**
     * 权限检查，是否有输入的权限中的其中一个权限，检查不通过抛出异常
     *
     * @param permissions 权限集合
     */
    public static void checkPermissionOr(List<String> permissions) {
        authManager.checkPermissionOr(permissions);
    }

}
