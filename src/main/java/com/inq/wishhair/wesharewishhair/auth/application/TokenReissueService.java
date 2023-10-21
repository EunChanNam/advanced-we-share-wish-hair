package com.inq.wishhair.wesharewishhair.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inq.wishhair.wesharewishhair.auth.application.dto.response.TokenResponse;
import com.inq.wishhair.wesharewishhair.auth.domain.AuthToken;
import com.inq.wishhair.wesharewishhair.auth.domain.AuthTokenManager;
import com.inq.wishhair.wesharewishhair.auth.domain.TokenRepository;
import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenReissueService {

	private final TokenRepository tokenRepository;
	private final AuthTokenManager authTokenManager;

	@Transactional
	public TokenResponse reissueToken(Long userId, String refreshToken) {

		//사용하지 않은 RTR 토큰인지, 존재하는지 확인
		if (!tokenRepository.existsByUserIdAndRefreshToken(userId, refreshToken)) {
			throw new WishHairException(ErrorCode.AUTH_INVALID_TOKEN);
		}

		AuthToken authToken = authTokenManager.generate(userId);

		tokenRepository.updateRefreshTokenByUserId(userId, authToken.refreshToken());

		return new TokenResponse(authToken.accessToken(), authToken.refreshToken());
	}
}
