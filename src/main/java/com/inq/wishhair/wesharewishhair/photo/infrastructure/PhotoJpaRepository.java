package com.inq.wishhair.wesharewishhair.photo.infrastructure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.inq.wishhair.wesharewishhair.photo.domain.Photo;
import com.inq.wishhair.wesharewishhair.photo.domain.PhotoRepository;

public interface PhotoJpaRepository extends PhotoRepository, JpaRepository<Photo, Long> {

	@Modifying
	@Query("delete from Photo p where p.review.id = :reviewId")
	void deleteAllByReview(@Param("reviewId") Long reviewId);

	@Modifying
	@Query("delete from Photo p where p.review.id in :reviewIds")
	void deleteAllByReviews(@Param("reviewIds") List<Long> reviewIds);
}
