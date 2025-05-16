package com.littlebank.finance.global.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RedisDao {

    private final RedisTemplate<String, String> redisTemplate;

    public boolean existData(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void setValues(String key, String data, Duration duration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, data, duration);
    }

    public String getValues(String key) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key);
    }

    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }

    // SADD
    public void addToSet(String key, String data) {
        redisTemplate.opsForSet().add(key, data);
    }

    // SMEMBERS
    public Set<String> getSetMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    // SREM
    public void removeFromSet(String key, String data) {
        redisTemplate.opsForSet().remove(key, data);
    }

    // DEL
    public void deleteSet(String key) {
        redisTemplate.delete(key);
    }

    public int getSetSize(String likeSetKey) {
        Long size = redisTemplate.opsForSet().size(likeSetKey);
        return size != null? size.intValue() : 0;
    }

    public boolean isMemberOfSet(String likeSetKey, String data) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(likeSetKey, data));
    }

    public void addToSetWithTTL(String key, String data, Duration duration) {
        redisTemplate.opsForSet().add(key, data);
        redisTemplate.expire(key, duration);
    }
}
