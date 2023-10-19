package com.inq.wishhair.wesharewishhair.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.inq.wishhair.wesharewishhair.global.aop.aspect.AddIsWriterAspect;

@Configuration
@EnableAspectJAutoProxy
public class AopConfig {

	@Bean
	public AddIsWriterAspect addIsWriterAspect() {
		return new AddIsWriterAspect();
	}
}
