package com.inq.wishhair.wesharewishhair.point.presentation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inq.wishhair.wesharewishhair.global.annotation.FetchAuthInfo;
import com.inq.wishhair.wesharewishhair.global.dto.response.PagedResponse;
import com.inq.wishhair.wesharewishhair.global.dto.response.Success;
import com.inq.wishhair.wesharewishhair.global.resolver.dto.AuthInfo;
import com.inq.wishhair.wesharewishhair.point.application.PointSearchService;
import com.inq.wishhair.wesharewishhair.point.application.dto.PointResponse;
import com.inq.wishhair.wesharewishhair.point.application.dto.PointUseRequest;
import com.inq.wishhair.wesharewishhair.point.application.PointService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Point API")
@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
public class PointController {

	private final PointService pointService;
	private final PointSearchService pointSearchService;

	@Operation(summary = "포인트 사용 API", description = "포인트를 사용한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	@PostMapping("/use")
	public ResponseEntity<Success> usePoint(
		@Parameter(description = "포인트 사용 폼") @RequestBody PointUseRequest request,
		@Parameter(hidden = true) @FetchAuthInfo AuthInfo authInfo
	) {
		pointService.usePoint(request, authInfo.userId());

		return ResponseEntity.ok(new Success());
	}

	@Operation(summary = "포인트 내역 조회 API", description = "포인트 내역을 조회한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	@GetMapping
	public ResponseEntity<PagedResponse<PointResponse>> findPointHistories(
		@Parameter(hidden = true) @FetchAuthInfo AuthInfo authInfo,
		@Parameter(description = "페이징 정보") @PageableDefault Pageable pageable
	) {
		return ResponseEntity.ok(pointSearchService.getPointHistories(authInfo.userId(), pageable));
	}
}
