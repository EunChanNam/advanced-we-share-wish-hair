package com.inq.wishhair.wesharewishhair.review.application;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationEventPublisher;

import com.inq.wishhair.wesharewishhair.common.support.MockTestSupport;
import com.inq.wishhair.wesharewishhair.common.utils.FileMockingUtils;
import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;
import com.inq.wishhair.wesharewishhair.hairstyle.application.HairStyleFindService;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.HairStyle;
import com.inq.wishhair.wesharewishhair.hairstyle.fixture.HairStyleFixture;
import com.inq.wishhair.wesharewishhair.photo.application.PhotoService;
import com.inq.wishhair.wesharewishhair.review.application.dto.request.ReviewCreateRequest;
import com.inq.wishhair.wesharewishhair.review.application.dto.request.ReviewUpdateRequest;
import com.inq.wishhair.wesharewishhair.review.domain.ReviewRepository;
import com.inq.wishhair.wesharewishhair.review.domain.entity.Review;
import com.inq.wishhair.wesharewishhair.review.domain.entity.Score;
import com.inq.wishhair.wesharewishhair.review.domain.likereview.LikeReviewRepository;
import com.inq.wishhair.wesharewishhair.review.event.PointChargeEvent;
import com.inq.wishhair.wesharewishhair.review.fixture.ReviewFixture;
import com.inq.wishhair.wesharewishhair.user.application.UserFindService;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;
import com.inq.wishhair.wesharewishhair.user.fixture.UserFixture;

@DisplayName("[ReviewService 테스트]")
class ReviewServiceTest extends MockTestSupport {

	@InjectMocks
	private ReviewService reviewService;
	@Mock
	private ReviewRepository reviewRepository;
	@Mock
	private LikeReviewRepository likeReviewRepository;
	@Mock
	private ReviewFindService reviewFindService;
	@Mock
	private PhotoService photoService;
	@Mock
	private UserFindService userFindService;
	@Mock
	private HairStyleFindService hairStyleFindService;
	@Mock
	private ApplicationEventPublisher eventPublisher;

	private void mockingFindWithPhotosById() {
		User user = UserFixture.getFixedManUser(1L);
		Review review = ReviewFixture.getReview(1L, user);
		given(reviewFindService.findWithPhotosById(1L))
			.willReturn(review);
	}

	@Test
	@DisplayName("[리뷰를 생성한다]")
	void createReview() throws IOException {
		//given
		List<String> photoUrls = List.of("url1", "url2");
		given(photoService.uploadPhotos(anyList()))
			.willReturn(photoUrls);

		User user = UserFixture.getFixedManUser(1L);
		given(userFindService.getById(1L))
			.willReturn(user);

		HairStyle hairstyle = HairStyleFixture.getWomanHairStyle(1L);
		given(hairStyleFindService.getById(1L))
			.willReturn(hairstyle);

		Review review = ReviewFixture.getReview(1L);
		given(reviewRepository.save(any(Review.class)))
			.willReturn(review);

		ReviewCreateRequest request = ReviewFixture.getReviewCreateRequest();

		//when
		Long actual = reviewService.createReview(request, 1L);

		//then
		assertThat(actual).isEqualTo(user.getId());
		verify(eventPublisher, times(1)).publishEvent(any(PointChargeEvent.class));
	}
	@Nested
	@DisplayName("[리뷰를 삭제한다]")
	class deleteReview {


		@Test
		@DisplayName("[성공적으로 삭제한다]")
		void success() {
			//given
			mockingFindWithPhotosById();

			//when
			boolean actual = reviewService.deleteReview(1L, 1L);

			//then
			assertThat(actual).isTrue();
		}
		@Test
		@DisplayName("[작성자가 아니라 실패한다]")
		void fail() {
			//given
			mockingFindWithPhotosById();

			//when
			ThrowingCallable when = () -> reviewService.deleteReview(1L, 2L);

			//then
			assertThatThrownBy(when)
				.isInstanceOf(WishHairException.class)
				.hasMessageContaining(ErrorCode.REVIEW_NOT_WRITER.getMessage());
		}

	}

	@Nested
	@DisplayName("[리뷰 수정한다]")
	class updateReview {

		@Test
		@DisplayName("[성공적으로 수정한다]")
		void success() throws IOException {
			//given
			mockingFindWithPhotosById();

			List<String> photoUrls = List.of("url1", "url2");
			given(photoService.uploadPhotos(anyList()))
				.willReturn(photoUrls);

			ReviewUpdateRequest request = new ReviewUpdateRequest(
				1L,
				"contents",
				Score.S4H,
				FileMockingUtils.createMockMultipartFiles()
			);

			//when
			boolean actual = reviewService.updateReview(request, 1L);

			//then
			assertThat(actual).isTrue();
		}

		@Test
		@DisplayName("[작성자가 아니라 실패한다]")
		void fail() throws IOException {
			//given
			mockingFindWithPhotosById();

			ReviewUpdateRequest request = new ReviewUpdateRequest(
				1L,
				"contents",
				Score.S4H,
				FileMockingUtils.createMockMultipartFiles()
			);

			//when
			ThrowingCallable when = () -> reviewService.updateReview(request, 2L);

			//then
			assertThatThrownBy(when)
				.isInstanceOf(WishHairException.class)
				.hasMessageContaining(ErrorCode.REVIEW_NOT_WRITER.getMessage());
		}
	}

	@Test
	@DisplayName("[특정 작성자의 리뷰를 모두 삭제한다]")
	void deleteReviewByWriter() {
		//given
		User user = UserFixture.getFixedManUser(1L);
		List<Review> reviews = List.of(ReviewFixture.getReview(1L, user));
		given(reviewFindService.findWithPhotosByUserId(1L))
			.willReturn(reviews);

		//when
		boolean actual = reviewService.deleteReviewByWriter(1L);

		//then
		assertThat(actual).isTrue();
	}
}