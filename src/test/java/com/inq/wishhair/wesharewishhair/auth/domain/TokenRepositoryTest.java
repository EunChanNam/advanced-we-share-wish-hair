package com.inq.wishhair.wesharewishhair.auth.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.inq.wishhair.wesharewishhair.auth.domain.entity.Token;
import com.inq.wishhair.wesharewishhair.common.support.RepositoryTestSupport;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@DisplayName("[TokenRepository Test] - Domain Layer")
class TokenRepositoryTest extends RepositoryTestSupport {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private TokenRepository tokenRepository;

	@Test
	@DisplayName("[해당 유저아이디를 가진 토큰을 조회한다]")
	void findByUserId() {
		//given
		final long userId = 1L;
		Token token = Token.issue(userId, "token");
		tokenRepository.save(token);

		//when
		Optional<Token> actual = tokenRepository.findByUserId(userId);

		//then
		assertThat(actual).contains(token);
	}

	@Test
	@DisplayName("[해당 유저 아이디와 리프래쉬 토큰을 가진 토큰을 조회한다]")
	void findByUserIdAndRefreshToken() {
		//given
		final String refreshToken = "token";
		final long userId = 1L;
		Token token = Token.issue(userId, refreshToken);
		tokenRepository.save(token);

		//when
		Optional<Token> actual = tokenRepository.findByUserIdAndRefreshToken(userId, refreshToken);

		//then
		assertThat(actual).contains(token);
	}

	@Test
	@DisplayName("[해당 유저 아이디를 가진 토큰을 삭제한다]")
	void deleteByUserId() {
		//given
		final long userId = 1L;
		Token token = Token.issue(userId, "token");
		tokenRepository.save(token);

		//when
		tokenRepository.deleteByUserId(userId);

		//then
		Optional<Token> actual = tokenRepository.findByUserId(userId);
		assertThat(actual).isNotPresent();
	}

	@Test
	@DisplayName("[해당 유저 아이디를 가진 토큰의 리프래쉬 토큰을 업데이트한다]")
	void updateRefreshTokenByUserId() {
		//given
		final long userId = 1L;
		Token token = Token.issue(userId, "token");
		tokenRepository.save(token);

		final String newRefreshToken = "refreshToken";

		//when
		tokenRepository.updateRefreshTokenByUserId(userId, newRefreshToken);
		entityManager.flush();
		entityManager.clear();

		//then
		Optional<Token> actual = tokenRepository.findByUserId(userId);
		assertThat(actual).isPresent();
		assertThat(actual.get().getRefreshToken()).isEqualTo(newRefreshToken);
	}
}