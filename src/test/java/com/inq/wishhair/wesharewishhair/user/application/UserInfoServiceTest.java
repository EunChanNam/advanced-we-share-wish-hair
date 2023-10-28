package com.inq.wishhair.wesharewishhair.user.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import com.inq.wishhair.wesharewishhair.global.dto.response.PagedResponse;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.hashtag.Tag;
import com.inq.wishhair.wesharewishhair.point.domain.PointLog;
import com.inq.wishhair.wesharewishhair.point.domain.PointLogRepository;
import com.inq.wishhair.wesharewishhair.point.domain.PointType;
import com.inq.wishhair.wesharewishhair.review.application.ReviewSearchService;
import com.inq.wishhair.wesharewishhair.user.application.dto.response.MyPageResponse;
import com.inq.wishhair.wesharewishhair.user.application.dto.response.UserInfo;
import com.inq.wishhair.wesharewishhair.user.application.dto.response.UserInformation;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;
import com.inq.wishhair.wesharewishhair.user.fixture.UserFixture;

@ExtendWith(MockitoExtension.class)
@DisplayName("[UserInfoService 테스트] - Application")
class UserInfoServiceTest {

	@InjectMocks
	private UserInfoService userInfoService;
	@Mock
	private UserFindService userFindService;
	@Mock
	private ReviewSearchService reviewSearchService;
	@Mock
	private PointLogRepository pointLogRepository;

	@Nested
	@DisplayName("[마이페이지 정보를 조회한다]")
	class getMyPageInfo {

		@Test
		@DisplayName("[포인트 기록이 존재해 잔여 포인트까지 조회한다]")
		void hasPointLog() {
			//given
			User user = UserFixture.getFixedManUser();
			given(userFindService.getById(1L)).willReturn(user);

			PointLog pointLog = PointLog.addPointLog(user, PointType.CHARGE, 1000, 500);
			given(pointLogRepository.findByUserOrderByCreatedDateDesc(user))
				.willReturn(Optional.of(pointLog));

			given(reviewSearchService.findLikingReviews(eq(1L), any(Pageable.class)))
				.willReturn(new PagedResponse<>(new ArrayList<>(), null));

			//when
			MyPageResponse actual = userInfoService.getMyPageInfo(1L);

			//then
			assertAll(
				() -> assertThat(actual.nickname()).isEqualTo(user.getNicknameValue()),
				() -> assertThat(actual.point()).isEqualTo(pointLog.getPoint()),
				() -> assertThat(actual.reviews()).isEmpty(),
				() -> assertThat(actual.sex()).isEqualTo(user.getSex())
			);
		}

		@Test
		@DisplayName("[포인트 기록이 존재하지 않아 0 포인트를 조회한다]")
		void noPoint() {
			//given
			User user = UserFixture.getFixedManUser();
			given(userFindService.getById(1L)).willReturn(user);

			given(pointLogRepository.findByUserOrderByCreatedDateDesc(user))
				.willReturn(Optional.empty());

			given(reviewSearchService.findLikingReviews(eq(1L), any(Pageable.class)))
				.willReturn(new PagedResponse<>(new ArrayList<>(), null));

			//when
			MyPageResponse actual = userInfoService.getMyPageInfo(1L);

			//then
			assertAll(
				() -> assertThat(actual.nickname()).isEqualTo(user.getNicknameValue()),
				() -> assertThat(actual.point()).isZero(),
				() -> assertThat(actual.reviews()).isEmpty(),
				() -> assertThat(actual.sex()).isEqualTo(user.getSex())
			);
		}
	}

	@Test
	@DisplayName("[얼굴형 정보가 포함된 유저의 간단한 정보를 조회한다]")
	void getUserInfo() {
		//given
		User user = UserFixture.getFixedManUser();
		user.updateFaceShape(Tag.ROUND);
		given(userFindService.getById(1L)).willReturn(user);

		//when
		UserInfo actual = userInfoService.getUserInfo(1L);

		//then
		assertAll(
			() -> assertThat(actual.hasFaceShape()).isTrue(),
			() -> assertThat(actual.faceShapeTag()).isEqualTo(Tag.ROUND.getDescription())
		);
	}

	@Test
	@DisplayName("[유저 상세정보를 조회한다]")
	void getUserInformation() {
		//given
		User user = UserFixture.getFixedManUser();
		given(userFindService.getById(1L)).willReturn(user);

		//when
		UserInformation actual = userInfoService.getUserInformation(1L);

		//then
		assertAll(
			() -> assertThat(actual.email()).isEqualTo(user.getEmailValue()),
			() -> assertThat(actual.name()).isEqualTo(user.getName()),
			() -> assertThat(actual.nickname()).isEqualTo(user.getNicknameValue()),
			() -> assertThat(actual.sex()).isEqualTo(user.getSex())
		);
	}
}