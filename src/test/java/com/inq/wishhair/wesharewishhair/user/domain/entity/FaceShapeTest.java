package com.inq.wishhair.wesharewishhair.user.domain.entity;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.hashtag.Tag;

@DisplayName("[FaceShape 테스트] - Domain")
class FaceShapeTest {

	@Nested
	@DisplayName("[FaceShape 을 생성한다]")
	class newEmail {

		@Test
		@DisplayName("[생성에 성공한다]")
		void success() {
			//given
			Tag tag = Tag.ROUND;

			//when
			FaceShape actual = new FaceShape(tag);

			//then
			assertThat(actual.getTag()).isEqualTo(tag);
		}

		@Test
		@DisplayName("[얼굴형 태그가 아니어서 실패한다]")
		void fail() {
			//given
			Tag tag = Tag.BANGS;

			//when
			ThrowingCallable when = () -> new FaceShape(tag);

			//then
			assertThatThrownBy(when)
				.isInstanceOf(WishHairException.class)
				.hasMessageContaining(ErrorCode.USER_TAG_MISMATCH.getMessage());
		}
	}
}