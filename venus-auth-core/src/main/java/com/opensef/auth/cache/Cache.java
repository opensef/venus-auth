package com.opensef.auth.cache;

import com.opensef.auth.constant.AuthConstant;

public interface Cache<K, V> {

    /**
     * 获取value值
     *
     * @param key key值
     * @return value值
     */
    V get(K key);

    /**
     * 设置value值
     *
     * @param key   key值
     * @param value value值
     */
    void put(K key, V value);

    /**
     * 设置value值
     *
     * @param key     key值
     * @param value   value值
     * @param timeout 过期时间（毫秒）
     */
    void put(K key, V value, long timeout);

    /**
     * 删除value值
     *
     * @param key key值
     * @return 删除的value值
     */
    V remove(K key);

    /**
     * 设置缓存过期时间
     *
     * @param key     key值
     * @param timeout 过期时间（毫秒）
     */
    void expire(K key, long timeout);

    /**
     * 获取剩余过期时间
     *
     * @param key key值
     * @return 过期时间（毫秒） -1表示永久
     */
    Long getExpire(K key);

    /**
     * 是否有效<br/>
     * 根据当前key的剩余过期时间判断，当key已经过期时，默认剩余过期时间返回-2，当你使用的缓存返回其他值时，你需要实现Cache接口并重写这个方法
     *
     * @param key key值
     * @return true:有效，false:已过期
     */
    default boolean isUnExpired(K key) {
        Long expire = getExpire(key);
        return expire != null && (expire > AuthConstant.EXPIRED_DATA_NOT_EXIST);
    }

}
