package com.inq.wishhair.wesharewishhair.review.fixture;

import java.util.List;

import org.springframework.test.util.ReflectionTestUtils;

import com.inq.wishhair.wesharewishhair.hairstyle.domain.HairStyle;
import com.inq.wishhair.wesharewishhair.hairstyle.fixture.HairStyleFixture;
import com.inq.wishhair.wesharewishhair.review.domain.entity.Review;
import com.inq.wishhair.wesharewishhair.review.domain.entity.Score;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;
import com.inq.wishhair.wesharewishhair.user.fixture.UserFixture;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReviewFixture {

	private static final List<String> URLS = List.of("url1", "url2");
	private static final String CONTENTS = "contents";

	public static Review getEmptyReview(Long id) {
		Review review = new Review();
		ReflectionTestUtils.setField(review, "id", id);
		return review;
	}

	public static Review getReview() {
		User user = UserFixture.getFixedManUser();
		HairStyle hairStyle = HairStyleFixture.getWomanHairStyle();

		return Review.createReview(
			user,
			CONTENTS,
			Score.S2H,
			URLS,
			hairStyle
		);
	}

	public static Review getReview(
		HairStyle hairStyle,
		User user
	) {
		return Review.createReview(
			user,
			CONTENTS,
			Score.S2H,
			URLS,
			hairStyle
		);
	}
}
