package com.inq.wishhair.wesharewishhair.auth.domain;

public record AuthToken(
	String accessToken,
	String refreshToken
) {
}
