package com.inq.wishhair.wesharewishhair.point.application.dto;

import com.inq.wishhair.wesharewishhair.user.event.RefundMailSendEvent;

public record PointUseRequest(
	String bankName,
	String accountNumber,
	int dealAmount
) {
	public RefundMailSendEvent toRefundMailEvent(final String userName) {
		return new RefundMailSendEvent(
			userName,
			bankName,
			accountNumber,
			dealAmount
		);
	}
}
