package com.inq.wishhair.wesharewishhair.review.application;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inq.wishhair.wesharewishhair.review.domain.entity.Review;
import com.inq.wishhair.wesharewishhair.review.domain.likereview.LikeReview;
import com.inq.wishhair.wesharewishhair.review.domain.likereview.LikeReviewRepository;
import com.inq.wishhair.wesharewishhair.review.application.dto.response.LikeReviewResponse;

import lombok.RequiredArgsConstructor;

//todo : 캐시를 이용해서 DB 에 변경을 줄이는 방식 적용해보기
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeReviewService {

	private final LikeReviewRepository likeReviewRepository;
	private final ReviewFindService reviewFindService;
	private final Set<Long> updateReviewSet;

	@Transactional
	public void executeLike(Long reviewId, Long userId) {
		if (notExistLikeReview(userId, reviewId)) {
			likeReviewRepository.save(LikeReview.addLike(userId, reviewId));

			Review review = reviewFindService.getById(reviewId);
			review.addLike();

			updateReviewSet.add(reviewId);
		}
	}

	@Transactional
	public void cancelLike(Long reviewId, Long userId) {
		likeReviewRepository.deleteByUserIdAndReviewId(userId, reviewId);

		Review review = reviewFindService.getById(reviewId);
		review.cancelLike();

		updateReviewSet.add(reviewId);
	}

	public LikeReviewResponse checkIsLiking(Long userId, Long reviewId) {
		return new LikeReviewResponse(notExistLikeReview(userId, reviewId));
	}

	private boolean notExistLikeReview(Long userId, Long reviewId) {
		return !likeReviewRepository.existsByUserIdAndReviewId(userId, reviewId);
	}
}
