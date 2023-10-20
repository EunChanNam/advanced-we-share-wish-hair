package com.inq.wishhair.wesharewishhair.point.application.dto;

import java.time.LocalDateTime;

import com.inq.wishhair.wesharewishhair.point.domain.PointLog;

public record PointResponse(
	String pointType,
	int dealAmount,
	int point,
	LocalDateTime dealDate
) {
	public PointResponse(final PointLog pointLog) {
		this(
			pointLog.getPointType().getDescription(),
			pointLog.getDealAmount(),
			pointLog.getPoint(),
			pointLog.getCreatedDate()
		);
	}
}
