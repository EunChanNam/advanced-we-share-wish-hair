package com.inq.wishhair.wesharewishhair.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inq.wishhair.wesharewishhair.auth.domain.entity.Token;
import com.inq.wishhair.wesharewishhair.auth.domain.TokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenManager {

	private final TokenRepository tokenRepository;

	@Transactional
	public void synchronizeRefreshToken(Long userId, String refreshToken) {
		tokenRepository.findByUserId(userId)
			.ifPresentOrElse(
				token -> token.updateRefreshToken(refreshToken),
				() -> tokenRepository.save(Token.issue(userId, refreshToken))
			);
	}

	public boolean existByUserIdAndRefreshToken(Long userId, String refreshToken) {
		return tokenRepository
			.findByUserIdAndRefreshToken(userId, refreshToken)
			.isPresent();
	}

	@Transactional
	public void deleteToken(Long userId) {
		tokenRepository.deleteByUserId(userId);
	}

	@Transactional
	public void updateRefreshToken(Long userId, String refreshToken) {
		tokenRepository.updateRefreshTokenByUserId(userId, refreshToken);
	}
}
