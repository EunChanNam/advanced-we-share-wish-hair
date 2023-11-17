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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "HairStyle API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hair_styles/wish/")
public class WishHairController {

	private final WishHairService wishHairService;

	@Operation(summary = "찜 API", description = "찜를 한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	@PostMapping(path = "{hairStyleId}")
	public ResponseEntity<Success> executeWish(
		@Parameter(description = "헤어스타일 아이디") @PathVariable Long hairStyleId,
		@Parameter(hidden = true) @FetchAuthInfo AuthInfo authInfo
	) {
		wishHairService.executeWish(hairStyleId, authInfo.userId());

		return ResponseEntity.ok(new Success());
	}

	@Operation(summary = "찜 취소 API", description = "찜를 취소한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	@DeleteMapping(path = "{hairStyleId}")
	public ResponseEntity<Success> cancelWish(
		@Parameter(description = "헤어스타일 아이디") @PathVariable Long hairStyleId,
		@Parameter(hidden = true) @FetchAuthInfo AuthInfo authInfo
	) {
		wishHairService.cancelWish(hairStyleId, authInfo.userId());

		return ResponseEntity.ok(new Success());
	}

	@Operation(summary = "헤어스타일 찜 확인 API", description = "찜를 취소한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	@GetMapping(path = {"{hairStyleId}"})
	public ResponseEntity<WishHairResponse> checkIsWishing(
		@Parameter(description = "헤어스타일 아이디") @PathVariable Long hairStyleId,
		@Parameter(hidden = true) @FetchAuthInfo AuthInfo authInfo
	) {
		WishHairResponse result = wishHairService.checkIsWishing(hairStyleId, authInfo.userId());
		return ResponseEntity.ok(result);
	}
}
