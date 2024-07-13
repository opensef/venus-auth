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
     * 创建时间
     */
    private Long createdTime;

    /**
     * 附加信息
     */
    private Map<String, Object> addInfo;

    public AuthTokenValue() {
    }

    public AuthTokenValue(String loginId, Long createdTime, Map<String, Object> addInfo) {
        this.loginId = loginId;
        this.createdTime = createdTime;
        this.addInfo = addInfo;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
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
                "loginId=" + loginId +
                ", createdTime=" + createdTime +
                ", addInfo=" + addInfo +
                '}';
    }

}
