package com.inq.wishhair.wesharewishhair.auth.application;

import static com.inq.wishhair.wesharewishhair.global.exception.ErrorCode.*;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inq.wishhair.wesharewishhair.auth.application.utils.RandomGenerator;
import com.inq.wishhair.wesharewishhair.auth.domain.AuthCodeRepository;
import com.inq.wishhair.wesharewishhair.auth.domain.entity.AuthCode;
import com.inq.wishhair.wesharewishhair.auth.event.AuthMailSendEvent;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;
import com.inq.wishhair.wesharewishhair.user.domain.entity.Email;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MailAuthService {

	private final ApplicationEventPublisher eventPublisher;
	private final AuthCodeRepository authCodeRepository;
	private final RandomGenerator randomGenerator;

	public void requestMailAuthorization(final String email) {
		String code = randomGenerator.generateString();

		eventPublisher.publishEvent(new AuthMailSendEvent(new Email(email), code));

		authCodeRepository.findByEmail(email)
			.ifPresentOrElse(
				authCode -> authCode.updateCode(code),
				() -> authCodeRepository.save(new AuthCode(email, code))
			);
	}

	public void checkAuthCode(
		final String email,
		final String authCode
	) {
		authCodeRepository.findByEmail(email)
			.filter(actualAuthCode -> actualAuthCode.getCode().equals(authCode))
			.orElseThrow(() -> new WishHairException(AUTH_INVALID_AUTH_CODE));

		authCodeRepository.deleteByEmail(email);
	}
}
