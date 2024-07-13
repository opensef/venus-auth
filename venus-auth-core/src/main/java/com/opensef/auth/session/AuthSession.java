package com.opensef.auth.session;

import java.util.List;

public class AuthSession {

    /**
     * sessionId
     */
    private String sessionId;

    /**
     * 创建时间（毫秒）
     */
    private Long createdTime;

    /**
     * 数据内容
     */
    private Object data;

    /**
     * 多个用户同时登录时，全部token值集合，按登录顺序存放
     */
    private List<String> tokenList;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public List<String> getTokenList() {
        return tokenList;
    }

    public void setTokenList(List<String> tokenList) {
        this.tokenList = tokenList;
    }

    @Override
    public String toString() {
        return "AuthSession{" +
                "sessionId='" + sessionId + '\'' +
                ", createdTime=" + createdTime +
                ", data=" + data +
                ", tokenList=" + tokenList +
                '}';
    }

}
