package com.inq.wishhair.wesharewishhair.auth.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inq.wishhair.wesharewishhair.auth.application.dto.response.LoginResponse;
import com.inq.wishhair.wesharewishhair.auth.domain.AuthToken;
import com.inq.wishhair.wesharewishhair.auth.domain.AuthTokenManager;
import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;
import com.inq.wishhair.wesharewishhair.user.domain.Email;
import com.inq.wishhair.wesharewishhair.user.domain.User;
import com.inq.wishhair.wesharewishhair.user.domain.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

	private final TokenManager tokenManager;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthTokenManager authTokenManager;

	@Transactional
	public LoginResponse login(String email, String pw) {
		User user = userRepository.findByEmail(new Email(email))
			.filter(findUser -> passwordEncoder.matches(pw, findUser.getPasswordValue()))
			.orElseThrow(() -> new WishHairException(ErrorCode.LOGIN_FAIL));

		AuthToken authToken = authTokenManager.generate(user.getId());

		tokenManager.synchronizeRefreshToken(user.getId(), authToken.refreshToken());

		return new LoginResponse(authToken.accessToken(), authToken.refreshToken());
	}

	@Transactional
	public void logout(Long userId) {
		tokenManager.deleteToken(userId);
	}
}
