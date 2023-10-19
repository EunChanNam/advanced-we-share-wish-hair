package com.inq.wishhair.wesharewishhair.global.interceptor.interceptor;

import static com.inq.wishhair.wesharewishhair.global.exception.ErrorCode.*;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.inq.wishhair.wesharewishhair.auth.domain.AuthTokenManager;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

	private static final String AUTHORIZATION = "Authorization";
	private static final String BEARER = "Bearer";

	private final AuthTokenManager authTokenManager;

	@Override
	public boolean preHandle(
		final HttpServletRequest request,
		final HttpServletResponse response,
		final Object handler
	) {
		String header = request.getHeader(AUTHORIZATION);

		validateHasAuthorizationHeader(header);
		validateAuthorizationHeader(header);

		return true;
	}

	private void validateHasAuthorizationHeader(final String header) {
		if (!StringUtils.hasText(header)) {
			throw new WishHairException(AUTH_REQUIRED_LOGIN);
		}
	}

	private void validateAuthorizationHeader(final String header) {
		String[] splitHeader = header.split(" ");

		if (splitHeader.length != 2 || !splitHeader[0].equals(BEARER)) {
			throw new WishHairException(AUTH_INVALID_AUTHORIZATION_HEADER);
		}

		authTokenManager.validateToken(splitHeader[1]);
	}
}
