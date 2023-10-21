package com.inq.wishhair.wesharewishhair.common.stub;

import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderStub implements PasswordEncoder {

	private static final String ENCODED_PW = "encoded_password";

	@Override
	public String encode(CharSequence rawPassword) {
		return ENCODED_PW;
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return true;
	}
}
