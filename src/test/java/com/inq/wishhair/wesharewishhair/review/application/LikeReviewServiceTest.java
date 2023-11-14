package com.inq.wishhair.wesharewishhair.review.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.inq.wishhair.wesharewishhair.common.support.MockTestSupport;
import com.inq.wishhair.wesharewishhair.global.utils.RedisUtils;
import com.inq.wishhair.wesharewishhair.review.application.dto.response.LikeReviewResponse;
import com.inq.wishhair.wesharewishhair.review.domain.likereview.LikeReview;
import com.inq.wishhair.wesharewishhair.review.domain.likereview.LikeReviewRepository;

import jakarta.persistence.EntityExistsException;

@DisplayName("[LikeReviewService 테스트]")
class LikeReviewServiceTest extends MockTestSupport {

	@InjectMocks
	private LikeReviewService likeReviewService;
	@Mock
	private LikeReviewRepository likeReviewRepository;
	@Mock
	private RedisUtils redisUtils;

	@Nested
	@DisplayName("[좋아요를 실행한다]")
	class executeLike {

		@Nested
		@DisplayName("[성공적으로 좋아요 한다]")
		class returnTrue {

			@Test
			@DisplayName("[레디스에 좋아요 정보가 있어서 기존 값을 증가시킨다]")
			void success1() {
				//given
				given(redisUtils.getData(1L))
					.willReturn(Optional.of(1L));

				//when
				boolean actual = likeReviewService.executeLike(1L, 1L);

				//then
				assertThat(actual).isTrue();
				verify(redisUtils, timeout(1)).increaseData(1L);
			}

			@Test
			@DisplayName("[레디스에 좋아요 정보가 없어서 새로 값을 세팅한다]")
			void success2() {
				//given
				given(redisUtils.getData(1L))
					.willReturn(Optional.empty());

				given(likeReviewRepository.countByReviewId(1L))
					.willReturn(10L);

				//when
				boolean actual = likeReviewService.executeLike(1L, 1L);

				//then
				assertThat(actual).isTrue();
				verify(redisUtils, timeout(1)).setData(1L, 10L);
			}
		}

		@Test
		@DisplayName("[이미 좋아요한 상태여서 false 를 반환한다]")
		void returnFalse() {
			given(likeReviewRepository.save(any(LikeReview.class)))
				.willThrow(new EntityExistsException());

			//when
			boolean actual = likeReviewService.executeLike(1L, 1L);

			//then
			assertThat(actual).isFalse();
		}
	}

	@Nested
	@DisplayName("[좋아요를 취소한다]")
	class cancelLike {

		@Test
		@DisplayName("[레디스에 좋아요 정보가 있어서 기존 값을 감소시킨다]")
		void success1() {
			//given
			given(redisUtils.getData(1L))
				.willReturn(Optional.of(1L));

			//when
			boolean actual = likeReviewService.cancelLike(1L, 1L);

			//then
			assertThat(actual).isTrue();
			verify(redisUtils, timeout(1)).decreaseData(1L);
		}

		@Test
		@DisplayName("[레디스에 좋아요 정보가 없어서 새로운 값을 세팅한다]")
		void success2() {
			//given
			given(redisUtils.getData(1L))
				.willReturn(Optional.empty());

			given(likeReviewRepository.countByReviewId(1L))
				.willReturn(10L);

			//when
			boolean actual = likeReviewService.cancelLike(1L, 1L);

			//then
			assertThat(actual).isTrue();
			verify(redisUtils, timeout(1)).setData(1L, 10L);
		}
	}

	@Test
	@DisplayName("[리뷰를 사용자가 좋아요한 상태인지 확인한다]")
	void checkIsLiking() {
		//given
		given(likeReviewRepository.existsByUserIdAndReviewId(1L, 1L))
			.willReturn(true);

		//when
		LikeReviewResponse actual = likeReviewService.checkIsLiking(1L, 1L);

		//then
		assertThat(actual.isLiking()).isTrue();
	}

	@Nested
	@DisplayName("[좋아요 개수를 조회한다]")
	class getLikeCount {

		@Test
		@DisplayName("[레디스에 좋아요 정보가 있어서 기존 값을 가져온다]")
		void success1() {
			//given
			given(redisUtils.getData(1L))
				.willReturn(Optional.of(10L));

			//when
			Long actual = likeReviewService.getLikeCount(1L);

			//then
			assertThat(actual).isEqualTo(10L);
		}

		@Test
		@DisplayName("[레디스에 좋아요 정보가 없어서 새로운 값을 세팅 후 가져온다]")
		void success2() {
			//given
			given(redisUtils.getData(1L))
				.willReturn(Optional.empty());

			given(likeReviewRepository.countByReviewId(1L))
				.willReturn(10L);

			//when
			Long actual = likeReviewService.getLikeCount(1L);

			//then
			assertThat(actual).isEqualTo(10L);
			verify(redisUtils, timeout(1)).setData(1L, 10L);
		}
	}

	@Test
	@DisplayName("[다수의 리뷰의 좋아요 개수들을 조회한다]")
	void getLikeCounts() {
		//given
		List<Long> reviewIds = List.of(1L, 2L);

		reviewIds.forEach(reviewId ->
			given(redisUtils.getData(reviewId)).willReturn(Optional.of(10L))
		);

		//when
		List<Long> actual = likeReviewService.getLikeCounts(reviewIds);

		//then
		assertThat(actual).contains(10L, 10L);
	}
}