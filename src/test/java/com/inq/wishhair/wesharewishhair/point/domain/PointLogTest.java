package com.inq.wishhair.wesharewishhair.point.domain;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;
import com.inq.wishhair.wesharewishhair.user.fixture.UserFixture;

@DisplayName("[PointLog 테스트 - Domain]")
class PointLogTest {

	@Nested
	@DisplayName("[포인트 로그를 추가한다]")
	class addPointLog {

		@Nested
		@DisplayName("[충전 포인트 로그를 생성한다]")
		class charge {

			@Test
			@DisplayName("[성공적으로 생성한다]")
			void success() {
				//given
				User user = UserFixture.getFixedManUser();
				int dealAmount = 1000;
				int prePoint = 100;

				//when
				PointLog actual = PointLog.addPointLog(user, PointType.CHARGE, dealAmount, prePoint);

				//then
				assertAll(
					() -> assertThat(actual.getPoint()).isEqualTo(prePoint + dealAmount),
					() -> assertThat(actual.getDealAmount()).isEqualTo(dealAmount),
					() -> assertThat(actual.getPointType()).isEqualTo(PointType.CHARGE)
				);
			}

			@Test
			@DisplayName("[충전 금액이 0이하여서 실패한다]")
			void fail() {
				//given
				User user = UserFixture.getFixedManUser();
				int dealAmount = -100;
				int prePoint = 100;

				//when
				ThrowingCallable when = () -> PointLog.addPointLog(user, PointType.CHARGE, dealAmount, prePoint);

				//then
				assertThatThrownBy(when)
					.isInstanceOf(WishHairException.class)
					.hasMessageContaining(ErrorCode.POINT_INVALID_POINT_RANGE.getMessage());
			}
		}

		@Nested
		@DisplayName("[사용 포인트 로그를 생성한다]")
		class use {

			@Test
			@DisplayName("[성공적으로 생성한다]")
			void success() {
				//given
				User user = UserFixture.getFixedManUser();
				int dealAmount = 1000;
				int prePoint = 2000;

				//when
				PointLog actual = PointLog.addPointLog(user, PointType.USE, dealAmount, prePoint);

				//then
				assertAll(
					() -> assertThat(actual.getPoint()).isEqualTo(prePoint - dealAmount),
					() -> assertThat(actual.getDealAmount()).isEqualTo(dealAmount),
					() -> assertThat(actual.getPointType()).isEqualTo(PointType.USE)
				);
			}

			@Test
			@DisplayName("[충전 금액이 0이하여서 실패한다]")
			void failByInvalidAmountRange() {
				//given
				User user = UserFixture.getFixedManUser();
				int dealAmount = -100;
				int prePoint = 100;

				//when
				ThrowingCallable when = () -> PointLog.addPointLog(user, PointType.USE, dealAmount, prePoint);

				//then
				assertThatThrownBy(when)
					.isInstanceOf(WishHairException.class)
					.hasMessageContaining(ErrorCode.POINT_INVALID_POINT_RANGE.getMessage());
			}

			@Test
			@DisplayName("[포인트 잔액이 부족해서 실패한다]")
			void failByNotEnoughPoint() {
				//given
				User user = UserFixture.getFixedManUser();
				int dealAmount = 2000;
				int prePoint = 1000;

				//when
				ThrowingCallable when = () -> PointLog.addPointLog(user, PointType.USE, dealAmount, prePoint);

				//then
				assertThatThrownBy(when)
					.isInstanceOf(WishHairException.class)
					.hasMessageContaining(ErrorCode.POINT_NOT_ENOUGH.getMessage());
			}
		}
	}
}