package com.inq.wishhair.wesharewishhair.review.fixture;

import java.io.IOException;
import java.util.List;

import org.springframework.test.util.ReflectionTestUtils;

import com.inq.wishhair.wesharewishhair.common.utils.FileMockingUtils;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.HairStyle;
import com.inq.wishhair.wesharewishhair.hairstyle.fixture.HairStyleFixture;
import com.inq.wishhair.wesharewishhair.review.application.dto.request.ReviewCreateRequest;
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

	public static Review getReview(Long id) {
		Review review = getReview();
		ReflectionTestUtils.setField(review, "id", id);
		return review;
	}

	public static Review getReview(Long id, User user) {
		Review review = Review.createReview(
			user,
			CONTENTS,
			Score.S2H,
			URLS,
			HairStyleFixture.getWomanHairStyle()
		);
		ReflectionTestUtils.setField(review, "id", id);

		return review;
	}

	public static Review getReview(HairStyle hairStyle, User user) {
		return Review.createReview(
			user,
			CONTENTS,
			Score.S2H,
			URLS,
			hairStyle
		);
	}

	public static Review getReview(Long id, HairStyle hairStyle, User user) {
		Review review = getReview(hairStyle, user);
		ReflectionTestUtils.setField(review, "id", id);
		return review;
	}

	public static ReviewCreateRequest getReviewCreateRequest() throws IOException {
		return new ReviewCreateRequest(
			CONTENTS,
			Score.S2H,
			List.of(FileMockingUtils.createMockMultipartFile("hello1.jpg")),
			1L
		);
	}
}
