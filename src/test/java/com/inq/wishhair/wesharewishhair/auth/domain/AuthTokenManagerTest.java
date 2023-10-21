package com.inq.wishhair.wesharewishhair.auth.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.inq.wishhair.wesharewishhair.auth.infrastructure.jwt.AuthAuthTokenManagerAdaptor;
import com.inq.wishhair.wesharewishhair.auth.infrastructure.jwt.JwtTokenProvider;
import com.inq.wishhair.wesharewishhair.auth.stub.JwtTokenProviderStub;

@DisplayName("[AuthTokenManager Test] - Infrastructure Layer")
class AuthTokenManagerTest {

	private final JwtTokenProvider jwtTokenProvider = new JwtTokenProviderStub();
	private final AuthTokenManager authTokenManager = new AuthAuthTokenManagerAdaptor(jwtTokenProvider);

	@Test
	@DisplayName("[인증 토큰을 발행한다]")
	void generate() {
		//when
		AuthToken actual = authTokenManager.generate(1L);

		//then
		AuthToken expected = new AuthToken(
			jwtTokenProvider.createAccessToken(1L),
			jwtTokenProvider.createRefreshToken(1L)
		);

		assertThat(actual).isEqualTo(expected);
	}

	@Test
	@DisplayName("[토큰에서 id 를 추출한다]")
	void getId() {
		//given
		String token = jwtTokenProvider.createRefreshToken(1L);

		//when
		Long actual = authTokenManager.getId(token);

		//then
		Long expected = jwtTokenProvider.getPayload(token);
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	@DisplayName("[토큰을 검증한다]")
	void validateToken() {
		//given
		String token = jwtTokenProvider.createRefreshToken(1L);

		//when
		Executable when = () -> authTokenManager.validateToken(token);

		//then
		assertDoesNotThrow(when);
	}
}