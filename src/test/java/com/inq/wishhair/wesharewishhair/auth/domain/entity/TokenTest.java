package com.inq.wishhair.wesharewishhair.auth.domain.entity;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("[Token Test] - Domain Layer")
class TokenTest {

	@Test
	@DisplayName("[토큰을 생성한다]")
	void issueTest() {
		//given
		final Long userId = 1L;
		final String token = "token";

		//when
		Token actual = Token.issue(userId, token);

		//then
		assertAll(
			() -> assertThat(actual.getUserId()).isEqualTo(userId),
			() -> assertThat(actual.getRefreshToken()).isEqualTo(token)
		);
	}

	@Test
	@DisplayName("[토큰을 변경한다]")
	void updateRefreshTokenTest() {
		//given
		Token token = Token.issue(1L, "token");
		final String newToken = "newToken";

		//when
		token.updateRefreshToken(newToken);

		//then
		assertThat(token.getRefreshToken()).isEqualTo(newToken);
	}
}