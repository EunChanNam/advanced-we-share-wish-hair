package com.inq.wishhair.wesharewishhair.auth.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inq.wishhair.wesharewishhair.auth.application.dto.response.LoginResponse;
import com.inq.wishhair.wesharewishhair.auth.domain.AuthToken;
import com.inq.wishhair.wesharewishhair.auth.domain.AuthTokenManager;
import com.inq.wishhair.wesharewishhair.auth.domain.TokenRepository;
import com.inq.wishhair.wesharewishhair.auth.domain.entity.Token;
import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;
import com.inq.wishhair.wesharewishhair.user.domain.entity.Email;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;
import com.inq.wishhair.wesharewishhair.user.domain.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

	private final UserRepository userRepository;
	private final TokenRepository tokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthTokenManager authTokenManager;

	@Transactional
	public LoginResponse login(
		final String email,
		final String pw
	) {
		User user = userRepository.findByEmail(new Email(email))
			.filter(findUser -> passwordEncoder.matches(pw, findUser.getPasswordValue()))
			.orElseThrow(() -> new WishHairException(ErrorCode.LOGIN_FAIL));

		AuthToken authToken = authTokenManager.generate(user.getId());
		synchronizeRefreshToken(user, authToken);

		return new LoginResponse(authToken.accessToken(), authToken.refreshToken());
	}

	@Transactional
	public void logout(final Long userId) {
		tokenRepository.deleteByUserId(userId);
	}

	private void synchronizeRefreshToken(
		final User user,
		final AuthToken authToken
	) {
		tokenRepository.findByUserId(user.getId())
			.ifPresentOrElse(
				token -> token.updateRefreshToken(authToken.refreshToken()),
				() -> tokenRepository.save(Token.issue(user.getId(), authToken.refreshToken()))
			);
	}
}
