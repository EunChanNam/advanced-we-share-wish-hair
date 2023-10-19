package com.inq.wishhair.wesharewishhair.auth.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inq.wishhair.wesharewishhair.auth.presentation.dto.request.LoginRequest;
import com.inq.wishhair.wesharewishhair.auth.application.AuthService;
import com.inq.wishhair.wesharewishhair.auth.application.dto.response.LoginResponse;
import com.inq.wishhair.wesharewishhair.global.annotation.FetchAuthInfo;
import com.inq.wishhair.wesharewishhair.global.dto.response.Success;
import com.inq.wishhair.wesharewishhair.global.resolver.dto.AuthInfo;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(final @RequestBody LoginRequest loginRequest) {
		LoginResponse response = authService.login(loginRequest.email(), loginRequest.pw());
		return ResponseEntity.ok(response);
	}

	@PostMapping("/logout")
	public ResponseEntity<Success> logout(@FetchAuthInfo AuthInfo authInfo) {
		authService.logout(authInfo.userId());
		return ResponseEntity.ok(new Success());
	}
}
