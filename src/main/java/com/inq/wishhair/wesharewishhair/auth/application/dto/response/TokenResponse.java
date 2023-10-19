package com.inq.wishhair.wesharewishhair.auth.application.dto.response;

public record TokenResponse(
	String accessToken,
	String refreshToken
) {
}
