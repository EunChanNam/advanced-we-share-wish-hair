package com.inq.wishhair.wesharewishhair.review.presentation;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inq.wishhair.wesharewishhair.global.dto.response.ResponseWrapper;
import com.inq.wishhair.wesharewishhair.review.application.dto.response.ReviewResponse;
import com.inq.wishhair.wesharewishhair.review.application.dto.response.ReviewResponseAssembler;
import com.inq.wishhair.wesharewishhair.review.domain.entity.Review;
import com.inq.wishhair.wesharewishhair.review.infrastructure.ReviewQueryDslRepository;
import com.inq.wishhair.wesharewishhair.review.infrastructure.ReviewQueryResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews/test")
public class ReviewQueryTestController {

	private final ReviewQueryDslRepository reviewQueryDslRepository;

	@Transactional(readOnly = true)
	public ResponseWrapper<ReviewResponse> withJoin() {
		List<Review> reviews = reviewQueryDslRepository.joinWithLikeQuery()
			.stream()
			.map(ReviewQueryResponse::getReview)
			.toList();
		return ReviewResponseAssembler.toWrappedReviewResponse(reviews);
	}

	@Transactional(readOnly = true)
	public ResponseWrapper<ReviewResponse> withoutJoin() {
		List<Review> reviews = reviewQueryDslRepository.noJoinQuery();
		return ReviewResponseAssembler.toWrappedReviewResponse(reviews);
	}
}
