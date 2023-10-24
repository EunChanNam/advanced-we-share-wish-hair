package com.inq.wishhair.wesharewishhair.auth.stub;

import com.inq.wishhair.wesharewishhair.auth.domain.AuthToken;
import com.inq.wishhair.wesharewishhair.auth.domain.AuthTokenManager;

public class AuthTokenMangerStub implements AuthTokenManager {

	@Override
	public AuthToken generate(Long userId) {
		return new AuthToken("accessToken", "refreshToken");
	}

	@Override
	public Long getId(String token) {
		return 1L;
	}

	@Override
	public void validateToken(String token) {
	}
}
