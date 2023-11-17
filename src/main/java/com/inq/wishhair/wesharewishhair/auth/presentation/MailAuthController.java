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
import com.inq.wishhair.wesharewishhair.user.domain.entity.Email;
import com.inq.wishhair.wesharewishhair.user.application.utils.UserValidator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Auth API")
@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class MailAuthController {

	private final UserValidator userValidator;
	private final MailAuthService mailAuthService;

	@Operation(summary = "이메일 중복체크 API", description = "이메일 중복체크를 한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	@PostMapping("/check")
	public ResponseEntity<Success> checkDuplicateEmail(
		@Parameter(description = "이메일") @RequestBody MailRequest request
	) {
		userValidator.validateEmailIsNotDuplicated(new Email(request.email()));

		return ResponseEntity.ok(new Success());
	}

	@Operation(summary = "인증메일 발송 API", description = "인증메일을 발송한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	@PostMapping("/send")
	public ResponseEntity<Success> sendAuthorizationMail(
		@Parameter(description = "이메일") @RequestBody MailRequest mailRequest
	) {
		mailAuthService.requestMailAuthorization(mailRequest.email());

		return ResponseEntity.ok(new Success());
	}

	@Operation(summary = "이메일 인증 API", description = "이메일을 인증한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	@PostMapping("/validate")
	public ResponseEntity<Success> authorizeKey(
		@Parameter(description = "메일 인증 폼") @RequestBody AuthKeyRequest request
	) {
		mailAuthService.checkAuthCode(request.email(), request.authKey());

		return ResponseEntity.ok(new Success());
	}
}
