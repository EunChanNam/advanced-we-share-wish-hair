package com.inq.wishhair.wesharewishhair.review.application.dto.response;

import com.inq.wishhair.wesharewishhair.review.domain.entity.Review;

public record ReviewSimpleResponse(
	Long reviewId,
	String userNickname,
	String hairStyleName,
	String contents
) {
	public ReviewSimpleResponse(Review review) {
		this(
			review.getId(),
			review.getWriter().getNicknameValue(),
			review.getHairStyle().getName(),
			review.getContentsValue()
		);
	}
}
