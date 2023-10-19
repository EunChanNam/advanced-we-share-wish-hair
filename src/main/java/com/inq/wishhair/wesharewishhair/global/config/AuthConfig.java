package com.inq.wishhair.wesharewishhair.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.inq.wishhair.wesharewishhair.auth.utils.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AuthConfig implements WebMvcConfigurer {

	private final JwtTokenProvider provider;

	// @Override
	// public void addInterceptors(InterceptorRegistry registry) {
	//     registry.addInterceptor(new )
	//             .addPathPatterns("/api/**")
	//             .excludePathPatterns("/api/auth/login", "/api/users", "/api/users/refresh/*", "/api/email/*",
	//                     "/api/oauth/login", "/api/oauth/access");
	// }

	// @Override
	// public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
	//     resolvers.addAll(List.of(new TokenResolver(), new PayloadResolver(provider)));
	// }
}
