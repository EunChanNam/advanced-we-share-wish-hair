package com.inq.wishhair.wesharewishhair.common.fixture;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthFixture {

	public static final String TOKEN = "token";
	public static final String BEARER = "Bearer ";
	public static final String AUTHORIZATION = "Authorization";
	public static final String ACCESS_TOKEN = BEARER + TOKEN;
}
