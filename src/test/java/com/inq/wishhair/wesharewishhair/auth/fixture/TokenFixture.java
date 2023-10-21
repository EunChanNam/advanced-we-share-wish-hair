package com.inq.wishhair.wesharewishhair.auth.fixture;

import com.inq.wishhair.wesharewishhair.auth.domain.entity.Token;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TokenFixture {

	private static final String TOKEN = "token";
	private static final Long USER_ID = 1L;

	public static Token getFixedToken() {
		return Token.issue(USER_ID, TOKEN);
	}
}
