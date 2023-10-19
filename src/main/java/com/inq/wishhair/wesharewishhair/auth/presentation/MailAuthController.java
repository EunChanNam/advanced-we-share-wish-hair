package com.inq.wishhair.wesharewishhair.auth.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inq.wishhair.wesharewishhair.auth.application.MailAuthService;
import com.inq.wishhair.wesharewishhair.auth.presentation.dto.request.AuthKeyRequest;
import com.inq.wishhair.wesharewishhair.auth.presentation.dto.request.MailRequest;
import com.inq.wishhair.wesharewishhair.global.dto.response.Success;
import com.inq.wishhair.wesharewishhair.user.domain.Email;
import com.inq.wishhair.wesharewishhair.user.service.UserValidator;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class MailAuthController {

	private final UserValidator userValidator;
	private final MailAuthService mailAuthService;

	@PostMapping("/check")
	public ResponseEntity<Success> checkDuplicateEmail(
		final @RequestBody MailRequest request
	) {
		userValidator.validateEmailIsNotDuplicated(new Email(request.email()));

		return ResponseEntity.ok(new Success());
	}

	@PostMapping("/send")
	public ResponseEntity<Success> sendAuthorizationMail(
		final @RequestBody MailRequest mailRequest
	) {
		mailAuthService.requestMailAuthorization(mailRequest.email());

		return ResponseEntity.ok(new Success());
	}

	@PostMapping("/validate")
	public ResponseEntity<Success> authorizeKey(
		final @RequestBody AuthKeyRequest authKeyRequest
	) {
		mailAuthService.requestMailAuthorization(authKeyRequest.authKey());

		return ResponseEntity.ok(new Success());
	}
}
