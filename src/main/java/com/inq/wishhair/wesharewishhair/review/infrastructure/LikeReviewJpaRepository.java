package com.inq.wishhair.wesharewishhair.review.infrastructure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.inq.wishhair.wesharewishhair.review.domain.likereview.LikeReview;
import com.inq.wishhair.wesharewishhair.review.domain.likereview.LikeReviewRepository;

public interface LikeReviewJpaRepository extends LikeReviewRepository, JpaRepository<LikeReview, Long> {

	@Query("select count(l.id) from LikeReview l where l.reviewId = :reviewId")
	Long countByReviewId(@Param("reviewId") Long reviewId);

	@Modifying
	@Query("delete from LikeReview l where l.reviewId = :reviewId")
	void deleteAllByReview(@Param("reviewId") Long reviewId);

	@Modifying
	@Query("delete from LikeReview l where l.userId = :userId and l.reviewId = :reviewId")
	int deleteByUserIdAndReviewId(@Param("userId") Long userId,
		@Param("reviewId") Long reviewId);

	boolean existsByUserIdAndReviewId(Long userId, Long reviewId);

	@Modifying
	@Query("delete from LikeReview l where l.reviewId in :reviewIds")
	void deleteAllByReviews(@Param("reviewIds") List<Long> reviewIds);
}
