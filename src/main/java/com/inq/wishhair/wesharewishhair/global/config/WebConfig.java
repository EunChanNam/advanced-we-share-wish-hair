package com.inq.wishhair.wesharewishhair.global.config;

import static org.springframework.http.HttpMethod.*;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.inq.wishhair.wesharewishhair.auth.domain.AuthTokenManager;
import com.inq.wishhair.wesharewishhair.global.interceptor.interceptor.AuthenticationInterceptor;
import com.inq.wishhair.wesharewishhair.global.interceptor.interceptor.PathMatcherInterceptor;
import com.inq.wishhair.wesharewishhair.global.resolver.AuthInfoArgumentResolver;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final AuthTokenManager authTokenManager;

	@Override
	public void addCorsMappings(final CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("http://localhost:8080")
			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
			.allowedHeaders("*");
	}

	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		HandlerInterceptor authenticationInterceptor = new AuthenticationInterceptor(authTokenManager);

		PathMatcherInterceptor pathMatcherInterceptor = new PathMatcherInterceptor(authenticationInterceptor)
			.addIncludePathPattern("/api/**")
			.addExcludePathPattern("/api/auth/login", POST)
			.addExcludePathPattern("/api/users")
			.addExcludePathPattern("/api/users/refresh/*")
			.addExcludePathPattern("/api/email/*");

		registry
			.addInterceptor(pathMatcherInterceptor)
			.addPathPatterns("/api/**")
			.order(1);
	}

	@Override
	public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new AuthInfoArgumentResolver(authTokenManager));
	}
}
