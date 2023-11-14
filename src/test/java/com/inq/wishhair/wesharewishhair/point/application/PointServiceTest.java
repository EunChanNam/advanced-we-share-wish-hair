package com.inq.wishhair.wesharewishhair.point.application;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;

import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;
import com.inq.wishhair.wesharewishhair.point.domain.PointLog;
import com.inq.wishhair.wesharewishhair.point.domain.PointLogRepository;
import com.inq.wishhair.wesharewishhair.point.fixture.PointLogFixture;
import com.inq.wishhair.wesharewishhair.user.application.UserFindService;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;
import com.inq.wishhair.wesharewishhair.user.event.RefundMailSendEvent;
import com.inq.wishhair.wesharewishhair.user.fixture.UserFixture;

@DisplayName("[PointLogService 테스트] - Application")
class PointServiceTest {

	private final PointService pointService;
	private final UserFindService userFindService;
	private final ApplicationEventPublisher eventPublisher;
	private final PointLogRepository pointLogRepository;

	public PointServiceTest() {
		this.userFindService = Mockito.mock(UserFindService.class);
		this.eventPublisher = Mockito.mock(ApplicationEventPublisher.class);
		this.pointLogRepository = Mockito.mock(PointLogRepository.class);
		this.pointService = new PointService(
			userFindService, eventPublisher, pointLogRepository
		);
	}

	@Nested
	@DisplayName("[포인트를 사용한다]")
	class usePoint {

		@Test
		@DisplayName("[성공적으로 사용한다]")
		void success() {
			//given
			User user = UserFixture.getFixedManUser(1L);
			given(userFindService.getById(user.getId()))
				.willReturn(user);

			PointLog pointLog = PointLogFixture.getUsePointLog(user);
			given(pointLogRepository.findByUserOrderByCreatedDateDesc(user))
				.willReturn(Optional.of(pointLog));

			//when
			boolean actual = pointService.usePoint(PointLogFixture.getPointUseRequest(), 1L);

			//then
			assertThat(actual).isTrue();
			verify(eventPublisher, times(1)).publishEvent(any(RefundMailSendEvent.class));
		}

		@Test
		@DisplayName("[이전 PointLog 가 없어서 실패한다]")
		void fail() {
			//given
			User user = UserFixture.getFixedManUser(1L);
			given(userFindService.getById(user.getId()))
				.willReturn(user);

			given(pointLogRepository.findByUserOrderByCreatedDateDesc(user))
				.willReturn(Optional.empty());

			//when
			ThrowingCallable when = () -> pointService.usePoint(PointLogFixture.getPointUseRequest(), 1L);

			//then
			assertThatThrownBy(when)
				.isInstanceOf(WishHairException.class)
				.hasMessageContaining(ErrorCode.POINT_NOT_ENOUGH.getMessage());
		}
	}

	@Nested
	@DisplayName("[포인트를 충전한다]")
	class chargePoint {

		@Test
		@DisplayName("[성공적으로 충전한다]")
		void success1() {
			//given
			User user = UserFixture.getFixedManUser(1L);
			given(userFindService.getById(user.getId()))
				.willReturn(user);

			PointLog pointLog = PointLogFixture.getUsePointLog(user);
			given(pointLogRepository.findByUserOrderByCreatedDateDesc(user))
				.willReturn(Optional.of(pointLog));

			//when
			boolean actual = pointService.chargePoint(1000, 1L);

			//then
			assertThat(actual).isTrue();
		}

		@Test
		@DisplayName("[이전 PointLog 가 없어서 0원에서 시작한 PointLog 를 만든다]")
		void success2() {
			//given
			User user = UserFixture.getFixedManUser(1L);
			given(userFindService.getById(user.getId()))
				.willReturn(user);

			given(pointLogRepository.findByUserOrderByCreatedDateDesc(user))
				.willReturn(Optional.empty());

			//when
			boolean actual = pointService.chargePoint(1000, 1L);

			//then
			assertThat(actual).isTrue();
		}
	}
}