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

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewSearchController {

	private final ReviewSearchService reviewSearchService;

	@GetMapping(path = "{reviewId}")
	public ResponseEntity<ReviewDetailResponse> findReview(
		@PathVariable Long reviewId,
		@FetchAuthInfo AuthInfo authInfo
	) {
		ReviewDetailResponse result = reviewSearchService.findReviewById(authInfo.userId(), reviewId);

		return ResponseEntity.ok(result);
	}

	@GetMapping
	public ResponseEntity<PagedResponse<ReviewResponse>> findPagingReviews(
		@PageableDefault(sort = LIKES, direction = Sort.Direction.DESC) Pageable pageable,
		@FetchAuthInfo AuthInfo authInfo
	) {
		PagedResponse<ReviewResponse> result = reviewSearchService.findPagedReviews(authInfo.userId(), pageable);

		return ResponseEntity.ok(result);
	}

	@GetMapping("/my")
	public ResponseEntity<PagedResponse<ReviewResponse>> findMyReviews(
		@PageableDefault(sort = DATE, direction = Sort.Direction.DESC) Pageable pageable,
		@FetchAuthInfo AuthInfo authInfo
	) {
		PagedResponse<ReviewResponse> result = reviewSearchService.findMyReviews(authInfo.userId(), pageable);

		return ResponseEntity.ok(result);
	}

	@GetMapping("/month")
	public ResponseWrapper<ReviewSimpleResponse> findReviewOfMonth() {
		return reviewSearchService.findReviewOfMonth();
	}

	@GetMapping("/hair_style/{hairStyleId}")
	public ResponseWrapper<ReviewResponse> findHairStyleReview(
		@PathVariable Long hairStyleId,
		@FetchAuthInfo AuthInfo authInfo
	) {
		return reviewSearchService.findReviewByHairStyle(authInfo.userId(), hairStyleId);
	}
}
