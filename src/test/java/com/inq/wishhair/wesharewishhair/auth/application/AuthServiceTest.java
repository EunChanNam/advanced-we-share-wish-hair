package com.inq.wishhair.wesharewishhair.auth.application;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.inq.wishhair.wesharewishhair.auth.application.dto.response.LoginResponse;
import com.inq.wishhair.wesharewishhair.auth.domain.AuthToken;
import com.inq.wishhair.wesharewishhair.auth.domain.AuthTokenManager;
import com.inq.wishhair.wesharewishhair.auth.domain.TokenRepository;
import com.inq.wishhair.wesharewishhair.auth.domain.entity.Token;
import com.inq.wishhair.wesharewishhair.auth.fixture.TokenFixture;
import com.inq.wishhair.wesharewishhair.auth.stub.AuthTokenMangerStub;
import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;
import com.inq.wishhair.wesharewishhair.user.domain.UserRepository;
import com.inq.wishhair.wesharewishhair.user.domain.entity.Email;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;
import com.inq.wishhair.wesharewishhair.user.fixture.UserFixture;

@DisplayName("[AuthService Test] - Application Layer")
class AuthServiceTest {

	private static final String EMAIL = "hello@naver.com";
	private static final String PW = "hello1234@";
	private static final User USER = UserFixture.getFixedManUser();

	private final AuthService authService;
	private final UserRepository userRepository;
	private final TokenRepository tokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthTokenManager authTokenManager;

	public AuthServiceTest() {
		this.userRepository = Mockito.mock(UserRepository.class);
		this.tokenRepository = Mockito.mock(TokenRepository.class);
		this.passwordEncoder = Mockito.mock(PasswordEncoder.class);
		this.authTokenManager = new AuthTokenMangerStub();
		this.authService = new AuthService(
			userRepository,
			tokenRepository,
			passwordEncoder,
			authTokenManager
		);
	}

	@Nested
	@DisplayName("로그인을 한다")
	class login {

		@Nested
		@DisplayName("[로그인에 성공한다]")
		class loginSuccess {

			public loginSuccess() {
				given(userRepository.findByEmail(new Email(EMAIL)))
					.willReturn(Optional.of(USER));

				given(passwordEncoder.matches(PW, USER.getPasswordValue()))
					.willReturn(true);
			}

			@Test
			@DisplayName("[토큰을 가지고있는 유저로 기존 토큰의 RefreshToken 을 업데이트한다]")
			void updateRefreshToken() {
				//given
				Token token = TokenFixture.getFixedToken();
				given(tokenRepository.findByUserId(null))
					.willReturn(Optional.of(token));

				//when
				LoginResponse actual = authService.login(EMAIL, PW);

				//then
				assertLoginResponse(actual);
				assertThat(token.getRefreshToken()).isEqualTo(actual.refreshToken());
			}

			@Test
			@DisplayName("[토큰이 없는 유저로 새로운 토큰을 생성한다]")
			void createNewToken() {
				//given
				given(tokenRepository.findByUserId(null))
					.willReturn(Optional.empty());

				//when
				LoginResponse actual = authService.login(EMAIL, PW);

				//then
				assertLoginResponse(actual);
				verify(tokenRepository, times(1)).save(any(Token.class));
			}

			private void assertLoginResponse(LoginResponse actual) {
				AuthToken expected = authTokenManager.generate(1L);
				assertAll(
					() -> assertThat(actual.accessToken()).isEqualTo(expected.accessToken()),
					() -> assertThat(actual.refreshToken()).isEqualTo(expected.refreshToken())
				);
			}
		}

		@Test
		@DisplayName("[잘못된 이메일로 로그인에 실패한다]")
		void failByInvalidEmail() {
			//given
			given(userRepository.findByEmail(new Email(EMAIL)))
				.willReturn(Optional.empty());

			//when
			ThrowingCallable when = () -> authService.login(EMAIL, PW);

			//then
			assertThatThrownBy(when)
				.isInstanceOf(WishHairException.class)
				.hasMessageContaining(ErrorCode.LOGIN_FAIL.getMessage());
		}

		@Test
		@DisplayName("[잘못된 비밀번호로 로그인에 실패한다]")
		void failByInvalidPw() {
			//given
			given(userRepository.findByEmail(new Email(EMAIL)))
				.willReturn(Optional.of(USER));

			given(passwordEncoder.matches(PW, USER.getPasswordValue()))
				.willReturn(false);

			//when
			ThrowingCallable when = () -> authService.login(EMAIL, PW);

			//then
			assertThatThrownBy(when)
				.isInstanceOf(WishHairException.class)
				.hasMessageContaining(ErrorCode.LOGIN_FAIL.getMessage());
		}
	}

	@Test
	@DisplayName("[로그아웃을 한다]")
	void logout() {
		//when
		Executable when = () -> authService.logout(1L);

		//then
		assertDoesNotThrow(when);
	}
}