package com.inq.wishhair.wesharewishhair.user.fixture;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.inq.wishhair.wesharewishhair.common.stub.PasswordEncoderStub;
import com.inq.wishhair.wesharewishhair.user.domain.entity.Password;
import com.inq.wishhair.wesharewishhair.user.domain.entity.Sex;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;
 
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserFixture {

	private static final PasswordEncoder PASSWORD_ENCODER = new PasswordEncoderStub();
	private static final String EMAIL = "hello@naver.com";
	private static final String PW = "hello1234@";
	private static final String NAME = "hello";
	private static final String NICKNAME = "hello";

	public static User getFixedManUser() {
		return User.createUser(
			EMAIL,
			Password.encrypt(PW, PASSWORD_ENCODER),
			NAME,
			NICKNAME,
			Sex.MAN
		);
	}

	public static User getFixedWomanUser() {
		return User.createUser(
			EMAIL,
			Password.encrypt(PW, PASSWORD_ENCODER),
			NAME,
			NICKNAME,
			Sex.WOMAN
		);
	}
}
