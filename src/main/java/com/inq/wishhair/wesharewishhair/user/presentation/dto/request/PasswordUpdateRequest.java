package com.inq.wishhair.wesharewishhair.user.presentation.dto.request;

public record PasswordUpdateRequest(
	String oldPassword,
	String newPassword
) {
}
