package com.inq.wishhair.wesharewishhair.user.fixture;

import com.inq.wishhair.wesharewishhair.user.domain.entity.Sex;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserFixture {

	private static final String EMAIL = "hello@naver.com";
	private static final String PW = "hello1234@";
	private static final String NAME = "hello";
	private static final String NICKNAME = "hello";

	public static User getFixedManUser() {
		return User.of(
			EMAIL,
			PW,
			NAME,
			NICKNAME,
			Sex.MAN
		);
	}

	public static User getFixedWomanUser() {
		return User.of(
			EMAIL,
			PW,
			NAME,
			NICKNAME,
			Sex.WOMAN
		);
	}
}
