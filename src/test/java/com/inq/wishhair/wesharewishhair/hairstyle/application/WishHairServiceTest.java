package com.inq.wishhair.wesharewishhair.hairstyle.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.inq.wishhair.wesharewishhair.common.support.MockTestSupport;
import com.inq.wishhair.wesharewishhair.hairstyle.application.dto.response.WishHairResponse;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.wishhair.WishHair;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.wishhair.WishHairRepository;

import jakarta.persistence.EntityExistsException;

@DisplayName("[WishHairService 테스트] - Application")
class WishHairServiceTest extends MockTestSupport {

	@InjectMocks
	private WishHairService wishHairService;
	@Mock
	private WishHairRepository wishHairRepository;

	@Nested
	@DisplayName("[헤어스타일 찜한다]")
	class executeWish {

		@Test
		@DisplayName("[성공적으로 찜하고 true 를 반환한다]")
		void success() {
			//when
			boolean actual = wishHairService.executeWish(1L, 1L);

			//then
			assertThat(actual).isTrue();
		}

		@Test
		@DisplayName("[이미 찜한 상태여서 false 를 반환한다]")
		void fail() {
			//given
			given(wishHairRepository.save(any(WishHair.class)))
				.willThrow(new EntityExistsException());

			//when
			boolean actual = wishHairService.executeWish(1L, 1L);

			//then
			assertThat(actual).isFalse();
		}
	}

	@Test
	@DisplayName("[찜을 취소한다]")
	void cancelWish() {
		//when
		boolean actual = wishHairService.cancelWish(1L, 1L);

		//then
		assertThat(actual).isTrue();
	}

	@Test
	@DisplayName("[찜한 헤어스타일인지 확인한다]")
	void checkIsWishing() {
		//given
		given(wishHairRepository.existsByHairStyleIdAndUserId(1L, 1L))
			.willReturn(true);

		//when
		WishHairResponse actual = wishHairService.checkIsWishing(1L, 1L);

		//then
		assertThat(actual.isWishing()).isTrue();
	}
}