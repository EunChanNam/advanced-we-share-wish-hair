package com.inq.wishhair.wesharewishhair.hairstyle.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inq.wishhair.wesharewishhair.global.annotation.FetchAuthInfo;
import com.inq.wishhair.wesharewishhair.global.dto.response.Success;
import com.inq.wishhair.wesharewishhair.global.resolver.dto.AuthInfo;
import com.inq.wishhair.wesharewishhair.hairstyle.application.WishHairService;
import com.inq.wishhair.wesharewishhair.hairstyle.application.dto.response.WishHairResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hair_styles/wish/")
public class WishHairController {

	private final WishHairService wishHairService;

	@PostMapping(path = "{hairStyleId}")
	public ResponseEntity<Success> executeWish(
		final @PathVariable Long hairStyleId,
		final @FetchAuthInfo AuthInfo authInfo
	) {

		wishHairService.executeWish(hairStyleId, authInfo.userId());

		return ResponseEntity.ok(new Success());
	}

	@DeleteMapping(path = "{hairStyleId}")
	public ResponseEntity<Success> cancelWish(
		final @PathVariable Long hairStyleId,
		final @FetchAuthInfo AuthInfo authInfo
	) {
		wishHairService.cancelWish(hairStyleId, authInfo.userId());

		return ResponseEntity.ok(new Success());
	}

	@GetMapping(path = {"{hairStyleId}"})
	public ResponseEntity<WishHairResponse> checkIsWishing(
		final @PathVariable Long hairStyleId,
		final @FetchAuthInfo AuthInfo authInfo
	) {
		WishHairResponse result = wishHairService.checkIsWishing(hairStyleId, authInfo.userId());
		return ResponseEntity.ok(result);
	}
}
