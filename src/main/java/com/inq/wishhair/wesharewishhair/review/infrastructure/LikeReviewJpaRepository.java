package com.inq.wishhair.wesharewishhair.review.infrastructure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.inq.wishhair.wesharewishhair.review.domain.likereview.LikeReview;
import com.inq.wishhair.wesharewishhair.review.domain.likereview.LikeReviewRepository;

public interface LikeReviewJpaRepository extends LikeReviewRepository, JpaRepository<LikeReview, Long> {

	Long countByReviewId(Long reviewId);

	void deleteByReviewId(Long reviewId);

	void deleteByUserIdAndReviewId(Long userId, Long reviewId);

	boolean existsByUserIdAndReviewId(Long userId, Long reviewId);

	void deleteByReviewIdIn(@Param("reviewIds") List<Long> reviewIds);

	void deleteAll();
}
