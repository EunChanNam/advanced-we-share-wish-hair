package com.inq.wishhair.wesharewishhair.review.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.inq.wishhair.wesharewishhair.review.domain.entity.Review;

public interface ReviewQueryRepository {

	//리뷰 단건 조회
	Optional<Review> findReviewById(Long id);

	//전체 리뷰 조회
	Slice<Review> findReviewByPaging(Pageable pageable);

	//좋아요 한 리뷰 조회
	Slice<Review> findReviewByLike(Long userId, Pageable pageable);

	//작성한 리뷰 조회
	Slice<Review> findReviewByUser(Long userId, Pageable pageable);

	//지난달에 작성한 리뷰 조회
	List<Review> findReviewByCreatedDate();

	//헤어스타일의 리뷰 조회
	List<Review> findReviewByHairStyle(Long hairStyleId);
}
