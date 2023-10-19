package com.inq.wishhair.wesharewishhair.user.service;

import static com.inq.wishhair.wesharewishhair.user.service.dto.response.UserResponseAssembler.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inq.wishhair.wesharewishhair.global.dto.response.PagedResponse;
import com.inq.wishhair.wesharewishhair.user.domain.point.PointHistory;
import com.inq.wishhair.wesharewishhair.user.domain.point.PointSearchRepository;
import com.inq.wishhair.wesharewishhair.user.service.dto.response.PointResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointSearchService {

	private final PointSearchRepository pointSearchRepository;

	public PagedResponse<PointResponse> findPointHistories(Long userId, Pageable pageable) {

		Slice<PointHistory> result = pointSearchRepository.findByUserIdOrderByNew(userId, pageable);
		return toPagedResponse(result);
	}
}
