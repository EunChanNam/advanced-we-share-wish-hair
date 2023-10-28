package com.inq.wishhair.wesharewishhair.user.domain.entity;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;

@DisplayName("[Email 테스트] - Domain")
class EmailTest {

	@Nested
	@DisplayName("[Email 을 생성한다]")
	class newEmail {

		@Test
		@DisplayName("[생성에 성공한다]")
		void success() {
			//given
			String email = "hello@naver.com";

			//when
			Email actual = new Email(email);

			//then
			assertThat(actual.getValue()).isEqualTo(email);
		}

		@ParameterizedTest(name = "{0}")
		@ValueSource(strings = {"hellonaver.com", "hello@navercom", "hello#naver.com"})
		@DisplayName("[이메일 형식이 맞지않아 생성에 실패한다]")
		void fail(String email) {
			//when
			ThrowingCallable when = () -> new Email(email);

			//then
			assertThatThrownBy(when)
				.isInstanceOf(WishHairException.class)
				.hasMessageContaining(ErrorCode.USER_INVALID_EMAIL.getMessage());
		}
	}
}