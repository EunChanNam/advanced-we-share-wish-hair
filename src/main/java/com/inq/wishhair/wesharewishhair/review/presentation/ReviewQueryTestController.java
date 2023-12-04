package com.inq.wishhair.wesharewishhair.review.presentation;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inq.wishhair.wesharewishhair.review.application.LikeReviewService;
import com.inq.wishhair.wesharewishhair.review.domain.entity.Review;
import com.inq.wishhair.wesharewishhair.review.infrastructure.ReviewQueryDslRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews/test")
public class ReviewQueryTestController {

	private final ReviewQueryDslRepository reviewQueryDslRepository;
	private final LikeReviewService likeReviewService;

	@Transactional(readOnly = true)
	@GetMapping("/join")
	public void withJoin() {
		reviewQueryDslRepository.joinWithLikeQuery();
	}

	@Transactional(readOnly = true)
	@GetMapping("/no_join")
	public void withoutJoin() {
		List<Review> reviews = reviewQueryDslRepository.noJoinQuery();
		List<Long> reviewIds = reviews.stream().map(Review::getId).toList();
		likeReviewService.getLikeCounts(reviewIds);
	}
}
