package com.inq.wishhair.wesharewishhair.global.utils;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtils {

	private final RedisTemplate<String, Long> redisTemplate;
	private final long expireTime;

	public RedisUtils(
		RedisTemplate<String, Long> redisTemplate,
		@Value("${spring.data.redis.expire-time}") long expireTime
	) {
		this.redisTemplate = redisTemplate;
		this.expireTime = expireTime;
	}

	public void setData(Long key, Long value) {
		redisTemplate
			.opsForValue()
			.set(String.valueOf(key), value, expireTime, TimeUnit.MILLISECONDS);
	}

	public void increaseData(Long key) {
		redisTemplate.opsForValue().increment(String.valueOf(key));
	}

	public void decreaseData(Long key) {
		redisTemplate.opsForValue().decrement(String.valueOf(key));
	}

	public Optional<Long> getData(Long key) {
		return Optional.ofNullable(
			redisTemplate.opsForValue().get(String.valueOf(key))
		);
	}
}
