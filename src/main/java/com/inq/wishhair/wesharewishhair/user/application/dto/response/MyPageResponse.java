package com.inq.wishhair.wesharewishhair.user.application.dto.response;

import java.util.List;

import com.inq.wishhair.wesharewishhair.review.service.dto.response.ReviewResponse;
import com.inq.wishhair.wesharewishhair.user.domain.entity.Sex;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;

public record MyPageResponse(
	String nickname,
	Sex sex,
	int point,
	List<ReviewResponse> reviews
) {
	public MyPageResponse(
		final User user,
		final List<ReviewResponse> reviewResponses,
		final int point
	) {
		this(
			user.getNicknameValue(),
			user.getSex(),
			point,
			reviewResponses
		);
	}
}
