package com.opensef.auth.config;

public enum TokenStyle {

    /**
     * UUID
     */
    UUID,

    /**
     * UUID, 不包含分隔符
     */
    UUID_NO_DASH,

    /**
     * 32位随机字符串
     */
    RANDOM_32,

    /**
     * 64位随机字符串
     */
    RANDOM_64,

    /**
     * 128位随机字符串
     */
    RANDOM_128

}
