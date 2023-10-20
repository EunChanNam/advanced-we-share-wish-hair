package com.inq.wishhair.wesharewishhair.review.application.dto.response;

public record ReviewDetailResponse(
	ReviewResponse reviewResponse,
	boolean isLiking
) {
}
