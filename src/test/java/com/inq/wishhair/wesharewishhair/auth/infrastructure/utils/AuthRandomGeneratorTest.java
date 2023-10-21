package com.inq.wishhair.wesharewishhair.auth.infrastructure.utils;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("[AuthRandomGenerator Test] - Infrastructure Layer")
class AuthRandomGeneratorTest {

	private final AuthRandomGenerator authRandomGenerator = new AuthRandomGenerator();

	@Test
	@DisplayName("[1000~9999 사이의 랜덤한 숫자 문자열을 생성한다]")
	void generateString() {
		//when
		String actual = authRandomGenerator.generateString();

		//then
		assertThat(Integer.parseInt(actual)).isBetween(1000, 9999);
	}
}