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

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	@PostMapping
	public ResponseEntity<Success> signUp(@RequestBody SignUpRequest createRequest) {
		Long userId = userService.createUser(createRequest);

		return ResponseEntity
			.created(URI.create("/api/users/" + userId))
			.body(new Success());
	}

	@DeleteMapping
	public ResponseEntity<Success> deleteUser(final @FetchAuthInfo AuthInfo authInfo) {
		userService.deleteUser(authInfo.userId());

		return ResponseEntity.ok(new Success());
	}

	@PatchMapping("/refresh/password")
	public ResponseEntity<Success> refreshPassword(@RequestBody PasswordRefreshRequest request) {

		userService.refreshPassword(request);

		return ResponseEntity.ok(new Success());
	}

	@PatchMapping
	public ResponseEntity<Success> updateUser(@RequestBody UserUpdateRequest request,
		final @FetchAuthInfo AuthInfo authInfo
	) {

		userService.updateUser(authInfo.userId(), request);

		return ResponseEntity.ok(new Success());
	}

	@PatchMapping("/password")
	public ResponseEntity<Success> updatePassword(
		final @RequestBody PasswordUpdateRequest request,
		final @FetchAuthInfo AuthInfo authInfo
	) {
		userService.updatePassword(authInfo.userId(), request);

		return ResponseEntity.ok(new Success());
	}

	@PatchMapping("/face_shape")
	public ResponseEntity<SimpleResponseWrapper<String>> updateFaceShape(
		final @ModelAttribute FaceShapeUpdateRequest request,
		final @FetchAuthInfo AuthInfo authInfo
	) {

		SimpleResponseWrapper<String> result = userService.updateFaceShape(authInfo.userId(), request.getFile());

		return ResponseEntity.ok(result);
	}
}
