package com.inq.wishhair.wesharewishhair.review.infrastructure;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.inq.wishhair.wesharewishhair.review.domain.ReviewRepository;
import com.inq.wishhair.wesharewishhair.review.domain.entity.Review;

public interface ReviewJpaRepository extends ReviewRepository, JpaRepository<Review, Long> {

	//review find service - 리뷰 단순 조회
	@Query("select distinct r from Review r " +
		"left outer join fetch r.photos " +
		"where r.id = :id")
	Optional<Review> findWithPhotosById(@Param("id") Long id);

	//회원 탈퇴를 위한 사용자가 작성한 리뷰 조회
	@Query("select distinct r from Review r " +
		"left outer join fetch r.photos " +
		"where r.writer.id = :userId")
	List<Review> findWithPhotosByUserId(@Param("userId") Long userId);

	@Modifying
	@Query("delete from Review r where r.id in :reviewIds")
	void deleteAllByWriter(@Param("reviewIds") List<Long> reviewIds);

	@Query("select case "
		   + "when l.reviewId is null then 0 "
		   + "else count(r.id) end "
		   + "from Review r "
		   + "left join LikeReview l on r.id = l.reviewId "
		   + "where r.id in :reviewIds "
		   + "group by r.id "
		   + "order by r.id")
	List<Integer> countLikeReviewByIdsOrderById(@Param("reviewIds") Set<Long> reviewIds);

	@Modifying
	@Query("update Review r SET r.likeCount = :likeCount where r.id = :id")
	void updateLikeCountById(
		@Param("id") Long id,
		@Param("likeCount") int likeCount
	);
}
