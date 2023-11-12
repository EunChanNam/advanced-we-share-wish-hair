package com.inq.wishhair.wesharewishhair.review.domain.entity;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.HairStyle;
import com.inq.wishhair.wesharewishhair.hairstyle.fixture.HairStyleFixture;
import com.inq.wishhair.wesharewishhair.photo.domain.Photo;
import com.inq.wishhair.wesharewishhair.review.fixture.ReviewFixture;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;
import com.inq.wishhair.wesharewishhair.user.fixture.UserFixture;

@DisplayName("[Review 테스트]")
class ReviewTest {

	@Nested
	@DisplayName("[Review 를 생성한다]")
	class createReview {

		@Test
		@DisplayName("[성공적으로 생성한다]")
		void success() {
			//given
			User user = UserFixture.getFixedManUser();
			String contents = "contents";
			Score score = Score.S2H;
			List<String> photoUrls = List.of("url1", "url2");
			HairStyle hairStyle = HairStyleFixture.getWomanHairStyle();

			//when
			Review actual = Review.createReview(
				user,
				contents,
				score,
				photoUrls,
				hairStyle
			);

			//then
			assertAll(
				() -> assertThat(actual.getContentsValue()).isEqualTo(contents),
				() -> assertThat(actual.getScore()).isEqualTo(score),
				() -> assertThat(actual.getWriter()).isEqualTo(user),
				() -> assertThat(actual.getHairStyle()).isEqualTo(hairStyle)
			);
			List<String> imageUrls = actual.getPhotos().stream().map(Photo::getStoreUrl).toList();
			assertThat(imageUrls).containsAll(photoUrls);
		}

		@ParameterizedTest(name = "{0}")
		@ValueSource(strings = {"fail", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"})
		@DisplayName("[Contents 의 길이가 부적합해서 실패한다]")
		void fail1(String contents) {
			//given
			User user = UserFixture.getFixedManUser();
			Score score = Score.S2H;
			List<String> photoUrls = List.of("url1");
			HairStyle hairStyle = HairStyleFixture.getWomanHairStyle();

			//when
			ThrowingCallable when = () -> Review.createReview(
				user,
				contents,
				score,
				photoUrls,
				hairStyle
			);

			//then
			assertThatThrownBy(when)
				.isInstanceOf(WishHairException.class)
				.hasMessageContaining(ErrorCode.CONTENTS_INVALID_LENGTH.getMessage());
		}
	}

	@Test
	@DisplayName("[Review 를 업데이트한다]")
	void updateReview() {
		//given
		Contents contents = new Contents("contents");
		Score score = Score.S1;
		List<String> photoUrls = List.of("url1", "url2");

		Review review = ReviewFixture.getReview();

		//when
		review.updateReview(contents, score, photoUrls);

		//then
		assertAll(
			() -> assertThat(review.getContents()).isEqualTo(contents),
			() -> assertThat(review.getScore()).isEqualTo(score)
		);
		List<String> imageUrls = review.getPhotos().stream().map(Photo::getStoreUrl).toList();
		assertThat(imageUrls).containsAll(photoUrls);
	}
}