package com.inq.wishhair.wesharewishhair.review.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inq.wishhair.wesharewishhair.global.dto.response.Success;
import com.inq.wishhair.wesharewishhair.review.controller.dto.request.ReviewCreateRequest;
import com.inq.wishhair.wesharewishhair.review.controller.dto.request.ReviewUpdateRequest;
import com.inq.wishhair.wesharewishhair.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

	private final ReviewService reviewService;

	@PostMapping
	public ResponseEntity<Success> createReview(
		@ModelAttribute ReviewCreateRequest reviewCreateRequest,
		@ExtractPayload Long userId) {

		Long reviewId = reviewService.createReview(reviewCreateRequest, userId);
		return ResponseEntity
			.created(URI.create("/api/review/" + reviewId))
			.body(new Success());
	}

	@DeleteMapping(path = "{reviewId}")
	public ResponseEntity<Success> deleteReview(@ExtractPayload Long userId,
		@PathVariable Long reviewId) {

		reviewService.deleteReview(reviewId, userId);

		return ResponseEntity.ok(new Success());
	}

	@PatchMapping
	public ResponseEntity<Success> updateReview(@ModelAttribute ReviewUpdateRequest request,
		@ExtractPayload Long userId) {

		reviewService.updateReview(request, userId);

		return ResponseEntity.ok(new Success());
	}
}
