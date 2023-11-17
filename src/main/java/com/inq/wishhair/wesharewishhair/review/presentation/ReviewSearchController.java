package com.inq.wishhair.wesharewishhair.review.presentation;

import static com.inq.wishhair.wesharewishhair.global.utils.SortCondition.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inq.wishhair.wesharewishhair.global.annotation.FetchAuthInfo;
import com.inq.wishhair.wesharewishhair.global.dto.response.PagedResponse;
import com.inq.wishhair.wesharewishhair.global.dto.response.ResponseWrapper;
import com.inq.wishhair.wesharewishhair.global.resolver.dto.AuthInfo;
import com.inq.wishhair.wesharewishhair.review.application.ReviewSearchService;
import com.inq.wishhair.wesharewishhair.review.application.dto.response.ReviewDetailResponse;
import com.inq.wishhair.wesharewishhair.review.application.dto.response.ReviewResponse;
import com.inq.wishhair.wesharewishhair.review.application.dto.response.ReviewSimpleResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Review API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewSearchController {

	private final ReviewSearchService reviewSearchService;

	@Operation(summary = "리뷰 단건 조회 API", description = "좋아요 한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	@GetMapping(path = "{reviewId}")
	public ResponseEntity<ReviewDetailResponse> findReview(
		@Parameter(description = "리뷰 아이디") @PathVariable Long reviewId,
		@Parameter(hidden = true) @FetchAuthInfo AuthInfo authInfo
	) {
		ReviewDetailResponse result = reviewSearchService.findReviewById(authInfo.userId(), reviewId);

		return ResponseEntity.ok(result);
	}

	@Operation(summary = "리뷰 조회 API", description = "리뷰를 조회한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	@GetMapping
	public ResponseEntity<PagedResponse<ReviewResponse>> findPagingReviews(
		@Parameter(description = "페이징 정보(기본 좋아요 개수로 정렬)")
		@PageableDefault(sort = LIKES, direction = Sort.Direction.DESC) Pageable pageable,
		@Parameter(hidden = true) @FetchAuthInfo AuthInfo authInfo
	) {
		PagedResponse<ReviewResponse> result = reviewSearchService.findPagedReviews(authInfo.userId(), pageable);

		return ResponseEntity.ok(result);
	}

	@Operation(summary = "나의 리뷰 조회 API", description = "내가 작성한 리뷰를 조회한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	@GetMapping("/my")
	public ResponseEntity<PagedResponse<ReviewResponse>> findMyReviews(
		@Parameter(description = "페이징 정보(최신순으로 정렬)")
		@PageableDefault(sort = DATE, direction = Sort.Direction.DESC) Pageable pageable,
		@Parameter(hidden = true) @FetchAuthInfo AuthInfo authInfo
	) {
		PagedResponse<ReviewResponse> result = reviewSearchService.findMyReviews(authInfo.userId(), pageable);

		return ResponseEntity.ok(result);
	}

	@Operation(summary = "이달의 리뷰 조회 API", description = "이달의 리뷰를 조회한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	@GetMapping("/month")
	public ResponseWrapper<ReviewSimpleResponse> findReviewOfMonth() {
		return reviewSearchService.findReviewOfMonth();
	}

	@Operation(summary = "헤어스타일 리뷰 조회 API", description = "헤어스타일의 리뷰를 조회한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	@GetMapping("/hair_style/{hairStyleId}")
	public ResponseWrapper<ReviewResponse> findHairStyleReview(
		@Parameter(description = "헤어스타일 아이디") @PathVariable Long hairStyleId,
		@Parameter(hidden = true) @FetchAuthInfo AuthInfo authInfo
	) {
		return reviewSearchService.findReviewByHairStyle(authInfo.userId(), hairStyleId);
	}
}
