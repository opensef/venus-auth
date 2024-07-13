package com.opensef.auth.authc;

import com.opensef.auth.config.TokenStyle;
import com.opensef.auth.util.AuthRandomUtil;

import java.util.UUID;

public class DefaultTokenHandler implements TokenHandler {

    private final TokenStyle tokenStyle;

    public DefaultTokenHandler(TokenStyle tokenStyle) {
        this.tokenStyle = tokenStyle;
    }

    @Override
    public String createToken() {
        switch (tokenStyle) {
            case UUID:
                return UUID.randomUUID().toString();
            case UUID_NO_DASH:
                return UUID.randomUUID().toString().replaceAll("-", "");
            case RANDOM_32:
                return AuthRandomUtil.randomAlphabetic(32);
            case RANDOM_64:
                return AuthRandomUtil.randomAlphabetic(64);
            case RANDOM_128:
                return AuthRandomUtil.randomAlphabetic(128);
            default:
                return UUID.randomUUID().toString();
        }
    }

}
