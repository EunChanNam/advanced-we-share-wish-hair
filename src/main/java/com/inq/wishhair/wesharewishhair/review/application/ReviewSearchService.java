package com.inq.wishhair.wesharewishhair.review.application;

import static com.inq.wishhair.wesharewishhair.review.application.dto.response.ReviewResponseAssembler.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inq.wishhair.wesharewishhair.global.aop.annotation.AddisWriter;
import com.inq.wishhair.wesharewishhair.global.dto.response.PagedResponse;
import com.inq.wishhair.wesharewishhair.global.dto.response.ResponseWrapper;
import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;
import com.inq.wishhair.wesharewishhair.review.application.dto.response.ReviewDetailResponse;
import com.inq.wishhair.wesharewishhair.review.application.dto.response.ReviewResponse;
import com.inq.wishhair.wesharewishhair.review.application.dto.response.ReviewSimpleResponse;
import com.inq.wishhair.wesharewishhair.review.domain.ReviewQueryRepository;
import com.inq.wishhair.wesharewishhair.review.domain.entity.Review;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewSearchService {

	private final ReviewQueryRepository reviewQueryRepository;
	private final LikeReviewService likeReviewService;

	private List<Long> fetchLikeCounts(List<Review> result) {
		List<Long> reviewIds = result.stream().map(Review::getId).toList();
		return likeReviewService.getLikeCounts(reviewIds);
	}

	/*리뷰 단건 조회*/
	@AddisWriter
	public ReviewDetailResponse findReviewById(Long userId, Long reviewId) {
		Review review = reviewQueryRepository.findReviewById(reviewId)
			.orElseThrow(() -> new WishHairException(ErrorCode.NOT_EXIST_KEY));

		Long likeCount = likeReviewService.getLikeCount(review.getId());
		boolean isLiking = likeReviewService.checkIsLiking(userId, reviewId).isLiking();

		return toReviewDetailResponse(review, likeCount, isLiking);
	}

	/*전체 리뷰 조회*/
	@AddisWriter
	public PagedResponse<ReviewResponse> findPagedReviews(Long userId, Pageable pageable) {
		Slice<Review> sliceResult = reviewQueryRepository.findReviewByPaging(pageable);

		List<Long> likeCounts = fetchLikeCounts(sliceResult.getContent());

		return toPagedReviewResponse(sliceResult, likeCounts);
	}

	/*좋아요한 리뷰 조회*/
	@AddisWriter
	public PagedResponse<ReviewResponse> findLikingReviews(Long userId, Pageable pageable) {
		Slice<Review> sliceResult = reviewQueryRepository.findReviewByLike(userId, pageable);

		List<Long> likeCounts = fetchLikeCounts(sliceResult.getContent());

		return toPagedReviewResponse(sliceResult, likeCounts);
	}

	/*나의 리뷰 조회*/
	@AddisWriter
	public PagedResponse<ReviewResponse> findMyReviews(Long userId, Pageable pageable) {
		Slice<Review> sliceResult = reviewQueryRepository.findReviewByUser(userId, pageable);

		List<Long> likeCounts = fetchLikeCounts(sliceResult.getContent());

		return toPagedReviewResponse(sliceResult, likeCounts);
	}

	/*이달의 추천 리뷰 조회*/
	public ResponseWrapper<ReviewSimpleResponse> findReviewOfMonth() {
		List<Review> result = reviewQueryRepository.findReviewByCreatedDate();
		return toWrappedSimpleResponse(result);
	}

	/*헤어스타일의 리뷰 조회*/
	@AddisWriter
	public ResponseWrapper<ReviewResponse> findReviewByHairStyle(Long userId, Long hairStyleId) {
		List<Review> result = reviewQueryRepository.findReviewByHairStyle(hairStyleId);

		List<Long> likeCounts = fetchLikeCounts(result);

		return toWrappedReviewResponse(result, likeCounts);
	}
}
