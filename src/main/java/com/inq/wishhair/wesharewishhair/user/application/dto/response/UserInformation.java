package com.inq.wishhair.wesharewishhair.user.application.dto.response;

import com.inq.wishhair.wesharewishhair.user.domain.entity.Sex;

public record UserInformation(
	String email,
	String name,
	String nickname,
	Sex sex
) {
}
