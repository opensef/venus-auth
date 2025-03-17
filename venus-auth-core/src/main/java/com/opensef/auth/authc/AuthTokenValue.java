package com.opensef.auth.authc;

import java.util.Map;

/**
 * Token对象（token缓存中的value值）
 */
public class AuthTokenValue {

    /**
     * 登录ID
     */
    private String loginId;

    /**
     * Token有效期（秒）
     */
    private Long timeout;

    /**
     * 创建时间
     */
    private Long createdTime;

    /**
     * Token过期时间（毫秒）
     */
    private Long expireTime;

    /**
     * 附加信息
     */
    private Map<String, Object> addInfo;

    public AuthTokenValue() {
    }

    public AuthTokenValue(String loginId, Long timeout, Long createdTime, Long expireTime, Map<String, Object> addInfo) {
        this.loginId = loginId;
        this.timeout = timeout;
        this.createdTime = createdTime;
        this.expireTime = expireTime;
        this.addInfo = addInfo;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public Map<String, Object> getAddInfo() {
        return addInfo;
    }

    public void setAddInfo(Map<String, Object> addInfo) {
        this.addInfo = addInfo;
    }

    @Override
    public String toString() {
        return "AuthTokenValue{" +
                "loginId='" + loginId + '\'' +
                ", timeout=" + timeout +
                ", createdTime=" + createdTime +
                ", expireTime=" + expireTime +
                ", addInfo=" + addInfo +
                '}';
    }

}
