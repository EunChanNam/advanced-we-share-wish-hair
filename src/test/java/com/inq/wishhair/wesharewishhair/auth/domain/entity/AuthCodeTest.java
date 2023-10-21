package com.inq.wishhair.wesharewishhair.auth.domain.entity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("[AuthCode Test] - Domain Layer")
class AuthCodeTest {

	@Test
	@DisplayName("[인증 코드를 변경한다]")
	void updateCode() {
		//given
		AuthCode authCode = new AuthCode("email", "code");
		final String newCode = "newCode";

		//when
		authCode.updateCode(newCode);

		//then
		assertThat(authCode.getCode()).isEqualTo(newCode);
	}
}