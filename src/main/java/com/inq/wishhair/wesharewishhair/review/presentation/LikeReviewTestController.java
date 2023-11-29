package com.inq.wishhair.wesharewishhair.review.presentation;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inq.wishhair.wesharewishhair.global.annotation.FetchAuthInfo;
import com.inq.wishhair.wesharewishhair.global.dto.response.Success;
import com.inq.wishhair.wesharewishhair.global.resolver.dto.AuthInfo;
import com.inq.wishhair.wesharewishhair.review.application.LikeReviewTestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/like")
public class LikeReviewTestController {

	private final LikeReviewTestService likeReviewService;

	@PostMapping("/test/clean")
	public Success clean() {
		likeReviewService.clean();
		return new Success();
	}

	@PostMapping("/test/count/{reviewId}")
	public Result count(@PathVariable Long reviewId) {
		likeReviewService.clean();
		return new Result(likeReviewService.count(reviewId));
	}

	@PostMapping("/test/lock/{reviewId}")
	public Success withLock(
		@PathVariable Long reviewId,
		@FetchAuthInfo AuthInfo authInfo
	) {
		likeReviewService.withLock(reviewId, authInfo.userId());
		return new Success();
	}

	@PostMapping("/test/no_lock/{reviewId}")
	public Success withoutLock(
		@PathVariable Long reviewId,
		@FetchAuthInfo AuthInfo authInfo
	) {
		likeReviewService.withoutLock(reviewId, authInfo.userId());
		return new Success();
	}

	record Result(long count) {
	}
}
