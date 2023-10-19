package com.inq.wishhair.wesharewishhair.hairstyle.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inq.wishhair.wesharewishhair.global.annotation.FetchAuthInfo;
import com.inq.wishhair.wesharewishhair.global.dto.response.PagedResponse;
import com.inq.wishhair.wesharewishhair.global.dto.response.ResponseWrapper;
import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;
import com.inq.wishhair.wesharewishhair.global.resolver.dto.AuthInfo;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.hashtag.Tag;
import com.inq.wishhair.wesharewishhair.hairstyle.service.HairStyleSearchService;
import com.inq.wishhair.wesharewishhair.hairstyle.service.dto.response.HairStyleResponse;
import com.inq.wishhair.wesharewishhair.hairstyle.service.dto.response.HairStyleSimpleResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hair_styles")
public class HairStyleSearchController {

	private final HairStyleSearchService hairStyleSearchService;

	@GetMapping("/recommend")
	public ResponseWrapper<HairStyleResponse> respondRecommendedHairStyle(
		final @RequestParam(defaultValue = "ERROR") List<Tag> tags,
		final @FetchAuthInfo AuthInfo authInfo
	) {
		validateHasTag(tags);

		return hairStyleSearchService.recommendHair(tags, authInfo.userId());
	}

	@GetMapping("/home")
	public ResponseWrapper<HairStyleResponse> findHairStyleByFaceShape(
		final @FetchAuthInfo AuthInfo authInfo
	) {
		return hairStyleSearchService.recommendHairByFaceShape(authInfo.userId());
	}

	@GetMapping("/wish")
	public PagedResponse<HairStyleResponse> findWishHairStyles(
		final @FetchAuthInfo AuthInfo authInfo,
		final @PageableDefault Pageable pageable) {

		return hairStyleSearchService.findWishHairStyles(authInfo.userId(), pageable);
	}

	@GetMapping
	public ResponseWrapper<HairStyleSimpleResponse> findAllHairStyles() {
		return hairStyleSearchService.findAllHairStyle();
	}

	private void validateHasTag(List<Tag> tags) {
		if (tags.get(0).equals(Tag.ERROR)) {
			throw new WishHairException(ErrorCode.RUN_NOT_ENOUGH_TAG);
		}
	}
}
