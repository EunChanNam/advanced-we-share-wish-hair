package com.inq.wishhair.wesharewishhair.point.domain;

import static com.inq.wishhair.wesharewishhair.global.exception.ErrorCode.*;

import java.util.function.BiFunction;

import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PointType {
	CHARGE(
		"충전",
		(chargeAmount, prePoint) -> {
			if (chargeAmount < 0) {
				throw new WishHairException(POINT_INVALID_POINT_RANGE);
			}
			return prePoint + chargeAmount;
		}),
	USE(
		"사용",
		(useAmount, prePoint) -> {
			int point = prePoint - useAmount;
			if (useAmount < 0) {
				throw new WishHairException(POINT_INVALID_POINT_RANGE);
			}
			if (point < 0) {
				throw new WishHairException(POINT_NOT_ENOUGH);
			}
			return point;
		});

	private final String description;
	private final BiFunction<Integer, Integer, Integer> pointCalculator;

	public String getDescription() {
		return description;
	}

	public int getPoint(final int dealAmount, final int prePoint) {
		return pointCalculator.apply(dealAmount, prePoint);
	}
}
