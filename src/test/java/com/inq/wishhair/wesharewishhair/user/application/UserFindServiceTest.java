package com.inq.wishhair.wesharewishhair.user.application;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.inq.wishhair.wesharewishhair.common.support.MockTestSupport;
import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;
import com.inq.wishhair.wesharewishhair.user.domain.UserRepository;
import com.inq.wishhair.wesharewishhair.user.domain.entity.Email;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;
import com.inq.wishhair.wesharewishhair.user.fixture.UserFixture;

@DisplayName("[UserFindService 테스트] - Application")
class UserFindServiceTest extends MockTestSupport {

	@InjectMocks
	private UserFindService userFindService;
	@Mock
	private UserRepository userRepository;

	@Nested
	@DisplayName("[id 로 유저를 조회한다]")
	class getById {

		private final Long userId = 1L;

		@Test
		@DisplayName("[조회에 성공한다]")
		void success() {
			//given
			User user = UserFixture.getFixedManUser();
			given(userRepository.findById(userId))
				.willReturn(Optional.of(user));

			//when
			User actual = userFindService.findByUserId(userId);

			//then
			assertThat(actual).isEqualTo(user);
		}

		@Test
		@DisplayName("[id 에 맞는 유저가 존재하지 않아 실패한다]")
		void fail() {
			//given
			given(userRepository.findById(userId))
				.willReturn(Optional.empty());

			//when
			ThrowingCallable when = () -> userFindService.findByUserId(userId);

			//then
			assertThatThrownBy(when)
				.isInstanceOf(WishHairException.class)
				.hasMessageContaining(ErrorCode.NOT_EXIST_KEY.getMessage());
		}
	}

	@Nested
	@DisplayName("[email 로 유저를 조회한다]")
	class getByEmail {

		private final Email email = new Email("hello@naver.com");

		@Test
		@DisplayName("[조회에 성공한다]")
		void success() {
			//given
			User user = UserFixture.getFixedManUser();
			given(userRepository.findByEmail(email))
				.willReturn(Optional.of(user));

			//when
			User actual = userFindService.findByEmail(email);

			//then
			assertThat(actual).isEqualTo(user);
		}

		@Test
		@DisplayName("[email 에 맞는 유저가 존재하지 않아 실패한다]")
		void fail() {
			//given
			given(userRepository.findByEmail(email))
				.willReturn(Optional.empty());

			//when
			ThrowingCallable when = () -> userFindService.findByEmail(email);

			//then
			assertThatThrownBy(when)
				.isInstanceOf(WishHairException.class)
				.hasMessageContaining(ErrorCode.USER_NOT_FOUND_BY_EMAIL.getMessage());
		}
	}
}