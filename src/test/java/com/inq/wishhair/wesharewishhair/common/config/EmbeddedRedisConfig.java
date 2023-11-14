package com.inq.wishhair.wesharewishhair.common.config;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.redis.core.RedisTemplate;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import redis.embedded.RedisServer;

@TestConfiguration
public class EmbeddedRedisConfig {

	private final RedisServer redisServer;
	private final RedisTemplate<String, Long> redisTemplate;

	public EmbeddedRedisConfig(
		@Value("${spring.data.redis.port}") int port,
		@Autowired RedisTemplate<String, Long> redisTemplate
	) {
		this.redisServer = new RedisServer(port);
		this.redisTemplate = redisTemplate;
	}

	@PostConstruct
	public void startRedis() {
		try {
			this.redisServer.start();
		} catch (RuntimeException e) {
			Set<String> keys = redisTemplate.keys("*");
			if (keys != null) {
				redisTemplate.delete(keys);
			}
		}
	}

	@PreDestroy
	public void stopRedis() {
		this.redisServer.stop();
	}
}