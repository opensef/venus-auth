package com.opensef.auth.manager;

import com.opensef.auth.authc.AuthToken;
import com.opensef.auth.authc.AuthTokenValue;
import com.opensef.auth.authc.TokenAnalysisHandler;
import com.opensef.auth.authc.TokenHandler;
import com.opensef.auth.authz.PermissionHandler;
import com.opensef.auth.cache.Cache;
import com.opensef.auth.config.AuthConfig;
import com.opensef.auth.constant.AuthConstant;
import com.opensef.auth.exception.AuthException;
import com.opensef.auth.exception.NoAuthenticationException;
import com.opensef.auth.exception.NoPermissionException;
import com.opensef.auth.session.AuthSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AuthManager {

    private static TokenHandler tokenHandler;

    private static TokenAnalysisHandler tokenAnalysisHandler;

    private static PermissionHandler permissionHandler;

    private static Cache<Object, Object> cache;

    private static AuthConfig authConfig;

    public void init(TokenHandler tokenHandler, TokenAnalysisHandler tokenAnalysisHandler, PermissionHandler permissionHandler,
                     Cache<Object, Object> cache, AuthConfig authConfig) {
        AuthManager.tokenHandler = tokenHandler;
        AuthManager.tokenAnalysisHandler = tokenAnalysisHandler;
        AuthManager.permissionHandler = permissionHandler;
        AuthManager.cache = cache;
        AuthManager.authConfig = authConfig;
    }

    /**
     * 登录
     *
     * @param loginId 登录唯一标识
     * @return token信息
     */
    public AuthToken login(String loginId) {
        return login(loginId, null, null);
    }

    /**
     * 登录
     *
     * @param loginId 登录唯一标识
     * @param addInfo 附加信息，可将此信息存储到token缓存中
     * @return token信息
     */
    public AuthToken login(String loginId, Map<String, Object> addInfo, Long expireTime) {
        long createdTime = System.currentTimeMillis();
        if (null == expireTime) {
            expireTime = expireTime(authConfig.getTimeout());
        } else {
            expireTime = expireTime(expireTime);
        }

        // 创建token
        String token = tokenHandler.createToken();

        // 保存token
        cache.put(genTokenKey(token), new AuthTokenValue(loginId, createdTime, addInfo), expireTime);

        AuthToken authToken = new AuthToken(token, createdTime, addInfo);

        // 根据loginId获取session，如果存在则更新，不存在则创建
        AuthSession authSession = getSession(loginId);
        if (null == authSession) {
            authSession = new AuthSession();
            authSession.setSessionId(genSessionKey(loginId));
            authSession.setCreatedTime(createdTime);

            ArrayList<String> tokenList = new ArrayList<>();
            tokenList.add(token);
            authSession.setTokenList(tokenList);
        } else {
            authSession.getTokenList().add(token);
        }

        cache.put(authSession.getSessionId(), authSession, expireTime);

        return authToken;
    }

    /**
     * 退出登录<br/>
     * 自动从Header或URL中获取token值
     */
    public void logout() {
        logoutByToken(tokenAnalysisHandler.getToken());
    }

    /**
     * 根据登录唯一标识退出登录，该登录唯一标识对应的全部token均退出
     *
     * @param loginId 登录唯一标识
     */
    public void logout(String loginId) {
        AuthSession authSession = getSession(loginId);
        if (authSession == null) {
            return;
        }

        // 删除session
        cache.remove(genSessionKey(authSession.getSessionId()));

        if (authSession.getTokenList() != null && !authSession.getTokenList().isEmpty()) {
            // 删除token
            for (String token : authSession.getTokenList()) {
                cache.remove(genTokenKey(token));
            }
        }
    }

    /**
     * 退出登录
     *
     * @param token token
     */
    public void logoutByToken(String token) {
        AuthTokenValue tokenValue = getTokenValue(token);
        if (null == tokenValue) {
            return;
        }

        // 删除token
        cache.remove(genTokenKey(token));

        // 获取session
        AuthSession authSession = getSession(tokenValue.getLoginId());
        if (authSession == null) {
            return;
        }

        // 判断session是否存在多个token，如果存在多个，删除token列表中对应的值，更新session；如果只有一个token，则删除session
        if ((authSession.getTokenList() == null || authSession.getTokenList().isEmpty()) || authSession.getTokenList().size() == 1) {
            cache.remove(authSession.getSessionId());
        } else {
            authSession.getTokenList().remove(token);
            long expire = cache.getExpire(authSession.getSessionId());
            cache.put(authSession.getSessionId(), authSession, expire);
        }
    }

    /**
     * 获取用户传入的token值
     *
     * @return token值
     */
    public String getToken() {
        return tokenAnalysisHandler.getToken();
    }

    /**
     * 获取当前登录的token值（登录）信息
     *
     * @return token值信息
     */
    public AuthTokenValue getTokenValue() {
        return (AuthTokenValue) cache.get(genTokenKey(tokenAnalysisHandler.getToken()));
    }

    /**
     * 获取token值信息
     *
     * @param token token
     * @return token值信息
     */
    public AuthTokenValue getTokenValue(String token) {
        return (AuthTokenValue) cache.get(genTokenKey(token));
    }

    /**
     * 获取token剩余过期时间<br/>
     * 内存缓存和redis缓存时，数据不存在返回-2；永不过期返回-1；其他值表示剩余过期时间<br>
     *
     * @param token token
     * @return token过期时间（毫秒）
     */
    public Long getTokenExpire(String token) {
        return cache.getExpire(genTokenKey(token));
    }

    /**
     * 获取session信息
     *
     * @param loginId 登录唯一标识
     * @return session信息
     */
    public AuthSession getSession(String loginId) {
        return (AuthSession) cache.get(genSessionKey(loginId));
    }

    /**
     * 获取session信息
     *
     * @param token token
     * @return session信息
     */
    public AuthSession getSessionByToken(String token) {
        if (token == null) {
            throw new AuthException("token不能为空");
        }
        AuthTokenValue authTokenValue = (AuthTokenValue) cache.get(genTokenKey(token));
        if (null != authTokenValue) {
            return getSession(String.valueOf(authTokenValue.getLoginId()));
        } else {
            return null;
        }
    }

    /**
     * 将数据存放到session中，可以设置多个key及对应的数据
     *
     * @param data 数据
     */
    public void setSessionData(String loginId, Object data) {
        AuthSession authSession = getSession(loginId);
        if (null == authSession) {
            return;
        }

        Long expire = cache.getExpire(authSession.getSessionId());

        authSession.setData(data);
        cache.put(authSession.getSessionId(), authSession, expire);
    }

    /**
     * 将数据存放到session中，可以设置多个key及对应的数据
     *
     * @param token token
     * @param data  数据
     */
    public <T> void setSessionDataByToken(String token, T data) {
        AuthSession authSession = getSessionByToken(token);
        if (null == authSession) {
            return;
        }

        Long expire = cache.getExpire(authSession.getSessionId());

        authSession.setData(data);
        cache.put(authSession.getSessionId(), authSession, expire);
    }

    /**
     * 从session中获取数据
     */
    @SuppressWarnings("unchecked")
    public <T> T getSessionData() {
        AuthSession authSession = getSessionByToken(tokenAnalysisHandler.getToken());
        if (null == authSession) {
            return null;
        }

        return (T) authSession.getData();
    }

    /**
     * 更新token和session的过期时间
     *
     * @param token   token
     * @param loginId 登录唯一标识
     * @param timeout 过期时间（毫秒）
     */
    public void updateTokenAndSessionTimeout(String token, String loginId, long timeout) {
        cache.expire(genTokenKey(token), timeout);
        cache.expire(genSessionKey(loginId), timeout);
    }

    /**
     * 更新token和session的过期时间
     */
    public void updateTokenAndSessionTimeout() {
        if (authConfig.getTimeout() == -1) {
            return;
        }
        String token = tokenAnalysisHandler.getToken();
        AuthTokenValue tokenValue = getTokenValue(token);
        updateTokenAndSessionTimeout(token, tokenValue.getLoginId(), expireTime(authConfig.getTimeout()));
    }

    /**
     * 是否通过认证<br/>
     * 自动从Header或URL中获取token值，根据token做判断
     *
     * @return true/false
     */
    public boolean isLogin() {
        return isLoginByToken(tokenAnalysisHandler.getToken());
    }

    /**
     * 是否通过认证
     *
     * @param loginId 登录唯一标识
     * @return true/false
     */
    public boolean isLogin(String loginId) {
        return cache.isUnExpired(genSessionKey(loginId));
    }

    /**
     * 是否通过认证
     *
     * @param token token
     * @return true/false
     */
    public boolean isLoginByToken(String token) {
        return cache.isUnExpired(genTokenKey(token));
    }

    /**
     * 是否有某个角色
     *
     * @param role 角色
     * @return true/false
     */
    public boolean isHasRole(String role) {
        AuthTokenValue tokenValue = getTokenValue();
        return isHasRole(tokenValue.getLoginId(), role);
    }

    /**
     * 是否有某个角色
     *
     * @param loginId 登录唯一标识
     * @param role    角色
     * @return true/false
     */
    public boolean isHasRole(String loginId, String role) {
        List<String> roles = permissionHandler.getRoles(loginId);
        return roles != null && roles.contains(role);
    }

    /**
     * 是否有输入的全部角色
     *
     * @param roles 角色集合
     * @return true/false
     */
    public boolean isHasRoleAnd(List<String> roles) {
        AuthTokenValue tokenValue = getTokenValue();
        return isHasRoleAnd(tokenValue.getLoginId(), roles);
    }

    /**
     * 是否有输入的全部角色
     *
     * @param loginId 登录唯一标识
     * @param roles   角色集合
     * @return true/false
     */
    public boolean isHasRoleAnd(String loginId, List<String> roles) {
        List<String> ownedRoles = permissionHandler.getRoles(loginId);
        if (ownedRoles == null) {
            return false;
        }
        for (String role : roles) {
            if (!ownedRoles.contains(role)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否有输入的全部角色中的其中一个
     *
     * @param roles 角色集合
     * @return true/false
     */
    public boolean isHasRoleOr(List<String> roles) {
        AuthTokenValue tokenValue = getTokenValue();
        return isHasRoleOr(tokenValue.getLoginId(), roles);
    }

    /**
     * 是否有输入的全部角色中的其中一个
     *
     * @param loginId 登录唯一标识
     * @param roles   角色集合
     * @return true/false
     */
    public boolean isHasRoleOr(String loginId, List<String> roles) {
        List<String> ownedRoles = permissionHandler.getRoles(loginId);
        if (ownedRoles == null) {
            return false;
        }
        for (String role : roles) {
            if (ownedRoles.contains(role)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否有某个权限
     *
     * @param permission 权限
     * @return true/false
     */
    public boolean isHasPermission(String permission) {
        AuthTokenValue tokenValue = getTokenValue();
        return isHasPermission(tokenValue.getLoginId(), permission);
    }

    /**
     * 是否有某个权限
     *
     * @param loginId    登录唯一标识
     * @param permission 权限
     * @return true/false
     */
    public boolean isHasPermission(String loginId, String permission) {
        List<String> permissions = permissionHandler.getPermissions(loginId);
        return permissions != null && permissions.contains(permission);
    }

    /**
     * 是否有输入的全部权限
     *
     * @param permissions 权限集合
     * @return true/false
     */
    public boolean isHasPermissionAnd(List<String> permissions) {
        AuthTokenValue tokenValue = getTokenValue();
        return isHasPermissionAnd(tokenValue.getLoginId(), permissions);
    }

    /**
     * 是否有输入的全部权限
     *
     * @param loginId     登录唯一标识
     * @param permissions 权限集合
     * @return true/false
     */
    public boolean isHasPermissionAnd(String loginId, List<String> permissions) {
        List<String> ownedPermissions = permissionHandler.getPermissions(loginId);
        if (ownedPermissions == null) {
            return false;
        }
        for (String permission : permissions) {
            if (!ownedPermissions.contains(permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否有输入的全部权限的其中一个
     *
     * @param permissions 权限集合
     * @return true/false
     */
    public boolean isHasPermissionOr(List<String> permissions) {
        AuthTokenValue tokenValue = getTokenValue();
        return isHasPermissionOr(tokenValue.getLoginId(), permissions);
    }

    /**
     * 是否有输入的全部权限的其中一个
     *
     * @param loginId     登录唯一标识
     * @param permissions 权限集合
     * @return true/false
     */
    public boolean isHasPermissionOr(String loginId, List<String> permissions) {
        List<String> ownedPermissions = permissionHandler.getPermissions(loginId);
        if (ownedPermissions == null) {
            return false;
        }
        for (String permission : permissions) {
            if (ownedPermissions.contains(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 登录检查，检查不通过抛出异常<br/>
     * 自动从Header或URL中获取token值，根据token做判断
     */
    public void checkLogin() {
        checkLoginByToken(tokenAnalysisHandler.getToken());
    }

    /**
     * 登录检查，检查不通过抛出异常<br/>
     *
     * @param loginId 登录唯一标识
     */
    public void checkLogin(String loginId) {
        if (!isLogin(loginId)) {
            throw new NoAuthenticationException("认证失败");
        }
    }

    /**
     * 登录检查，检查不通过抛出异常
     *
     * @param token token
     */
    public void checkLoginByToken(String token) {
        if (!isLoginByToken(token)) {
            throw new NoAuthenticationException("认证失败");
        }
    }

    /**
     * 角色检查，检查不通过抛出异常
     *
     * @param role 角色
     */
    public void checkRole(String role) {
        if (!isHasRole(role)) {
            throw new NoPermissionException("权限不足");
        }
    }

    /**
     * 角色检查，是否有输入的全部角色，检查不通过抛出异常
     *
     * @param roles 角色集合
     */
    public void checkRoleAnd(List<String> roles) {
        if (!isHasRoleAnd(roles)) {
            throw new NoPermissionException("权限不足");
        }
    }

    /**
     * 角色检查，是否有输入的角色中的其中一个角色，检查不通过抛出异常
     *
     * @param roles 角色集合
     */
    public void checkRoleOr(List<String> roles) {
        if (!isHasRoleOr(roles)) {
            throw new NoPermissionException("权限不足");
        }
    }

    /**
     * 权限检查，检查不通过抛出异常
     *
     * @param permission 权限
     */
    public void checkPermission(String permission) {
        if (!isHasPermission(permission)) {
            throw new NoPermissionException("权限不足");
        }
    }

    /**
     * 权限检查，是否有输入的全部权限，检查不通过抛出异常
     *
     * @param permissions 权限集合
     */
    public void checkPermissionAnd(List<String> permissions) {
        if (!isHasPermissionAnd(permissions)) {
            throw new NoPermissionException("权限不足");
        }
    }

    /**
     * 权限检查，是否有输入的权限中的其中一个权限，检查不通过抛出异常
     *
     * @param permissions 权限集合
     */
    public void checkPermissionOr(List<String> permissions) {
        if (!isHasPermissionOr(permissions)) {
            throw new NoPermissionException("权限不足");
        }
    }


    /**
     * 生成token的key
     *
     * @param token token
     * @return token的key
     */
    public String genTokenKey(String token) {
        return authConfig.getTokenKey() + ":" + token;
    }

    /**
     * 生成session的key
     *
     * @param loginId 登录唯一标识
     * @return session的key
     */
    public String genSessionKey(String loginId) {
        return authConfig.getSessionKey() + ":" + loginId;
    }

    /**
     * 计算到期时间戳，timeout为-1时不做处理，其他值时，将秒转换为毫秒
     *
     * @param timeout 过期时间（秒）
     * @return 到期时间戳
     */
    public long expireTime(long timeout) {
        if (timeout == AuthConstant.NEVER_EXPIRE) {
            return timeout;
        }
        return timeout * 1000;
    }

}
