package com.inq.wishhair.wesharewishhair.point.event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.inq.wishhair.wesharewishhair.review.event.PointChargeEvent;
import com.inq.wishhair.wesharewishhair.point.application.PointService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PointChargeEventListener {

	private final PointService pointService;

	@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
	public void chargePoint(PointChargeEvent event) {
		pointService.chargePoint(event.dealAmount(), event.userId());
	}
}
