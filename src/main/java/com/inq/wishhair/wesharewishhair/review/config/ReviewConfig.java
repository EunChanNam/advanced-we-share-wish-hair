package com.inq.wishhair.wesharewishhair.review.config;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.inq.wishhair.wesharewishhair.review.config.converter.ScoreConverter;

@Configuration
public class ReviewConfig implements WebMvcConfigurer {

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new ScoreConverter());
	}

	@Bean
	public Set<Long> updateReviewMap() {
		Map<Long, Boolean> concurrentHashMap = new ConcurrentHashMap<>();
		return concurrentHashMap.keySet();
	}
}
