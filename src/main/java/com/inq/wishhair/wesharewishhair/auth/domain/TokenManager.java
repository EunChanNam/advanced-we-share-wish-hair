package com.inq.wishhair.wesharewishhair.auth.domain;

public interface TokenManager {

	AuthToken generate(final Long memberId);

	Long getId(final String token);

	void validateToken(final String token);
}
