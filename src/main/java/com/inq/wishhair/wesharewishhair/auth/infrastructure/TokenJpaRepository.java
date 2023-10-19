package com.inq.wishhair.wesharewishhair.auth.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.inq.wishhair.wesharewishhair.auth.domain.Token;
import com.inq.wishhair.wesharewishhair.auth.domain.TokenRepository;

public interface TokenJpaRepository extends TokenRepository, JpaRepository<Token, Long> {

	Optional<Token> findByUserId(Long userId);

	@Query("select t from Token t where t.userId = :userId " +
		"and t.refreshToken = :refreshToken")
	Optional<Token> findByUserIdAndRefreshToken(
		@Param("userId") Long userId,
		@Param("refreshToken") String refreshToken);

	@Modifying
	@Query("delete from Token t where t.userId = :userId")
	void deleteByUserId(@Param("userId") Long userId);

	@Modifying // RTR 정책에 의한 RefreshToken 업데이트
	@Query("update Token SET refreshToken = :refreshToken " +
		"where userId = :userId")
	void updateRefreshTokenByUserId(
		@Param("userId") Long userId,
		@Param("refreshToken") String refreshToken);
}
