package com.inq.wishhair.wesharewishhair.auth.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inq.wishhair.wesharewishhair.auth.domain.entity.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {

	Optional<Token> findByUserId(Long userId);

	Optional<Token> findByUserIdAndRefreshToken(Long userId, String refreshToken);

	void deleteByUserId(Long userId);

	void updateRefreshTokenByUserId(Long userId, String refreshToken);
}
