package com.inq.wishhair.wesharewishhair.global.interceptor.interceptor;

import java.util.Arrays;

import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PathMatcherInterceptor implements HandlerInterceptor {

	private final HandlerInterceptor target;
	private final PathMatcherContainer pathMatcherContainer;

	public PathMatcherInterceptor(final HandlerInterceptor target) {
		this.target = target;
		pathMatcherContainer = new PathMatcherContainer();
	}

	@Override
	public boolean preHandle(
		final HttpServletRequest request,
		final HttpServletResponse response,
		final Object handler
	) throws Exception {
		boolean interceptorRequired = pathMatcherContainer.isInterceptorRequired(
			request.getRequestURI(), HttpMethod.valueOf(request.getMethod())
		);

		if (interceptorRequired) {
			return target.preHandle(request, response, handler);
		}
		return true;
	}

	public PathMatcherInterceptor addIncludePathPattern(final String pathPattern, final HttpMethod method) {
		pathMatcherContainer.includePathPattern(pathPattern, method);
		return this;
	}

	public PathMatcherInterceptor addIncludePathPattern(final String pathPattern) {
		Arrays.stream(HttpMethod.values())
				.forEach(method ->
					pathMatcherContainer.includePathPattern(pathPattern, method));

		return this;
	}

	public PathMatcherInterceptor addExcludePathPattern(final String pathPattern, final HttpMethod method) {
		pathMatcherContainer.excludePathPattern(pathPattern, method);
		return this;
	}

	public PathMatcherInterceptor addExcludePathPattern(final String pathPattern) {
		Arrays.stream(HttpMethod.values())
			.forEach(method ->
				pathMatcherContainer.excludePathPattern(pathPattern, method));

		return this;
	}
}
