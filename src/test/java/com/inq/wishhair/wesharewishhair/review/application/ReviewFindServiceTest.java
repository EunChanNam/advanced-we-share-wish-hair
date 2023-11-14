package com.inq.wishhair.wesharewishhair.review.application;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.inq.wishhair.wesharewishhair.common.support.MockTestSupport;
import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;
import com.inq.wishhair.wesharewishhair.review.domain.ReviewRepository;
import com.inq.wishhair.wesharewishhair.review.domain.entity.Review;
import com.inq.wishhair.wesharewishhair.review.fixture.ReviewFixture;

@DisplayName("[ReviewFindService 테스트]")
class ReviewFindServiceTest extends MockTestSupport {

	@InjectMocks
	private ReviewFindService reviewFindService;
	@Mock
	private ReviewRepository reviewRepository;

	@Nested
	@DisplayName("[아이디로 리뷰를 조회한다(photo 조인)]")
	class findWithPhotosById {

		@Test
		@DisplayName("[성공적으로 조회한다]")
		void success() {
			//given
			Review review = ReviewFixture.getReview();
			given(reviewRepository.findWithPhotosById(1L))
				.willReturn(Optional.of(review));

			//when
			Review actual = reviewFindService.findWithPhotosById(1L);

			//then
			assertThat(actual).isEqualTo(review);
		}

		@Test
		@DisplayName("[아이디에 해당하는 리뷰가 존재하지 않아 실패한다]")
		void fail() {
			//given
			given(reviewRepository.findWithPhotosById(1L))
				.willReturn(Optional.empty());

			//when
			ThrowingCallable when = () -> reviewFindService.findWithPhotosById(1L);

			//then
			assertThatThrownBy(when)
				.isInstanceOf(WishHairException.class)
				.hasMessageContaining(ErrorCode.NOT_EXIST_KEY.getMessage());
		}
	}

	@Test
	@DisplayName("[특정 작성자의 리뷰를 조회한다]")
	void findWithPhotosByUserId() {
		//given
		List<Review> reviews = List.of(ReviewFixture.getReview());
		given(reviewRepository.findWithPhotosByWriterId(1L))
			.willReturn(reviews);

		//when
		List<Review> actual = reviewFindService.findWithPhotosByUserId(1L);

		//then
		assertThat(actual)
			.hasSameSizeAs(reviews)
			.containsAll(reviews);
	}
}