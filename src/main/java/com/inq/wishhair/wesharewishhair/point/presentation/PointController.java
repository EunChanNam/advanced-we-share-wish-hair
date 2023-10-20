package com.inq.wishhair.wesharewishhair.point.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inq.wishhair.wesharewishhair.global.annotation.FetchAuthInfo;
import com.inq.wishhair.wesharewishhair.global.dto.response.Success;
import com.inq.wishhair.wesharewishhair.global.resolver.dto.AuthInfo;
import com.inq.wishhair.wesharewishhair.point.application.dto.PointUseRequest;
import com.inq.wishhair.wesharewishhair.point.application.PointService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users/point")
@RequiredArgsConstructor
public class PointController {

	private final PointService pointService;

	@PostMapping("/use")
	public ResponseEntity<Success> usePoint(
		final @RequestBody PointUseRequest request,
		final @FetchAuthInfo AuthInfo authInfo
	) {

		pointService.usePoint(request, authInfo.userId());

		return ResponseEntity.ok(new Success());
	}
}
