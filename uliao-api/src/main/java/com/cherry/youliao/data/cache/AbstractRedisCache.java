package com.cherry.youliao.data.cache;

import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unchecked")
public abstract class AbstractRedisCache<K,V> implements InitializingBean {

    @Autowired
    private RedisTemplate redisTemplate;

    private RedisSerializer<K> keySerializer;

    private RedisSerializer<V> valueSerializer;

    private String prefix;

    protected abstract RedisSerializer<K> keySerializer();

    protected abstract RedisSerializer<V> valueSerializer();

    public AbstractRedisCache(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.keySerializer = keySerializer();
        this.valueSerializer = valueSerializer();
        if (null == this.keySerializer || null == this.valueSerializer) {
            throw new BeanInitializationException("null == keySerializer || null == valueSerializer");
        }
    }

    public V get(K key) {
        final String cacheKey = genRealKey(key);
        return (V) redisTemplate.opsForValue().get(cacheKey);
    }

    public void put(K key, V value) {
        final String cacheKey = genRealKey(key);
        redisTemplate.opsForValue().set(cacheKey, value);
    }

    public void put(K key, V value, long expires, TimeUnit timeUnit) {
        final String cacheKey = genRealKey(key);
        redisTemplate.opsForValue().set(cacheKey, value, expires, timeUnit);
    }

    public void put(K key, V value, Date expiresAt) {
        final String cacheKey = genRealKey(key);
        redisTemplate.opsForValue().set(cacheKey, value);
        redisTemplate.opsForValue().getOperations().expireAt(cacheKey, expiresAt);
    }

    private String genRealKey(K key) {
        return String.format("%s-%s", prefix, key);
    }
}
