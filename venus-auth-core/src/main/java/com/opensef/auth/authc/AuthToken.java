package com.opensef.auth.authc;

import java.util.Map;

/**
 * 认证通过后的Token对象
 */
public class AuthToken {

    /**
     * token值
     */
    private final String token;

    /**
     * 创建时间戳（毫秒）
     */
    private final Long createdTime;

    /**
     * 登录附加信息
     */
    private final Map<String, Object> addInfo;

    public AuthToken(String token, Long createdTime, Map<String, Object> addInfo) {
        this.token = token;
        this.createdTime = createdTime;
        this.addInfo = addInfo;
    }

    public String getToken() {
        return token;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    @Override
    public String toString() {
        return "AuthToken{" +
                "token='" + token + '\'' +
                ", createdTime=" + createdTime +
                ", addInfo=" + addInfo +
                '}';
    }

}
