package com.inq.wishhair.wesharewishhair.user.presentation.dto.request;

public record PasswordRefreshRequest(
	String email,
	String newPassword
) {
}
