package com.inq.wishhair.wesharewishhair.user.presentation.dto.request;

import com.inq.wishhair.wesharewishhair.user.domain.entity.Sex;

public record UserUpdateRequest(
	String nickname,
	Sex sex
) {
}
