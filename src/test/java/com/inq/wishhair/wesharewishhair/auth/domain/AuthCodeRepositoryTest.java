package com.inq.wishhair.wesharewishhair.auth.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.inq.wishhair.wesharewishhair.auth.domain.entity.AuthCode;
import com.inq.wishhair.wesharewishhair.common.support.RepositoryTestSupport;

@DisplayName("[AuthCodeRepository Test] - Domain Layer")
class AuthCodeRepositoryTest extends RepositoryTestSupport {

	@Autowired
	private AuthCodeRepository authCodeRepository;

	@Test
	@DisplayName("[해당 이메일에 해당되는 인증코드를 삭제한다]")
	void deleteByEmail() {
		//given
		final String email = "email";
		AuthCode authCode = new AuthCode(email, "code");
		authCodeRepository.save(authCode);

		//when
		authCodeRepository.deleteByEmail(email);

		//then
		Optional<AuthCode> actual = authCodeRepository.findByEmail(email);
		assertThat(actual).isNotPresent();
	}

	@Test
	@DisplayName("[해당 이메일을 가진 인증코드를 조회한다]")
	void findByEmail() {
		//given
		final String email = "email";
		AuthCode authCode = new AuthCode(email, "code");
		authCodeRepository.save(authCode);

		//when
		Optional<AuthCode> actual = authCodeRepository.findByEmail(email);

		//then
		assertThat(actual).contains(authCode);
	}
}