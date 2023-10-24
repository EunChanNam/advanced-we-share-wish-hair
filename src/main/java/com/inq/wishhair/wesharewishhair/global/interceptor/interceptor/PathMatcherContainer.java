package com.inq.wishhair.wesharewishhair.global.interceptor.interceptor;

import java.util.HashSet;
import java.util.Set;

import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

public class PathMatcherContainer {

	private final PathMatcher pathMatcher;
	private final Set<PathInfo> includePath;
	private final Set<PathInfo> excludePath;

	public PathMatcherContainer() {
		pathMatcher = new AntPathMatcher();
		includePath = new HashSet<>();
		excludePath = new HashSet<>();
	}

	public boolean isInterceptorRequired(final String path, final HttpMethod method) {
		boolean isInclude = includePath.stream()
			.anyMatch(include -> matches(include, path, method));

		boolean isNotExclude = excludePath.stream()
			.noneMatch(exclude -> matches(exclude, path, method));

		return isNotExclude && isInclude;
	}

	public void includePathPattern(final String pathPattern, final HttpMethod method) {
		includePath.add(new PathInfo(pathPattern, method));
	}

	public void excludePathPattern(final String pathPattern, final HttpMethod method) {
		excludePath.add(new PathInfo(pathPattern, method));
	}

	private boolean matches(
		final PathInfo pathInfo,
		final String targetPath,
		final HttpMethod targetMethod
	) {
		boolean pathMatch = pathMatcher.match(pathInfo.pathPattern, targetPath);
		boolean methodMath = pathInfo.method.equals(targetMethod);
		return pathMatch && methodMath;
	}

	private record PathInfo(
		String pathPattern,
		HttpMethod method
	) {
	}
}
