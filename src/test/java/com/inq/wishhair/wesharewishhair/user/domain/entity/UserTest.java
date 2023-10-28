package com.inq.wishhair.wesharewishhair.user.domain.entity;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.hashtag.Tag;
import com.inq.wishhair.wesharewishhair.user.fixture.UserFixture;

@DisplayName("[User 테스트] - Domain")
class UserTest {

	@Nested
	@DisplayName("[User 를 생성한다]")
	class of {

		@Test
		@DisplayName("[생성에 성공한다]")
		void success() {
			//given
			String email = "hello@naver.com";
			String password = "hello1234@";
			String name = "name";
			String nickname = "hello";
			Sex sex = Sex.WOMAN;

			//when
			User user = User.of(
				email,
				password,
				name,
				nickname,
				sex
			);

			//then
			assertAll(
				() -> assertThat(user.getName()).isEqualTo(name),
				() -> assertThat(user.getNicknameValue()).isEqualTo(nickname),
				() -> assertThat(user.getSex()).isEqualTo(sex),
				() -> assertThat(user.getFaceShape()).isNull()
			);
		}

		@ParameterizedTest(name = "{0}")
		@ValueSource(strings = {"hellonaver.com", "hello@navercom", "hello#naver.com"})
		@DisplayName("[이메일 형식이 맞지않아 생성에 실패한다]")
		void failByEmail(String email) {
			//when
			ThrowingCallable when = () -> new Email(email);

			//then
			assertThatThrownBy(when)
				.isInstanceOf(WishHairException.class)
				.hasMessageContaining(ErrorCode.USER_INVALID_EMAIL.getMessage());
		}

		@ParameterizedTest(name = "{0}")
		@ValueSource(strings = {"hello", "hello1234"})
		@DisplayName("[비밀번호 형식에 맞지않아 생성에 실패한다]")
		void failByPassword(String password) {
			//when
			ThrowingCallable when = () -> new Password(password);

			//then
			assertThatThrownBy(when)
				.isInstanceOf(WishHairException.class)
				.hasMessageContaining(ErrorCode.USER_INVALID_PASSWORD.getMessage());
		}

		@ParameterizedTest(name = "{0}")
		@ValueSource(strings = {"hello man", "hello_man", "h"})
		@DisplayName("[닉네임 형식에 맞지않아 생성에 실패한다]")
		void failByNickname(String nickname) {
			//when
			ThrowingCallable when = () -> new Nickname(nickname);

			//then
			assertThatThrownBy(when)
				.isInstanceOf(WishHairException.class)
				.hasMessageContaining(ErrorCode.USER_INVALID_NICKNAME.getMessage());
		}
	}

	@Nested
	@DisplayName("[FaceShape 변경한다]")
	class updateFaceShape {

		@Test
		@DisplayName("[생성에 성공한다]")
		void success() {
			//given
			User user = UserFixture.getFixedManUser();

			//when
			user.updateFaceShape(Tag.ROUND);

			//then
			assertThat(user.getFaceShapeTag()).isEqualTo(Tag.ROUND);
		}

		@Test
		@DisplayName("[얼굴형 태그가 아니어서 실패한다]")
		void fail() {
			//given
			User user = UserFixture.getFixedManUser();
			Tag tag = Tag.BANGS;

			//when
			ThrowingCallable when = () -> user.updateFaceShape(tag);

			//then
			assertThatThrownBy(when)
				.isInstanceOf(WishHairException.class)
				.hasMessageContaining(ErrorCode.USER_TAG_MISMATCH.getMessage());
		}
	}
}