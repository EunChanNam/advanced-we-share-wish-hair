package com.inq.wishhair.wesharewishhair.auth.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inq.wishhair.wesharewishhair.auth.application.TokenReissueService;
import com.inq.wishhair.wesharewishhair.auth.application.dto.response.TokenResponse;
import com.inq.wishhair.wesharewishhair.global.annotation.FetchAuthInfo;
import com.inq.wishhair.wesharewishhair.global.resolver.dto.AuthInfo;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TokenReissueController {

	private final TokenReissueService tokenReissueService;

	@PostMapping("/token/reissue")
	public ResponseEntity<TokenResponse> reissueToken(
		final @FetchAuthInfo AuthInfo authInfo
	) {
		TokenResponse response = tokenReissueService.reissueToken(authInfo.userId(), authInfo.token());
		return ResponseEntity.ok(response);
	}
}
