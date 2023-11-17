package com.inq.wishhair.wesharewishhair.auth.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inq.wishhair.wesharewishhair.auth.application.TokenReissueService;
import com.inq.wishhair.wesharewishhair.auth.application.dto.response.TokenResponse;
import com.inq.wishhair.wesharewishhair.global.annotation.FetchAuthInfo;
import com.inq.wishhair.wesharewishhair.global.resolver.dto.AuthInfo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Auth API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TokenReissueController {

	private final TokenReissueService tokenReissueService;

	@Operation(summary = "토큰 재발급 API", description = "토큰을 재발급한다")
	@ApiResponse(responseCode = "200", description = "엑세스 토큰 + 리프레쉬 토큰", useReturnTypeSchema = true)
	@PostMapping("/tokens/reissue")
	public ResponseEntity<TokenResponse> reissueToken(@Parameter(hidden = true) @FetchAuthInfo AuthInfo authInfo) {
		TokenResponse response = tokenReissueService.reissueToken(authInfo.userId(), authInfo.token());
		return ResponseEntity.ok(response);
	}
}
