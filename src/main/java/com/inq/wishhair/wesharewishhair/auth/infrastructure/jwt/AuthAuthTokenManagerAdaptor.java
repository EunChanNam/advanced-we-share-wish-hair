package com.inq.wishhair.wesharewishhair.auth.infrastructure.jwt;

import org.springframework.stereotype.Component;

import com.inq.wishhair.wesharewishhair.auth.domain.AuthTokenManager;
import com.inq.wishhair.wesharewishhair.auth.domain.AuthToken;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthAuthTokenManagerAdaptor implements AuthTokenManager {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public AuthToken generate(final Long userId) {
		String accessToken = jwtTokenProvider.createAccessToken(userId);
		String refreshToken = jwtTokenProvider.createRefreshToken(userId);
		return new AuthToken(accessToken, refreshToken);
	}

	@Override
	public Long getId(final String token) {
		return jwtTokenProvider.getPayload(token);
	}

	@Override
	public void validateToken(final String token) {
		jwtTokenProvider.validateToken(token);
	}
}
