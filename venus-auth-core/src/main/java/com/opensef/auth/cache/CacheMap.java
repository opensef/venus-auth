package com.opensef.auth.cache;

import com.opensef.auth.constant.AuthConstant;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CacheMap<K, V> {

    private final Map<K, CacheData> DATA_MAP = new ConcurrentHashMap<>();

    /**
     * 过期检查时间周期（毫秒）
     */
    private final long expireCheckCycle;

    protected CacheMap(long expireCheckCycle) {
        this.expireCheckCycle = expireCheckCycle;

        // 启动移除过期key任务
        removeExpiredKeyTask();
    }

    public void put(K key, V value, long timeout) {
        CacheData cacheData = new CacheData();
        cacheData.setData(value);
        cacheData.setTimeout(timeout == AuthConstant.NEVER_EXPIRE ? AuthConstant.NEVER_EXPIRE : System.currentTimeMillis() + timeout);
        DATA_MAP.put(key, cacheData);
    }

    @SuppressWarnings("unchecked")
    public V get(K key) {
        CacheData cacheData = DATA_MAP.get(key);
        if (null != cacheData) {
            // 如果key已经过期，则将其删除
            if (cacheData.getTimeout() != AuthConstant.NEVER_EXPIRE && cacheData.getTimeout() <= System.currentTimeMillis()) {
                DATA_MAP.remove(key);
                return null;
            }
            return (V) cacheData.getData();
        } else {
            return null;
        }
    }

    /**
     * 设置过期时间
     *
     * @param key     key
     * @param timeout 过期时间（毫秒）
     */
    public void expire(K key, long timeout) {
        CacheData cacheData = DATA_MAP.get(key);
        if (null != cacheData) {
            cacheData.setTimeout(timeout == AuthConstant.NEVER_EXPIRE ? timeout : System.currentTimeMillis() + timeout);
            DATA_MAP.put(key, cacheData);
        }
    }

    /**
     * 获取过期时间
     * 数据不存在返回-2；永不过期返回-1；其他值表示剩余过期时间
     *
     * @param key key
     * @return 过期时间（毫秒）
     */
    public Long getExpire(K key) {
        long currentTime = System.currentTimeMillis();
        CacheData cacheData = DATA_MAP.get(key);
        if (cacheData == null) {
            return AuthConstant.EXPIRED_DATA_NOT_EXIST;
        }

        if (cacheData.getTimeout() > currentTime) {
            return cacheData.getTimeout() - currentTime;
        } else if (cacheData.getTimeout() == AuthConstant.NEVER_EXPIRE) {
            return AuthConstant.NEVER_EXPIRE;
        } else {
            DATA_MAP.remove(key);
            return AuthConstant.EXPIRED_DATA_NOT_EXIST;
        }
    }

    @SuppressWarnings("unchecked")
    public V remove(K key) {
        CacheData cacheData = DATA_MAP.remove(key);
        if (null != cacheData) {
            return (V) cacheData.data;
        } else {
            return null;
        }
    }

    public static class CacheData {

        /**
         * 数据
         */
        private Object data;

        /**
         * 到期时间戳（毫秒）
         */
        private Long timeout;

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public Long getTimeout() {
            return timeout;
        }

        public void setTimeout(Long timeout) {
            this.timeout = timeout;
        }

    }

    /**
     * 移除过期数据定时任务
     */
    private void removeExpiredKeyTask() {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            DATA_MAP.entrySet().removeIf(entry -> entry.getValue().getTimeout() != AuthConstant.NEVER_EXPIRE &&
                    entry.getValue().getTimeout() < System.currentTimeMillis());
        }, expireCheckCycle, expireCheckCycle, TimeUnit.MILLISECONDS);
    }

}
