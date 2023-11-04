package com.inq.wishhair.wesharewishhair.review.fixture;

import org.springframework.test.util.ReflectionTestUtils;

import com.inq.wishhair.wesharewishhair.review.domain.entity.Review;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReviewFixture {

	public static Review getEmptyReview(Long id) {
		Review review = new Review();
		ReflectionTestUtils.setField(review, "id", id);
		return review;
	}
}
