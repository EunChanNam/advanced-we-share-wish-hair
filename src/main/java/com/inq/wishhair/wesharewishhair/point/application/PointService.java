package com.inq.wishhair.wesharewishhair.point.application;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inq.wishhair.wesharewishhair.point.application.dto.PointUseRequest;
import com.inq.wishhair.wesharewishhair.point.domain.PointLog;
import com.inq.wishhair.wesharewishhair.point.domain.PointLogRepository;
import com.inq.wishhair.wesharewishhair.point.domain.PointType;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;
import com.inq.wishhair.wesharewishhair.user.application.UserFindService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointService {

	private final UserFindService userFindService;
	private final ApplicationEventPublisher eventPublisher;
	private final PointLogRepository pointLogRepository;

	@Transactional
	public void usePoint(final PointUseRequest request, final Long userId) {

		User user = userFindService.getById(userId);
		insertPointHistory(PointType.USE, request.dealAmount(), user);

		eventPublisher.publishEvent(request.toRefundMailEvent(user.getName()));
	}

	@Transactional
	public void chargePoint(int dealAmount, Long userId) {
		User user = userFindService.getById(userId);
		insertPointHistory(PointType.CHARGE, dealAmount, user);
	}

	private void insertPointHistory(
		final PointType pointType,
		final int dealAmount,
		final User user
	) {
		pointLogRepository.findByUserOrderByCreatedDateDesc(user)
				.ifPresent(lastPointLog -> {
					PointLog newPointLog = PointLog.addPointLog(
						user,
						pointType,
						dealAmount,
						lastPointLog.getPoint()
					);
					pointLogRepository.save(newPointLog);
				});
	}
}

