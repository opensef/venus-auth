package com.opensef.auth.annotation;

import java.lang.annotation.*;

/**
 * 校验角色
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckRole {

    String[] value();

    /**
     * value为多个值时的逻辑关系
     *
     * @return Logical
     */
    Logical logical() default Logical.AND;

}
