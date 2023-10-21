package com.inq.wishhair.wesharewishhair.auth.infrastructure.jwt;

import static com.inq.wishhair.wesharewishhair.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;

@DisplayName("[JwtTokenProvider Test] - Infrastructure Layer")
class JwtTokenProviderTest {

	private final String secretKey;
	private final JwtTokenProvider jwtTokenProvider;

	public JwtTokenProviderTest() {
		secretKey = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
		this.jwtTokenProvider = new JwtTokenProvider(secretKey, 10000, 10000);
	}

	@Test
	void createAccessToken() {
		//given
		final Long userId =1L;

		//when
		String actual = jwtTokenProvider.createAccessToken(userId);

		//then
		Long expected = jwtTokenProvider.getPayload(actual);
		assertThat(userId).isEqualTo(expected);
	}

	@Test
	void createRefreshToken() {
		//given
		final Long userId =1L;

		//when
		String actual = jwtTokenProvider.createRefreshToken(userId);

		//then
		Long expected = jwtTokenProvider.getPayload(actual);
		assertThat(userId).isEqualTo(expected);
	}

	@Test
	void getPayload() {
		//given
		final Long userId =1L;
		String token = jwtTokenProvider.createAccessToken(userId);

		//when
		Long actual = jwtTokenProvider.getPayload(token);

		//then
		assertThat(actual).isEqualTo(userId);
	}

	@Nested
	@DisplayName("[토큰을 검증한다]")
	class validateToken {

		@Test
		@DisplayName("[검증을 통과한다]")
		void pass() {
			//given
			String token = jwtTokenProvider.createAccessToken(1L);

			//when
			Executable when = () -> jwtTokenProvider.validateToken(token);

			//then
			assertDoesNotThrow(when);
		}

		@Test
		@DisplayName("[유효기간이 만료되어 검증에 실패한다]")
		void failByExpire() {
			//given
			JwtTokenProvider provider = new JwtTokenProvider(secretKey, 0, 0);
			String token = provider.createRefreshToken(1L);

			//when
			ThrowingCallable when = () -> provider.validateToken(token);

			//then
			assertThatThrownBy(when)
				.isInstanceOf(WishHairException.class)
				.hasMessageContaining(AUTH_EXPIRED_TOKEN.getMessage());
		}

		@Test
		@DisplayName("[잘못된 시크릿키로 인코딩된 토큰여서 실패한다]")
		void failBySecretKey() {
			//given
			final String newSecretKey = secretKey + "b";
			JwtTokenProvider provider = new JwtTokenProvider(newSecretKey, 10000, 10000);
			String token = provider.createRefreshToken(1L);

			//when
			ThrowingCallable when = () -> jwtTokenProvider.validateToken(token);

			//then
			assertThatThrownBy(when)
				.isInstanceOf(WishHairException.class)
				.hasMessageContaining(AUTH_INVALID_TOKEN.getMessage());
		}
	}
}