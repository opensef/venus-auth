package com.opensef.auth.spring.boot.autoconfigure;

import com.opensef.auth.AuthUtil;
import com.opensef.auth.authc.DefaultTokenHandler;
import com.opensef.auth.authc.TokenAnalysisHandler;
import com.opensef.auth.authc.TokenHandler;
import com.opensef.auth.authz.DefaultPermissionHandler;
import com.opensef.auth.authz.PermissionHandler;
import com.opensef.auth.authz.strategy.AuthStrategyFactory;
import com.opensef.auth.cache.Cache;
import com.opensef.auth.cache.MemoryCache;
import com.opensef.auth.config.AuthConfig;
import com.opensef.auth.manager.AuthManager;
import com.opensef.auth.spring.boot.aop.AuthAop;
import com.opensef.auth.spring.boot.handler.SpringTokenAnalysisHandler;
import com.opensef.auth.spring.boot.web.AuthApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
@EnableConfigurationProperties(AuthProperties.class)
public class AuthAutoConfiguration {

    @Autowired(required = false)
    private TokenHandler tokenHandler;

    @Autowired(required = false)
    private TokenAnalysisHandler tokenAnalysisHandler;

    @Autowired(required = false)
    private PermissionHandler permissionHandler;

    @Autowired(required = false)
    private Cache<?, ?> cache;

    @Autowired(required = false)
    private AuthProperties authProperties;

    private AuthManager authManager;

    @Bean
    public AuthApplicationContext venusAuthApplicationContext() {
        return new AuthApplicationContext();
    }

    public void init() {
        if (this.tokenHandler == null) {
            this.tokenHandler = new DefaultTokenHandler(authProperties.getTokenStyle());
        }
        if (tokenAnalysisHandler == null) {
            this.tokenAnalysisHandler = new SpringTokenAnalysisHandler(authProperties.getTokenName());
        }
        if (this.permissionHandler == null) {
            this.permissionHandler = new DefaultPermissionHandler();
        }
        if (this.cache == null) {
            this.cache = new MemoryCache<String, Objects>();
        }
    }

    @SuppressWarnings("unchecked")
    @Bean
    public AuthManager authManager() {
        init();

        AuthConfig authConfig = new AuthConfig();
        authConfig.setTimeout(authProperties.getTimeout());
        authConfig.setTokenStyle(authProperties.getTokenStyle());
        authConfig.setTokenKey(authProperties.getTokenKey());
        authConfig.setSessionKey(authProperties.getSessionKey());

        AuthManager authManager = new AuthManager();
        authManager.init(tokenHandler, tokenAnalysisHandler, permissionHandler, (Cache<Object, Object>) cache, authConfig);

        this.authManager = authManager;
        AuthUtil.setAuthManager(authManager);
        return authManager;
    }

    @Bean
    public AuthStrategyFactory authStrategyFactory() {
        return new AuthStrategyFactory();
    }

    @Bean
    public AuthAop authAop() {
        return new AuthAop(authStrategyFactory(), authManager);
    }

}
