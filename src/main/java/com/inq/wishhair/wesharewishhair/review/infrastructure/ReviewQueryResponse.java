package com.inq.wishhair.wesharewishhair.review.infrastructure;

import com.inq.wishhair.wesharewishhair.review.domain.entity.Review;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;

@Getter
public class ReviewQueryResponse {

	private final Review review;
	private final long likes;

	@QueryProjection
	public ReviewQueryResponse(Review review, long likes) {
		this.review = review;
		this.likes = likes;
	}
}