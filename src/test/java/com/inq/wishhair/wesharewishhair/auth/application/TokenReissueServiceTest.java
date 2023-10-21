package com.inq.wishhair.wesharewishhair.auth.application;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.inq.wishhair.wesharewishhair.auth.application.dto.response.TokenResponse;
import com.inq.wishhair.wesharewishhair.auth.domain.AuthToken;
import com.inq.wishhair.wesharewishhair.auth.domain.AuthTokenManager;
import com.inq.wishhair.wesharewishhair.auth.domain.TokenRepository;
import com.inq.wishhair.wesharewishhair.auth.infrastructure.jwt.AuthAuthTokenManagerAdaptor;
import com.inq.wishhair.wesharewishhair.auth.stub.JwtTokenProviderStub;
import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;

@ExtendWith(MockitoExtension.class)
@DisplayName("[TokenReissueService 테스트] - Application Layer")
class TokenReissueServiceTest {

	private final TokenReissueService tokenReissueService;
	private final TokenRepository tokenRepository;
	private final AuthTokenManager authTokenManager;

	public TokenReissueServiceTest() {
		this.tokenRepository = Mockito.mock(TokenRepository.class);
		this.authTokenManager = new AuthAuthTokenManagerAdaptor(new JwtTokenProviderStub());
		this.tokenReissueService = new TokenReissueService(tokenRepository, authTokenManager);
	}

	@Nested
	@DisplayName("토큰을 재발급한다")
	class reissueToken {

		private static final Long USER_ID = 1L;
		private static final String REFRESH_TOKEN = "TOKEN";

		@Test
		@DisplayName("[재발급에 성공한다]")
		void success() {
			//given
			given(tokenRepository.existsByUserIdAndRefreshToken(USER_ID, REFRESH_TOKEN))
				.willReturn(true);

			//when
			TokenResponse actual = tokenReissueService.reissueToken(USER_ID, REFRESH_TOKEN);

			//then
			AuthToken expected = authTokenManager.generate(1L);
			assertAll(
				() -> assertThat(actual.accessToken()).isEqualTo(expected.accessToken()),
				() -> assertThat(actual.refreshToken()).isEqualTo(expected.refreshToken())
			);
		}

		@Test
		@DisplayName("[이미 재발급에 사용된 토큰으로 실패한다]")
		void failByInvalidRefreshToken() {
			//given
			given(tokenRepository.existsByUserIdAndRefreshToken(USER_ID, REFRESH_TOKEN))
				.willReturn(false);

			//when
			ThrowingCallable when = () -> tokenReissueService.reissueToken(USER_ID, REFRESH_TOKEN);

			//then
			assertThatThrownBy(when)
				.isInstanceOf(WishHairException.class)
				.hasMessageContaining(ErrorCode.AUTH_INVALID_TOKEN.getMessage());
		}
	}
}