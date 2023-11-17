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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Review API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews/like/")
public class LikeReviewController {

	private final LikeReviewService likeReviewService;

	@Operation(summary = "좋아요 API", description = "좋아요 한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	@PostMapping(path = "{reviewId}")
	public ResponseEntity<Success> executeLike(
		@Parameter(description = "리뷰 아이디") @PathVariable Long reviewId,
		@Parameter(hidden = true) @FetchAuthInfo AuthInfo authInfo
	) {
		likeReviewService.executeLike(reviewId, authInfo.userId());
		return ResponseEntity.ok(new Success());
	}

	@Operation(summary = "좋아요 취소 API", description = "좋아요를 취소한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	@DeleteMapping("/{reviewId}")
	public ResponseEntity<Success> cancelLike(
		@Parameter(description = "리뷰 아이디") @PathVariable Long reviewId,
		@Parameter(hidden = true) @FetchAuthInfo AuthInfo authInfo
	) {
		likeReviewService.cancelLike(reviewId, authInfo.userId());
		return ResponseEntity.ok(new Success());
	}

	@Operation(summary = "좋아요 확인 API", description = "좋아요한 상태인지 확인한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	@GetMapping(path = "{reviewId}")
	public ResponseEntity<LikeReviewResponse> checkIsLiking(
		@Parameter(hidden = true) @FetchAuthInfo AuthInfo authInfo,
		@Parameter(description = "리뷰 아이디") @PathVariable Long reviewId
	) {
		LikeReviewResponse result = likeReviewService.checkIsLiking(authInfo.userId(), reviewId);
		return ResponseEntity.ok(result);
	}
}
