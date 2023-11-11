package com.inq.wishhair.wesharewishhair.hairstyle.application;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

import com.inq.wishhair.wesharewishhair.common.support.MockTestSupport;
import com.inq.wishhair.wesharewishhair.global.dto.response.PagedResponse;
import com.inq.wishhair.wesharewishhair.global.dto.response.Paging;
import com.inq.wishhair.wesharewishhair.global.dto.response.ResponseWrapper;
import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;
import com.inq.wishhair.wesharewishhair.hairstyle.application.dto.response.HairStyleResponse;
import com.inq.wishhair.wesharewishhair.hairstyle.application.dto.response.HairStyleSimpleResponse;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.HairStyle;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.HairStyleQueryRepository;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.HairStyleRepository;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.hashtag.Tag;
import com.inq.wishhair.wesharewishhair.hairstyle.fixture.HairStyleFixture;
import com.inq.wishhair.wesharewishhair.hairstyle.utils.HairRecommendCondition;
import com.inq.wishhair.wesharewishhair.user.application.UserFindService;
import com.inq.wishhair.wesharewishhair.user.domain.entity.FaceShape;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;
import com.inq.wishhair.wesharewishhair.user.fixture.UserFixture;

@DisplayName("[HairStyleSearchService 테스트]")
class HairStyleSearchServiceTest extends MockTestSupport {

	@InjectMocks
	private HairStyleSearchService hairStyleSearchService;
	@Mock
	private HairStyleRepository hairStyleRepository;
	@Mock
	private HairStyleQueryRepository hairStyleQueryRepository;
	@Mock
	private UserFindService userFindService;

	private final List<HairStyle> hairStyles = List.of(
		HairStyleFixture.getWomanHairStyle(1L),
		HairStyleFixture.getWomanHairStyle(2L)
	);

	private void assertHairStyleResponse(HairStyleResponse response, HairStyle hairStyle) {
		assertAll(
			() -> assertThat(response.hairStyleId()).isEqualTo(hairStyle.getId()),
			() -> assertThat(response.name()).isEqualTo(hairStyle.getName()),
			() -> assertThat(response.hashTags()).hasSameSizeAs(hairStyle.getHashTags()),
			() -> assertThat(response.photos()).hasSameSizeAs(hairStyle.getPhotos())
		);
	}

	@Nested
	@DisplayName("[태그와 사용자의 얼굴형을 기반으로 헤어스타일을 추천한다]")
	class recommendHair {

		@Test
		@DisplayName("[성공적으로 추천한다]")
		void success() {
			//given
			User user = UserFixture.getFixedWomanUser(1L);
			user.updateFaceShape(new FaceShape(Tag.ROUND));
			given(userFindService.findByUserId(user.getId()))
				.willReturn(user);

			given(hairStyleQueryRepository.findByRecommend(any(HairRecommendCondition.class), any(Pageable.class)))
				.willReturn(hairStyles);

			//when
			ResponseWrapper<HairStyleResponse> actual = hairStyleSearchService.recommendHair(
				List.of(Tag.CUTE, Tag.LIGHT), 1L);

			//then
			List<HairStyleResponse> result = actual.getResult();
			assertThat(result).hasSameSizeAs(hairStyles);
			for (int i = 0; i < result.size(); i++) {
				assertHairStyleResponse(result.get(i), hairStyles.get(i));
			}
		}

		@Test
		@DisplayName("[얼굴형이 존재하지 않는 user 로 실패한다]")
		void fail() {
			//given
			User user = UserFixture.getFixedWomanUser(1L);
			given(userFindService.findByUserId(user.getId()))
				.willReturn(user);

			//when
			ThrowingCallable when = () -> hairStyleSearchService.recommendHair(List.of(Tag.CUTE, Tag.LIGHT), 1L);

			//then
			assertThatThrownBy(when)
				.isInstanceOf(WishHairException.class)
				.hasMessageContaining(ErrorCode.USER_NO_FACE_SHAPE_TAG.getMessage());
		}
	}

	@Test
	@DisplayName("[홈화면 사용자 맞춤 헤어스타일 추천한다]")
	void recommendHairByFaceShape() {
		//given
		User user = UserFixture.getFixedWomanUser(1L);
		user.updateFaceShape(new FaceShape(Tag.ROUND));
		given(userFindService.findByUserId(user.getId()))
			.willReturn(user);

		given(hairStyleQueryRepository.findByFaceShape(any(HairRecommendCondition.class), any(Pageable.class)))
			.willReturn(hairStyles);

		//when
		ResponseWrapper<HairStyleResponse> actual = hairStyleSearchService.recommendHairByFaceShape(1L);

		//then
		List<HairStyleResponse> result = actual.getResult();
		assertThat(result).hasSameSizeAs(hairStyles);
		for (int i = 0; i < result.size(); i++) {
			assertHairStyleResponse(result.get(i), hairStyles.get(i));
		}
	}

	@Test
	@DisplayName("[찜한 헤어스타일을 조회한다]")
	void findWishHairStyles() {
		//given
		SliceImpl<HairStyle> sliceHairStyles = new SliceImpl<>(hairStyles);
		given(hairStyleQueryRepository.findByWish(eq(1L), any(Pageable.class)))
			.willReturn(sliceHairStyles);

		//when
		PagedResponse<HairStyleResponse> actual = hairStyleSearchService.findWishHairStyles(
			1L, PageRequest.of(0, 4)
		);

		//then
		Paging paging = actual.getPaging();
		assertAll(
			() -> assertThat(paging.getPage()).isZero(),
			() -> assertThat(paging.hasNext()).isFalse(),
			() -> assertThat(paging.getContentSize()).isEqualTo(2)
		);

		List<HairStyleResponse> result = actual.getResult();
		assertThat(result).hasSameSizeAs(hairStyles);
		for (int i = 0; i < result.size(); i++) {
			assertHairStyleResponse(result.get(i), hairStyles.get(i));
		}
	}

	@Test
	@DisplayName("[전체 헤어스타일을 조회한다]")
	void findAllHairStyle() {
		//given
		given(hairStyleRepository.findAllByOrderByName())
			.willReturn(hairStyles);

		//when
		ResponseWrapper<HairStyleSimpleResponse> actual = hairStyleSearchService.findAllHairStyle();

		//then
		List<HairStyleSimpleResponse> result = actual.getResult();
		assertThat(result).hasSameSizeAs(hairStyles);
		for (int i = 0; i < result.size(); i++) {
			HairStyleSimpleResponse response = result.get(i);
			HairStyle hairStyle = hairStyles.get(i);

			assertAll(
				() -> assertThat(response.hairStyleId()).isEqualTo(hairStyle.getId()),
				() -> assertThat(response.hairStyleName()).isEqualTo(hairStyle.getName())
			);
		}
	}

}