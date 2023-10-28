package com.inq.wishhair.wesharewishhair.user.presentation.dto.request;

import com.inq.wishhair.wesharewishhair.user.domain.entity.Sex;

import jakarta.validation.constraints.NotNull;

public record SignUpRequest(
	@NotNull
	String email,
	@NotNull
	String pw,
	@NotNull
	String name,
	@NotNull
	String nickname,
	@NotNull
	Sex sex
) {
}
