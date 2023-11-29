package com.inq.wishhair.wesharewishhair.review.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inq.wishhair.wesharewishhair.global.utils.RedisUtils;
import com.inq.wishhair.wesharewishhair.review.domain.entity.Review;
import com.inq.wishhair.wesharewishhair.review.domain.likereview.LikeReview;
import com.inq.wishhair.wesharewishhair.review.domain.likereview.LikeReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeReviewTestService {

	private final LikeReviewRepository likeReviewRepository;
	private final ReviewFindService reviewFindService;
	private final RedisUtils redisUtils;

	@Transactional(readOnly = true)
	public long count(Long reviewId) {
		return likeReviewRepository.countByReviewId(reviewId);
	}

	@Transactional
	public void clean() {
		likeReviewRepository.deleteAll();
	}

	/**
	 * LikeReview 생성 후 Review 에 락걸고 likeCount 변수 update
	 */
	@Transactional
	public void withLock(Long reviewId, Long userId) {
		likeReviewRepository.save(LikeReview.addLike(userId, reviewId));

		Review review = reviewFindService.getWithLockById(reviewId);
		review.addLike();
	}

	/**
	 * 레디스에 좋아요 정보가 없으면 새로 등록하고 있으면 INCR 수행
	 */
	@Transactional
	public void withoutLock(Long reviewId, Long userId) {
		likeReviewRepository.save(LikeReview.addLike(userId, reviewId));

		redisUtils.getData(reviewId)
			.ifPresentOrElse(
				likeCount -> redisUtils.increaseData(reviewId),
				() -> {
					Long likeCount = likeReviewRepository.countByReviewId(reviewId);
					redisUtils.setData(reviewId, likeCount);
				}
			);
	}
}
