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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Review API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

	private final ReviewService reviewService;

	@Operation(summary = "리뷰 등록 API", description = "리뷰를 등록한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	@PostMapping
	public ResponseEntity<Success> createReview(
		@ModelAttribute ReviewCreateRequest reviewCreateRequest,
		@Parameter(hidden = true) @FetchAuthInfo AuthInfo authInfo
	) {
		Long reviewId = reviewService.createReview(reviewCreateRequest, authInfo.userId());

		return ResponseEntity
			.created(URI.create("/api/review/" + reviewId))
			.body(new Success());
	}

	@Operation(summary = "리뷰 삭제 API", description = "리뷰를 삭제한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	@DeleteMapping(path = "{reviewId}")
	public ResponseEntity<Success> deleteReview(
		@Parameter(hidden = true) @FetchAuthInfo AuthInfo authInfo,
		@PathVariable Long reviewId
	) {
		reviewService.deleteReview(reviewId, authInfo.userId());

		return ResponseEntity.ok(new Success());
	}

	@Operation(summary = "리뷰 수정 API", description = "리뷰를 수정한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	@PatchMapping
	public ResponseEntity<Success> updateReview(
		@ModelAttribute ReviewUpdateRequest request,
		@Parameter(hidden = true) @FetchAuthInfo AuthInfo authInfo
	) {
		reviewService.updateReview(request, authInfo.userId());

		return ResponseEntity.ok(new Success());
	}
}
