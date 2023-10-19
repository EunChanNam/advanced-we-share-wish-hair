package com.inq.wishhair.wesharewishhair.auth.domain;

public interface AuthTokenManager {

	AuthToken generate(final Long userId);

	Long getId(final String token);

	void validateToken(final String token);
}
