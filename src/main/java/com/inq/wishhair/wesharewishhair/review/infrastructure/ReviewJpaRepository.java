package com.inq.wishhair.wesharewishhair.review.infrastructure;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import com.inq.wishhair.wesharewishhair.review.domain.ReviewRepository;
import com.inq.wishhair.wesharewishhair.review.domain.entity.Review;

import jakarta.persistence.LockModeType;

public interface ReviewJpaRepository extends ReviewRepository, JpaRepository<Review, Long> {

	//review find service - 리뷰 단순 조회
	@EntityGraph(attributePaths = "photos")
	Optional<Review> findWithPhotosById(Long id);

	//회원 탈퇴를 위한 사용자가 작성한 리뷰 조회
	List<Review> findWithPhotosByWriterId(Long writerId);

	void deleteByIdIn(List<Long> reviewIds);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<Review> findWithLockById(Long id);
}
