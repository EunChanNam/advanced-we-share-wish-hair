package com.inq.wishhair.wesharewishhair.hairstyle.application;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.HairStyle;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.HairStyleRepository;
import com.inq.wishhair.wesharewishhair.hairstyle.fixture.HairStyleFixture;

@DisplayName("[HairStyleFindService 테스트] - Application")
class HairStyleFindServiceTest {

	private final HairStyleFindService hairStyleFindService;
	private final HairStyleRepository hairStyleRepository;

	public HairStyleFindServiceTest() {
		this.hairStyleRepository = Mockito.mock(HairStyleRepository.class);
		this.hairStyleFindService = new HairStyleFindService(hairStyleRepository);
	}

	@Nested
	@DisplayName("[아이디로 HairStyle 을 조회한다]")
	class findById {

		@Test
		@DisplayName("[성공적으로 조회한다]")
		void success() {
			//given
			HairStyle hairStyle = HairStyleFixture.getWomanHairStyle();
			given(hairStyleRepository.findById(1L))
				.willReturn(Optional.of(hairStyle));

			//when
			HairStyle actual = hairStyleFindService.getById(1L);

			//then
			assertThat(actual).isEqualTo(hairStyle);
		}

		@Test
		@DisplayName("[아이디에 해당하는 HairStyle 이 존재하지 않아 실패한다]")
		void fail() {
			//given
			given(hairStyleRepository.findById(1L))
				.willReturn(Optional.empty());

			//when
			ThrowingCallable when = () -> hairStyleFindService.getById(1L);

			//then
			assertThatThrownBy(when)
				.isInstanceOf(WishHairException.class)
				.hasMessageContaining(ErrorCode.NOT_EXIST_KEY.getMessage());
		}
	}
}