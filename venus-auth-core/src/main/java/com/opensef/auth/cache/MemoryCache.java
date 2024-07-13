package com.opensef.auth.cache;

import com.opensef.auth.constant.AuthConstant;

public class MemoryCache<K, V> implements Cache<K, V> {

    /**
     * 内存缓存，每1小时检查一次过期key
     */
    private final CacheMap<K, V> CACHE_MAP = new CacheMap<>(60 * 60 * 1000);

    @Override
    public V get(K key) {
        return CACHE_MAP.get(key);
    }

    @Override
    public void put(K key, V value) {
        CACHE_MAP.put(key, value, AuthConstant.NEVER_EXPIRE);
    }

    @Override
    public void put(K key, V value, long timeout) {
        CACHE_MAP.put(key, value, timeout);
    }

    @Override
    public V remove(K key) {
        return CACHE_MAP.remove(key);
    }

    @Override
    public void expire(K key, long timeout) {
        CACHE_MAP.expire(key, timeout);
    }

    /**
     * 数据不存在返回-2；永不过期返回-1；其他值表示剩余过期时间
     *
     * @param key key值
     * @return 剩余过期时间
     */
    @Override
    public Long getExpire(K key) {
        return CACHE_MAP.getExpire(key);
    }

    @Override
    public boolean isUnExpired(K key) {
        return Cache.super.isUnExpired(key);
    }

}
