package com.inq.wishhair.wesharewishhair.point.application;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;
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

	private void saveNewPointLog(
		User user,
		PointType pointType,
		int dealAmount,
		int prePoint
	) {
		PointLog newPointLog = PointLog.addPointLog(
			user,
			pointType,
			dealAmount,
			prePoint
		);
		pointLogRepository.save(newPointLog);
	}

	@Transactional
	public boolean usePoint(final PointUseRequest request, final Long userId) {

		User user = userFindService.getById(userId);

		pointLogRepository.findByUserOrderByCreatedDateDesc(user)
			.ifPresentOrElse(
				lastPointLog -> saveNewPointLog(
					user,
					PointType.USE,
					request.dealAmount(),
					lastPointLog.getPoint()
				),
				() -> {
					throw new WishHairException(ErrorCode.POINT_NOT_ENOUGH);
				});

		eventPublisher.publishEvent(request.toRefundMailEvent(user.getName()));
		return true;
	}

	@Transactional
	public boolean chargePoint(int dealAmount, Long userId) {
		User user = userFindService.getById(userId);
		pointLogRepository.findByUserOrderByCreatedDateDesc(user)
			.ifPresentOrElse(
				lastPointLog -> saveNewPointLog(
					user,
					PointType.CHARGE,
					dealAmount,
					lastPointLog.getPoint()
				),
				() -> saveNewPointLog(
					user,
					PointType.CHARGE,
					dealAmount,
					0
				)
			);

		return true;
	}
}
