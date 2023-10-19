package com.inq.wishhair.wesharewishhair.auth.infrastructure.jwt;

import org.springframework.stereotype.Component;

import com.inq.wishhair.wesharewishhair.auth.domain.TokenManager;
import com.inq.wishhair.wesharewishhair.auth.domain.AuthToken;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthTokenManagerAdaptor implements TokenManager {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public AuthToken generate(final Long memberId) {
		String accessToken = jwtTokenProvider.createAccessToken(memberId);
		String refreshToken = jwtTokenProvider.createRefreshToken(memberId);
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
