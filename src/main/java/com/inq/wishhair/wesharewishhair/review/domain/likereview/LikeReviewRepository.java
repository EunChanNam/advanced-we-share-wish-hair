package com.inq.wishhair.wesharewishhair.review.domain.likereview;

import java.util.List;

public interface LikeReviewRepository {

	LikeReview save(LikeReview likeReview);

	Long countByReviewId(Long reviewId);

	void deleteByReviewId(Long reviewId);

	void deleteByUserIdAndReviewId(Long userId, Long reviewId);

	boolean existsByUserIdAndReviewId(Long userId, Long reviewId);

	void deleteByReviewIdIn(List<Long> reviewIds);

	void deleteAll();
}
