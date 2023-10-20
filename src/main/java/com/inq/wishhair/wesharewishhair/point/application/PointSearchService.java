package com.inq.wishhair.wesharewishhair.point.application;

import static com.inq.wishhair.wesharewishhair.user.application.dto.response.UserResponseAssembler.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inq.wishhair.wesharewishhair.global.dto.response.PagedResponse;
import com.inq.wishhair.wesharewishhair.point.domain.PointLog;
import com.inq.wishhair.wesharewishhair.point.domain.PointLogRepository;
import com.inq.wishhair.wesharewishhair.point.application.dto.PointResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointSearchService {

	private final PointLogRepository pointLogRepository;

	public PagedResponse<PointResponse> getPointHistories(
		final Long userId,
		final Pageable pageable
	) {

		Slice<PointLog> result = pointLogRepository.findByUserIdOrderByNew(userId, pageable);
		return toPagedResponse(result);
	}
}
