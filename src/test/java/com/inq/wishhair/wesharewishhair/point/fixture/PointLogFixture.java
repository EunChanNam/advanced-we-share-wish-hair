package com.inq.wishhair.wesharewishhair.point.fixture;

import org.springframework.test.util.ReflectionTestUtils;

import com.inq.wishhair.wesharewishhair.point.domain.PointLog;
import com.inq.wishhair.wesharewishhair.point.domain.PointType;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;
import com.inq.wishhair.wesharewishhair.user.fixture.UserFixture;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PointLogFixture {

	private static final int DEAL_AMOUNT = 1000;
	private static final int PRE_POINT = 2000;

	private static User getUser() {
		User user = UserFixture.getFixedManUser();
		ReflectionTestUtils.setField(user, "id", 1L);
		return user;
	}

	public static PointLog getChargePointLog() {
		return PointLog.addPointLog(getUser(), PointType.CHARGE, DEAL_AMOUNT, PRE_POINT);
	}

	public static PointLog getUsePointLog() {
		return PointLog.addPointLog(getUser(), PointType.USE, DEAL_AMOUNT, PRE_POINT);
	}
}
