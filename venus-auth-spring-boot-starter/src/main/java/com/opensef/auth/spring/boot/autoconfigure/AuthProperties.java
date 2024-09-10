package com.opensef.auth.spring.boot.autoconfigure;

import com.opensef.auth.config.TokenStyle;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Objects;

@ConfigurationProperties(prefix = "auth")
public class AuthProperties {

    /**
     * 默认过期时间（秒），30分钟
     */
    public static final long DEFAULT_TIMEOUT = 1800L;

    /**
     * 默认token风格为UUID
     */
    public static final TokenStyle DEFAULT_TOKEN_STYLE = TokenStyle.UUID;

    /**
     * 默认token key
     */
    public static final String DEFAULT_TOKEN_KEY = "auth:token";

    /**
     * 默认session key
     */
    public static final String DEFAULT_SESSION_KEY = "auth:session";

    /**
     * token名称，系统根据此名称从请求头和URL中获取token值
     */
    private String tokenName;

    /**
     * 过期时间（秒），-1为用不过期
     */
    private Long timeout = DEFAULT_TIMEOUT;

    /**
     * token风格，UUID、random32,random64、random128，默认为uuid
     * 当实现TokenHandler接口自定义实现时，此配置不生效
     */
    private TokenStyle tokenStyle = DEFAULT_TOKEN_STYLE;

    /**
     * token key，默认为auth:token
     */
    private String tokenKey = DEFAULT_TOKEN_KEY;

    /**
     * session key，默认为auth:session
     */
    private String sessionKey = DEFAULT_SESSION_KEY;

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = Objects.requireNonNullElse(timeout, DEFAULT_TIMEOUT);
    }

    public TokenStyle getTokenStyle() {
        return tokenStyle;
    }

    public void setTokenStyle(TokenStyle tokenStyle) {
        this.tokenStyle = Objects.requireNonNullElse(tokenStyle, DEFAULT_TOKEN_STYLE);
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = Objects.requireNonNullElse(tokenKey, DEFAULT_TOKEN_KEY);
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = Objects.requireNonNullElse(sessionKey, DEFAULT_SESSION_KEY);
    }

}
