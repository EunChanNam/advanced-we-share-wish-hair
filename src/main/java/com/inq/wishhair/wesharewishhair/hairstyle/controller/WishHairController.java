package com.inq.wishhair.wesharewishhair.hairstyle.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inq.wishhair.wesharewishhair.global.dto.response.Success;
import com.inq.wishhair.wesharewishhair.hairstyle.service.WishHairService;
import com.inq.wishhair.wesharewishhair.hairstyle.service.dto.response.WishHairResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hair_styles/wish/")
public class WishHairController {

	private final WishHairService wishHairService;

	@PostMapping(path = "{hairStyleId}")
	public ResponseEntity<Success> executeWish(
		@PathVariable Long hairStyleId,
		@ExtractPayload Long userId) {

		wishHairService.executeWish(hairStyleId, userId);

		return ResponseEntity.ok(new Success());
	}

	@DeleteMapping(path = "{hairStyleId}")
	public ResponseEntity<Success> cancelWish(@PathVariable Long hairStyleId,
		@ExtractPayload Long userId) {

		wishHairService.cancelWish(hairStyleId, userId);

		return ResponseEntity.ok(new Success());
	}

	@GetMapping(path = {"{hairStyleId}"})
	public ResponseEntity<WishHairResponse> checkIsWishing(@PathVariable Long hairStyleId,
		@ExtractPayload Long userId) {

		WishHairResponse result = wishHairService.checkIsWishing(hairStyleId, userId);
		return ResponseEntity.ok(result);
	}
}
