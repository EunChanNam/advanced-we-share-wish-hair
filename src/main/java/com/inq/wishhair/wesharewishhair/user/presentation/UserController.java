package com.inq.wishhair.wesharewishhair.user.presentation;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inq.wishhair.wesharewishhair.global.annotation.FetchAuthInfo;
import com.inq.wishhair.wesharewishhair.global.dto.response.SimpleResponseWrapper;
import com.inq.wishhair.wesharewishhair.global.dto.response.Success;
import com.inq.wishhair.wesharewishhair.global.resolver.dto.AuthInfo;
import com.inq.wishhair.wesharewishhair.user.presentation.dto.request.FaceShapeUpdateRequest;
import com.inq.wishhair.wesharewishhair.user.presentation.dto.request.PasswordRefreshRequest;
import com.inq.wishhair.wesharewishhair.user.presentation.dto.request.PasswordUpdateRequest;
import com.inq.wishhair.wesharewishhair.user.presentation.dto.request.SignUpRequest;
import com.inq.wishhair.wesharewishhair.user.presentation.dto.request.UserUpdateRequest;
import com.inq.wishhair.wesharewishhair.user.application.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "User API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	@Operation(summary = "회원가입 API", description = "회원가입 한다")
	@ApiResponse(responseCode = "201", useReturnTypeSchema = true)
	@PostMapping
	public ResponseEntity<Success> signUp(
		@Parameter(description = "회원가입 폼") @RequestBody SignUpRequest createRequest
	) {
		Long userId = userService.createUser(createRequest);

		return ResponseEntity
			.created(URI.create("/api/users/" + userId))
			.body(new Success());
	}

	@Operation(summary = "회원 탈퇴 API", description = "회원탈퇴를 한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	@DeleteMapping
	public ResponseEntity<Success> deleteUser(final @Parameter(hidden = true) @FetchAuthInfo AuthInfo authInfo) {
		userService.deleteUser(authInfo.userId());

		return ResponseEntity.ok(new Success());
	}

	@Operation(summary = "비밀번호 갱신 API", description = "비밀번호 찾기로 비밀번호를 갱신한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	@PatchMapping("/refresh/password")
	public ResponseEntity<Success> refreshPassword(
		@Parameter(description = "비밀번호 갱신 폼") @RequestBody PasswordRefreshRequest request
	) {
		userService.refreshPassword(request);

		return ResponseEntity.ok(new Success());
	}

	@Operation(summary = "사용자 정보 수정 API", description = "사용자 정보를 수정한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	@PatchMapping
	public ResponseEntity<Success> updateUser(
		@Parameter(description = "사용자 정보 수정 폼") @RequestBody UserUpdateRequest request,
		@Parameter(hidden = true) @FetchAuthInfo AuthInfo authInfo
	) {
		userService.updateUser(authInfo.userId(), request);

		return ResponseEntity.ok(new Success());
	}

	@Operation(summary = "비밀번호 변경 API", description = "비밀번호를 변경한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	@PatchMapping("/password")
	public ResponseEntity<Success> updatePassword(
		@Parameter(description = "비밀번호 변경 폼") @RequestBody PasswordUpdateRequest request,
		@Parameter(hidden = true) @FetchAuthInfo AuthInfo authInfo
	) {
		userService.updatePassword(authInfo.userId(), request);

		return ResponseEntity.ok(new Success());
	}

	@Operation(summary = "얼굴형 분석 API", description = "얼굴형을 분석한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	@PatchMapping("/face_shape")
	public ResponseEntity<SimpleResponseWrapper<String>> updateFaceShape(
		@Parameter(description = "얼굴형 분석 폼") @ModelAttribute FaceShapeUpdateRequest request,
		@Parameter(hidden = true) @FetchAuthInfo AuthInfo authInfo
	) {
		SimpleResponseWrapper<String> result = userService.updateFaceShape(authInfo.userId(), request.getFile());

		return ResponseEntity.ok(result);
	}
}
