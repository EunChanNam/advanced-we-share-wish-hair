package com.inq.wishhair.wesharewishhair.auth.application.dto.response;

public record LoginResponse(
	String accessToken,
	String refreshToken
) {
}
