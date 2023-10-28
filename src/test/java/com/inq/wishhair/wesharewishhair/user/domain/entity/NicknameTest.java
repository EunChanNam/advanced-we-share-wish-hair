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

@DisplayName("[Nickname 테스트] - Domain")
class NicknameTest {

	@Nested
	@DisplayName("[Nickname 을 생성한다]")
	class newNickname {

		@Test
		@DisplayName("[생성에 성공한다]")
		void success() {
			//given
			String nickname = "hello";

			//when
			Nickname actual = new Nickname(nickname);

			//then
			assertThat(actual.getValue()).isEqualTo(nickname);
		}

		@ParameterizedTest(name = "{0}")
		@ValueSource(strings = {"hello man", "hello_man", "h"})
		@DisplayName("[형식에 맞지않아 생성에 실패한다]")
		void fail(String nickname) {
			//when
			ThrowingCallable when = () -> new Nickname(nickname);

			//then
			assertThatThrownBy(when)
				.isInstanceOf(WishHairException.class)
				.hasMessageContaining(ErrorCode.USER_INVALID_NICKNAME.getMessage());
		}
	}
}