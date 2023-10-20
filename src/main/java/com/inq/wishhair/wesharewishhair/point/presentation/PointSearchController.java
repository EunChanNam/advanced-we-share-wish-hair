package com.inq.wishhair.wesharewishhair.point.presentation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inq.wishhair.wesharewishhair.global.annotation.FetchAuthInfo;
import com.inq.wishhair.wesharewishhair.global.dto.response.PagedResponse;
import com.inq.wishhair.wesharewishhair.global.resolver.dto.AuthInfo;
import com.inq.wishhair.wesharewishhair.point.application.PointSearchService;
import com.inq.wishhair.wesharewishhair.point.application.dto.PointResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users/point")
@RequiredArgsConstructor
public class PointSearchController {

	private final PointSearchService pointSearchService;

	@GetMapping
	public ResponseEntity<PagedResponse<PointResponse>> findPointHistories(
		final @FetchAuthInfo AuthInfo authInfo,
		final @PageableDefault Pageable pageable
	) {

		return ResponseEntity.ok(pointSearchService.getPointHistories(authInfo.userId(), pageable));
	}
}
