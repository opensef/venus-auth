package com.opensef.auth.config;

public class AuthConfig {

    /**
     * 过期时间（秒），-1为用不过期
     */
    private Long timeout;

    /**
     * token风格，UUID、random32,random64、random128，默认为uuid
     * 当实现TokenHandler接口自定义实现时，此配置不生效
     */
    private TokenStyle tokenStyle;

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public TokenStyle getTokenStyle() {
        return tokenStyle;
    }

    public void setTokenStyle(TokenStyle tokenStyle) {
        this.tokenStyle = tokenStyle;
    }

}
