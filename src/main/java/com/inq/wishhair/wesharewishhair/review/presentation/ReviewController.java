package com.inq.wishhair.wesharewishhair.review.presentation;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inq.wishhair.wesharewishhair.global.annotation.FetchAuthInfo;
import com.inq.wishhair.wesharewishhair.global.dto.response.Success;
import com.inq.wishhair.wesharewishhair.global.resolver.dto.AuthInfo;
import com.inq.wishhair.wesharewishhair.review.application.dto.request.ReviewCreateRequest;
import com.inq.wishhair.wesharewishhair.review.application.dto.request.ReviewUpdateRequest;
import com.inq.wishhair.wesharewishhair.review.application.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

	private final ReviewService reviewService;

	@PostMapping
	public ResponseEntity<Success> createReview(
		@ModelAttribute ReviewCreateRequest reviewCreateRequest,
		@FetchAuthInfo AuthInfo authInfo
	) {
		Long reviewId = reviewService.createReview(reviewCreateRequest, authInfo.userId());

		return ResponseEntity
			.created(URI.create("/api/review/" + reviewId))
			.body(new Success());
	}

	@DeleteMapping(path = "{reviewId}")
	public ResponseEntity<Success> deleteReview(
		@FetchAuthInfo AuthInfo authInfo,
		@PathVariable Long reviewId
	) {
		reviewService.deleteReview(reviewId, authInfo.userId());

		return ResponseEntity.ok(new Success());
	}

	@PatchMapping
	public ResponseEntity<Success> updateReview(
		@ModelAttribute ReviewUpdateRequest request,
		@FetchAuthInfo AuthInfo authInfo
	) {
		reviewService.updateReview(request, authInfo.userId());

		return ResponseEntity.ok(new Success());
	}
}
