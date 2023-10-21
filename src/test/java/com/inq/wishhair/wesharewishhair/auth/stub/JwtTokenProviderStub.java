package com.inq.wishhair.wesharewishhair.auth.stub;

import com.inq.wishhair.wesharewishhair.auth.infrastructure.jwt.JwtTokenProvider;

public class JwtTokenProviderStub extends JwtTokenProvider {

	private static final String ACCESS_TOKEN = "access_token";
	private static final String REFRESH_TOKEN = "refresh_token";

	public JwtTokenProviderStub() {
		super("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 0, 0);
	}

	@Override
	public String createAccessToken(final Long userId) {
		return ACCESS_TOKEN;
	}

	@Override
	public String createRefreshToken(final Long userId) {
		return REFRESH_TOKEN;
	}

	@Override
	public Long getPayload(final String token) {
		return 1L;
	}

	@Override
	public void validateToken(final String token) {
	}
}
