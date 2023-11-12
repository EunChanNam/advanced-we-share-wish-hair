package com.inq.wishhair.wesharewishhair.review.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inq.wishhair.wesharewishhair.global.utils.RedisUtils;
import com.inq.wishhair.wesharewishhair.review.application.dto.response.LikeReviewResponse;
import com.inq.wishhair.wesharewishhair.review.domain.likereview.LikeReview;
import com.inq.wishhair.wesharewishhair.review.domain.likereview.LikeReviewRepository;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeReviewService {

	private final LikeReviewRepository likeReviewRepository;
	private final RedisUtils redisUtils;

	private Long updateLikeCountFromRedis(Long reviewId) {
		Long likeCount = likeReviewRepository.countByReviewId(reviewId);
		redisUtils.setData(reviewId, likeCount);
		return likeCount;
	}

	@Transactional
	public boolean executeLike(Long reviewId, Long userId) {
		try {
			likeReviewRepository.save(LikeReview.addLike(userId, reviewId));
		} catch (EntityExistsException e) {
			return false;
		}

		//락을 걸지않고 값이없으면 좋아요 개수를 로드해서 반영 기능 추가
		redisUtils.getData(reviewId)
			.ifPresentOrElse(
				likeCount -> redisUtils.increaseData(reviewId),
				() -> updateLikeCountFromRedis(reviewId)
			);

		return true;
	}

	@Transactional
	public boolean cancelLike(Long reviewId, Long userId) {
		likeReviewRepository.deleteByUserIdAndReviewId(userId, reviewId);

		redisUtils.getData(reviewId)
			.ifPresentOrElse(
				likeCount -> redisUtils.decreaseData(reviewId),
				() -> updateLikeCountFromRedis(reviewId)
			);

		return true;
	}

	public LikeReviewResponse checkIsLiking(Long userId, Long reviewId) {
		boolean isLiking = likeReviewRepository.existsByUserIdAndReviewId(userId, reviewId);
		return new LikeReviewResponse(isLiking);
	}

	public Long getLikeCount(Long reviewId) {
		return redisUtils.getData(reviewId)
			.orElse(updateLikeCountFromRedis(reviewId));
	}

	public List<Long> getLikeCounts(List<Long> reviewIds) {
		return reviewIds.stream()
			.map(this::getLikeCount)
			.toList();
	}

}
