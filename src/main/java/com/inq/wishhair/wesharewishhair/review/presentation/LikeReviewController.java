package com.inq.wishhair.wesharewishhair.review.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inq.wishhair.wesharewishhair.global.annotation.FetchAuthInfo;
import com.inq.wishhair.wesharewishhair.global.dto.response.Success;
import com.inq.wishhair.wesharewishhair.global.resolver.dto.AuthInfo;
import com.inq.wishhair.wesharewishhair.review.application.LikeReviewService;
import com.inq.wishhair.wesharewishhair.review.application.dto.response.LikeReviewResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews/like/")
public class LikeReviewController {

	private final LikeReviewService likeReviewService;

	@PostMapping(path = "{reviewId}")
	public ResponseEntity<Success> executeLike(
		final @PathVariable Long reviewId,
		final @FetchAuthInfo AuthInfo authInfo
	) {

		likeReviewService.executeLike(reviewId, authInfo.userId());
		return ResponseEntity.ok(new Success());
	}

	@DeleteMapping("/{reviewId}")
	public ResponseEntity<Success> cancelLike(
		final @PathVariable Long reviewId,
		final @FetchAuthInfo AuthInfo authInfo
	) {

		likeReviewService.cancelLike(reviewId, authInfo.userId());
		return ResponseEntity.ok(new Success());
	}

	@GetMapping(path = "{reviewId}")
	public ResponseEntity<LikeReviewResponse> checkIsLiking(
		final @FetchAuthInfo AuthInfo authInfo,
		final @PathVariable Long reviewId
	) {

		LikeReviewResponse result = likeReviewService.checkIsLiking(authInfo.userId(), reviewId);
		return ResponseEntity.ok(result);
	}
}
