package com.inq.wishhair.wesharewishhair.review.application.query.dto;

import com.inq.wishhair.wesharewishhair.review.domain.entity.Review;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;

@Getter
public record ReviewQueryResponse(Review review, long likes) {

	@QueryProjection
	public ReviewQueryResponse {
		//QueryProjection 적용
	}
}
