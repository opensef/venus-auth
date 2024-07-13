package com.opensef.auth.exception;

/**
 * 认证失败异常
 */
public class NoAuthenticationException extends AuthException {

    public NoAuthenticationException() {
    }

    public NoAuthenticationException(String message) {
        super(message);
    }

    public NoAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoAuthenticationException(Throwable cause) {
        super(cause);
    }

}
