package com.inq.wishhair.wesharewishhair.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inq.wishhair.wesharewishhair.global.annotation.FetchAuthInfo;
import com.inq.wishhair.wesharewishhair.global.resolver.dto.AuthInfo;
import com.inq.wishhair.wesharewishhair.user.service.UserInfoService;
import com.inq.wishhair.wesharewishhair.user.service.dto.response.MyPageResponse;
import com.inq.wishhair.wesharewishhair.user.service.dto.response.UserInfo;
import com.inq.wishhair.wesharewishhair.user.service.dto.response.UserInformation;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserInfoController {

	private final UserInfoService userInfoService;

	@GetMapping("/my_page")
	public ResponseEntity<MyPageResponse> getMyPageInfo(final @FetchAuthInfo AuthInfo authInfo) {

		MyPageResponse result = userInfoService.getMyPageInfo(authInfo.userId());

		return ResponseEntity.ok(result);
	}

	@GetMapping("/info")
	public ResponseEntity<UserInformation> getUserInformation(
		final @FetchAuthInfo AuthInfo authInfo
	) {

		UserInformation result = userInfoService.getUserInformation(authInfo.userId());

		return ResponseEntity.ok(result);
	}

	@GetMapping("/home_info")
	public ResponseEntity<UserInfo> getUserInfo(final @FetchAuthInfo AuthInfo authInfo) {

		UserInfo result = userInfoService.getUserInfo(authInfo.userId());

		return ResponseEntity.ok(result);
	}
}
