package com.inq.wishhair.wesharewishhair.review.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inq.wishhair.wesharewishhair.global.dto.response.Success;
import com.inq.wishhair.wesharewishhair.review.service.LikeReviewService;
import com.inq.wishhair.wesharewishhair.review.service.dto.response.LikeReviewResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews/like/")
public class LikeReviewController {

	private final LikeReviewService likeReviewService;

	@PostMapping(path = "{reviewId}")
	public ResponseEntity<Success> executeLike(
		@PathVariable Long reviewId,
		@ExtractPayload Long userId) {

		likeReviewService.executeLike(reviewId, userId);
		return ResponseEntity.ok(new Success());
	}

	@DeleteMapping("/{reviewId}")
	public ResponseEntity<Success> cancelLike(
		@PathVariable Long reviewId,
		@ExtractPayload Long userId) {

		likeReviewService.cancelLike(reviewId, userId);
		return ResponseEntity.ok(new Success());
	}

	@GetMapping(path = "{reviewId}")
	public ResponseEntity<LikeReviewResponse> checkIsLiking(@ExtractPayload Long userId,
		@PathVariable Long reviewId) {

		LikeReviewResponse result = likeReviewService.checkIsLiking(userId, reviewId);
		return ResponseEntity.ok(result);
	}
}
