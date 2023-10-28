package com.inq.wishhair.wesharewishhair.user.domain.entity;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;

@DisplayName("[Password 테스트] - Domain")
class PasswordTest {

	@Nested
	@DisplayName("[Password 를 생성한다]")
	class newPassword {

		@Test
		@DisplayName("[생성에 성공한다]")
		void success() {
			//given
			String password = "hello1234@";

			//when
			Password actual = new Password(password);

			//then
			assertThat(actual.getValue()).isEqualTo(password);
		}

		@ParameterizedTest(name = "{0}")
		@ValueSource(strings = {"hello", "hello1234"})
		@DisplayName("[형식에 맞지않아 생성에 실패한다]")
		void fail(String password) {
			//when
			ThrowingCallable when = () -> new Password(password);

			//then
			assertThatThrownBy(when)
				.isInstanceOf(WishHairException.class)
				.hasMessageContaining(ErrorCode.USER_INVALID_PASSWORD.getMessage());
		}
	}
}