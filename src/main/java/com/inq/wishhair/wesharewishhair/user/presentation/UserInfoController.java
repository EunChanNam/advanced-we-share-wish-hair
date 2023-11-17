package com.inq.wishhair.wesharewishhair.user.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inq.wishhair.wesharewishhair.global.annotation.FetchAuthInfo;
import com.inq.wishhair.wesharewishhair.global.resolver.dto.AuthInfo;
import com.inq.wishhair.wesharewishhair.user.application.UserInfoService;
import com.inq.wishhair.wesharewishhair.user.application.dto.response.MyPageResponse;
import com.inq.wishhair.wesharewishhair.user.application.dto.response.UserInfo;
import com.inq.wishhair.wesharewishhair.user.application.dto.response.UserInformation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "User API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserInfoController {

	private final UserInfoService userInfoService;

	@Operation(summary = "마이페이지 정보 조회 API", description = "마이페이지 정보를 조회한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	@GetMapping("/my_page")
	public ResponseEntity<MyPageResponse> getMyPageInfo(@Parameter(hidden = true) @FetchAuthInfo AuthInfo authInfo) {

		MyPageResponse result = userInfoService.getMyPageInfo(authInfo.userId());

		return ResponseEntity.ok(result);
	}

	@Operation(summary = "사용자 정보 조회 API", description = "사용자 정보 조회한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	@GetMapping("/info")
	public ResponseEntity<UserInformation> getUserInformation(
		@Parameter(hidden = true) @FetchAuthInfo AuthInfo authInfo
	) {

		UserInformation result = userInfoService.getUserInformation(authInfo.userId());

		return ResponseEntity.ok(result);
	}

	@Operation(summary = "간략한 사용자 정보 조회 API", description = "간락한 사용자 정보를 조회한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	@GetMapping("/home_info")
	public ResponseEntity<UserInfo> getUserInfo(@Parameter(hidden = true) @FetchAuthInfo AuthInfo authInfo) {

		UserInfo result = userInfoService.getUserInfo(authInfo.userId());

		return ResponseEntity.ok(result);
	}
}
