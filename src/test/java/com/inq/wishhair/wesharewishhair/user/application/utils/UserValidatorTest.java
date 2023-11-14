package com.inq.wishhair.wesharewishhair.user.application.utils;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;
import com.inq.wishhair.wesharewishhair.user.domain.UserRepository;
import com.inq.wishhair.wesharewishhair.user.domain.entity.Email;
import com.inq.wishhair.wesharewishhair.user.domain.entity.Nickname;

@DisplayName("[UserValidator 테스트] - Application")
class UserValidatorTest {

	private final UserValidator userValidator;
	private final UserRepository userRepository;

	public UserValidatorTest() {
		this.userRepository = Mockito.mock(UserRepository.class);
		this.userValidator = new UserValidator(userRepository);
	}

	@Nested
	@DisplayName("[닉네임 중복 검증을 한다]")
	class validateNicknameIsNotDuplicated {

		@Test
		@DisplayName("[중복이 아니어서 검증에 통과한다]")
		void success() {
			//given
			Nickname nickname = new Nickname("nickname");
			given(userRepository.existsByNickname(any(Nickname.class)))
				.willReturn(false);

			//when
			Executable when = () -> userValidator.validateNicknameIsNotDuplicated(nickname);

			//then
			assertDoesNotThrow(when);
		}

		@Test
		@DisplayName("[중복이어서 실패한다]")
		void fail() {
			//given
			Nickname nickname = new Nickname("nickname");
			given(userRepository.existsByNickname(any(Nickname.class)))
				.willReturn(true);

			//when
			ThrowingCallable when = () -> userValidator.validateNicknameIsNotDuplicated(nickname);

			//then
			assertThatThrownBy(when)
				.isInstanceOf(WishHairException.class)
				.hasMessageContaining(ErrorCode.USER_DUPLICATED_NICKNAME.getMessage());
		}
	}

	@Nested
	@DisplayName("[이메일 중복 검증을 한다]")
	class validateEmailIsNotDuplicated {

		@Test
		@DisplayName("[중복이 아니어서 검증에 통과한다]")
		void success() {
			//given
			Email email = new Email("hello@naver.com");
			given(userRepository.existsByEmail(any(Email.class)))
				.willReturn(false);

			//when
			Executable when = () -> userValidator.validateEmailIsNotDuplicated(email);

			//then
			assertDoesNotThrow(when);
		}

		@Test
		@DisplayName("[중복이어서 실패한다]")
		void fail() {
			//given
			Email email = new Email("hello@naver.com");
			given(userRepository.existsByEmail(any(Email.class)))
				.willReturn(true);

			//when
			ThrowingCallable when = () -> userValidator.validateEmailIsNotDuplicated(email);

			//then
			assertThatThrownBy(when)
				.isInstanceOf(WishHairException.class)
				.hasMessageContaining(ErrorCode.USER_DUPLICATED_EMAIL.getMessage());
		}
	}
}