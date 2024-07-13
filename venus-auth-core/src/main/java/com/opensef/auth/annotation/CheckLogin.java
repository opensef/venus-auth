package com.opensef.auth.annotation;

import java.lang.annotation.*;

/**
 * 校验登录认证
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckLogin {

}
