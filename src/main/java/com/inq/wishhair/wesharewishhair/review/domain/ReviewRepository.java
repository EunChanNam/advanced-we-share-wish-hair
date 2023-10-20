package com.inq.wishhair.wesharewishhair.review.domain;

import java.util.List;
import java.util.Optional;

import com.inq.wishhair.wesharewishhair.review.domain.entity.Review;

public interface ReviewRepository {

	Review save(Review review);

	//review find service - 리뷰 단순 조회
	Optional<Review> findWithPhotosById(Long id);

	//회원 탈퇴를 위한 사용자가 작성한 리뷰 조회
	List<Review> findWithPhotosByUserId(Long userId);

	void deleteAllByWriter(List<Long> reviewIds);

	void delete(Review review);
}
