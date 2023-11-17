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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Auth API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@Operation(summary = "로그인 API", description = "로그인을 한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(final @RequestBody LoginRequest loginRequest) {
		LoginResponse response = authService.login(loginRequest.email(), loginRequest.pw());
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "로그아웃 API", description = "로그아웃을 한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	@PostMapping("/logout")
	public ResponseEntity<Success> logout(@Parameter(hidden = true) @FetchAuthInfo AuthInfo authInfo) {
		authService.logout(authInfo.userId());
		return ResponseEntity.ok(new Success());
	}
}
