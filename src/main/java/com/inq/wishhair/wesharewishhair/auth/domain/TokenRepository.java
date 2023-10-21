package com.inq.wishhair.wesharewishhair.auth.domain;

import java.util.Optional;

import com.inq.wishhair.wesharewishhair.auth.domain.entity.Token;

public interface TokenRepository {

	Token save(Token token);

	Optional<Token> findByUserId(Long userId);

	Optional<Token> findByUserIdAndRefreshToken(Long userId, String refreshToken);

	void deleteByUserId(Long userId);

	void updateRefreshTokenByUserId(Long userId, String refreshToken);
}
