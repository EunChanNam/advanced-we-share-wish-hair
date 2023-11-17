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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@io.swagger.v3.oas.annotations.tags.Tag(name = "HairStyle API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hair_styles")
public class HairStyleSearchController {

	private final HairStyleSearchService hairStyleSearchService;

	@Operation(summary = "헤어스타일 추천 API", description = "헤어스타일 추천을 한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	@GetMapping("/recommend")
	public ResponseWrapper<HairStyleResponse> respondRecommendedHairStyle(
		@Parameter(description = "태그 정보") @RequestParam(defaultValue = "ERROR") List<Tag> tags,
		@Parameter(hidden = true) @FetchAuthInfo AuthInfo authInfo
	) {
		return hairStyleSearchService.recommendHair(tags, authInfo.userId());
	}

	@Operation(summary = "얼굴형에 맞는 헤어스타일 추천 API", description = "얼굴형 정보로만 헤어스타일을 추천한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	@GetMapping("/home")
	public ResponseWrapper<HairStyleResponse> findHairStyleByFaceShape(
		@Parameter(hidden = true) @FetchAuthInfo AuthInfo authInfo
	) {
		return hairStyleSearchService.recommendHairByFaceShape(authInfo.userId());
	}

	@Operation(summary = "찜한 헤어스타일 조회 API", description = "찜한 헤어스타일을 조회한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	@GetMapping("/wish")
	public PagedResponse<HairStyleResponse> findWishHairStyles(
		@Parameter(hidden = true) @FetchAuthInfo AuthInfo authInfo,
		@Parameter(description = "페이징 정보") @PageableDefault Pageable pageable) {

		return hairStyleSearchService.findWishHairStyles(authInfo.userId(), pageable);
	}

	@Operation(summary = "모든 헤어스타일 API", description = "모든 헤어스타일의 간략한 정보를 조회한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	@GetMapping
	public ResponseWrapper<HairStyleSimpleResponse> findAllHairStyles() {
		return hairStyleSearchService.findAllHairStyle();
	}
}
