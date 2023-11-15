package com.inq.wishhair.wesharewishhair.hairstyle.presentation;

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
import com.inq.wishhair.wesharewishhair.global.resolver.dto.AuthInfo;
import com.inq.wishhair.wesharewishhair.hairstyle.application.HairStyleSearchService;
import com.inq.wishhair.wesharewishhair.hairstyle.application.dto.response.HairStyleResponse;
import com.inq.wishhair.wesharewishhair.hairstyle.application.dto.response.HairStyleSimpleResponse;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.hashtag.Tag;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hair_styles")
public class HairStyleSearchController {

	private final HairStyleSearchService hairStyleSearchService;

	@GetMapping("/recommend")
	public ResponseWrapper<HairStyleResponse> respondRecommendedHairStyle(
		@RequestParam(defaultValue = "ERROR") List<Tag> tags,
		@FetchAuthInfo AuthInfo authInfo
	) {
		return hairStyleSearchService.recommendHair(tags, authInfo.userId());
	}

	@GetMapping("/home")
	public ResponseWrapper<HairStyleResponse> findHairStyleByFaceShape(
		@FetchAuthInfo AuthInfo authInfo
	) {
		return hairStyleSearchService.recommendHairByFaceShape(authInfo.userId());
	}

	@GetMapping("/wish")
	public PagedResponse<HairStyleResponse> findWishHairStyles(
		@FetchAuthInfo AuthInfo authInfo,
		@PageableDefault Pageable pageable) {

		return hairStyleSearchService.findWishHairStyles(authInfo.userId(), pageable);
	}

	@GetMapping
	public ResponseWrapper<HairStyleSimpleResponse> findAllHairStyles() {
		return hairStyleSearchService.findAllHairStyle();
	}
}
