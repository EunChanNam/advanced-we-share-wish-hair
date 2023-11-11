package com.inq.wishhair.wesharewishhair.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import redis.embedded.RedisServer;

@TestConfiguration
public class EmbeddedRedisConfig {

	private final RedisServer redisServer;

	public EmbeddedRedisConfig(@Value("${spring.data.redis.port}") int port) {
		this.redisServer = new RedisServer(port);
	}

	@PostConstruct
	public void startRedis() {
		this.redisServer.start();
	}

	@PreDestroy
	public void stopRedis() {
		this.redisServer.stop();
	}
}