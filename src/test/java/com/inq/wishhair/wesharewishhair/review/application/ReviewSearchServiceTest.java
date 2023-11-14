package com.inq.wishhair.wesharewishhair.review.application;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import com.inq.wishhair.wesharewishhair.common.support.MockTestSupport;
import com.inq.wishhair.wesharewishhair.global.dto.response.PagedResponse;
import com.inq.wishhair.wesharewishhair.global.dto.response.Paging;
import com.inq.wishhair.wesharewishhair.global.dto.response.ResponseWrapper;
import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;
import com.inq.wishhair.wesharewishhair.hairstyle.application.dto.response.HashTagResponse;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.HairStyle;
import com.inq.wishhair.wesharewishhair.hairstyle.fixture.HairStyleFixture;
import com.inq.wishhair.wesharewishhair.photo.application.dto.response.PhotoInfo;
import com.inq.wishhair.wesharewishhair.review.application.dto.response.LikeReviewResponse;
import com.inq.wishhair.wesharewishhair.review.application.dto.response.ReviewDetailResponse;
import com.inq.wishhair.wesharewishhair.review.application.dto.response.ReviewResponse;
import com.inq.wishhair.wesharewishhair.review.application.dto.response.ReviewSimpleResponse;
import com.inq.wishhair.wesharewishhair.review.domain.ReviewQueryRepository;
import com.inq.wishhair.wesharewishhair.review.domain.entity.Review;
import com.inq.wishhair.wesharewishhair.review.fixture.ReviewFixture;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;
import com.inq.wishhair.wesharewishhair.user.fixture.UserFixture;

@DisplayName("[ReviewSearchService 테스트]")
class ReviewSearchServiceTest extends MockTestSupport {

	@InjectMocks
	private ReviewSearchService reviewSearchService;
	@Mock
	private ReviewQueryRepository reviewQueryRepository;
	@Mock
	private LikeReviewService likeReviewService;

	private void assertReviewResponse(
		ReviewResponse response,
		Review review,
		Long likeCount
	) {
		assertAll(
			() -> assertThat(response.getReviewId()).isEqualTo(review.getId()),
			() -> assertThat(response.getLikes()).isEqualTo(likeCount),
			() -> assertThat(response.getContents()).isEqualTo(review.getContentsValue()),
			() -> assertThat(response.getHairStyleName()).isEqualTo(review.getHairStyle().getName()),
			() -> assertThat(response.getScore()).isEqualTo(review.getScore().getValue()),
			() -> assertThat(response.getUserNickname()).isEqualTo(review.getWriter().getNicknameValue()),
			() -> assertThat(response.getWriterId()).isEqualTo(review.getWriter().getId()),
			() -> {
				List<HashTagResponse> expectedHashTags = review.getHairStyle().getHashTags()
					.stream()
					.map(HashTagResponse::new)
					.toList();

				assertThat(response.getHashTags()).containsAll(expectedHashTags);
			},
			() -> {
				List<PhotoInfo> expectedPhotos = review.getPhotos()
					.stream()
					.map(photo -> new PhotoInfo(photo.getStoreUrl()))
					.toList();

				assertThat(response.getPhotos()).containsAll(expectedPhotos);
			}
		);
	}

	private void assertPaging(
		Paging actual,
		boolean hasNext,
		int contentSize
	) {
		assertThat(actual.hasNext()).isEqualTo(hasNext);
		assertThat(actual.getContentSize()).isEqualTo(contentSize);
	}

	private Review createReview(Long id) {
		HairStyle hairStyle = HairStyleFixture.getWomanHairStyle(id);
		User user = UserFixture.getFixedManUser(id);
		return ReviewFixture.getReview(id, hairStyle, user);
	}

	@Nested
	@DisplayName("[리뷰를 단건 조회한다]")
	class findReviewById {

		@Test
		@DisplayName("[성공적으로 조회한다]")
		void success() {
			//given
			Review review = createReview(1L);

			given(reviewQueryRepository.findReviewById(1L))
				.willReturn(Optional.of(review));

			given(likeReviewService.getLikeCount(1L))
				.willReturn(10L);

			given(likeReviewService.checkIsLiking(1L, 1L))
				.willReturn(new LikeReviewResponse(true));

			//when
			ReviewDetailResponse actual = reviewSearchService.findReviewById(1L, 1L);

			//then
			assertThat(actual.isLiking()).isTrue();
			assertReviewResponse(actual.reviewResponse(), review, 10L);
		}

		@Test
		@DisplayName("[아이디에 해당하는 리뷰가 존재하지 않아 실패한다]")
		void fail() {
			//given
			given(reviewQueryRepository.findReviewById(1L))
				.willReturn(Optional.empty());

			//when
			ThrowingCallable when = () -> reviewSearchService.findReviewById(1L, 1L);

			//then
			assertThatThrownBy(when)
				.isInstanceOf(WishHairException.class)
				.hasMessageContaining(ErrorCode.NOT_EXIST_KEY.getMessage());
		}
	}

	@Test
	@DisplayName("[전체 리뷰를 조회한다]")
	void findPagedReviews() {
		//given
		Pageable pageable = PageRequest.of(0, 4);

		List<Review> reviews = List.of(createReview(1L), createReview(2L));
		Slice<Review> reviewSlice = new SliceImpl<>(reviews, pageable, false);
		given(reviewQueryRepository.findReviewByPaging(pageable))
			.willReturn(reviewSlice);

		List<Long> likeCounts = List.of(10L, 20L);
		given(likeReviewService.getLikeCounts(List.of(1L, 2L)))
			.willReturn(likeCounts);

		//when
		PagedResponse<ReviewResponse> actual = reviewSearchService.findPagedReviews(1L, pageable);

		//then
		assertPaging(actual.getPaging(), false, 2);
		List<ReviewResponse> result = actual.getResult();
		for (int i = 0; i < result.size(); i++) {
			assertReviewResponse(result.get(i), reviews.get(i), likeCounts.get(i));
		}
	}

	@Test
	@DisplayName("[좋아요한 리뷰를 조회한다]")
	void findLikingReviews() {
		//given
		Pageable pageable = PageRequest.of(0, 4);

		List<Review> reviews = List.of(createReview(1L), createReview(2L));
		Slice<Review> reviewSlice = new SliceImpl<>(reviews, pageable, false);
		given(reviewQueryRepository.findReviewByLike(1L, pageable))
			.willReturn(reviewSlice);

		List<Long> likeCounts = List.of(10L, 20L);
		given(likeReviewService.getLikeCounts(List.of(1L, 2L)))
			.willReturn(likeCounts);

		//when
		PagedResponse<ReviewResponse> actual = reviewSearchService.findLikingReviews(1L, pageable);

		//then
		assertPaging(actual.getPaging(), false, 2);
		List<ReviewResponse> result = actual.getResult();
		for (int i = 0; i < result.size(); i++) {
			assertReviewResponse(result.get(i), reviews.get(i), likeCounts.get(i));
		}
	}

	@Test
	@DisplayName("[내가 작성한 리뷰를 조회한다]")
	void findMyReviews() {
		//given
		Pageable pageable = PageRequest.of(0, 4);

		List<Review> reviews = List.of(createReview(1L), createReview(2L));
		Slice<Review> reviewSlice = new SliceImpl<>(reviews, pageable, false);
		given(reviewQueryRepository.findReviewByUser(1L, pageable))
			.willReturn(reviewSlice);

		List<Long> likeCounts = List.of(10L, 20L);
		given(likeReviewService.getLikeCounts(List.of(1L, 2L)))
			.willReturn(likeCounts);

		//when
		PagedResponse<ReviewResponse> actual = reviewSearchService.findMyReviews(1L, pageable);

		//then
		assertPaging(actual.getPaging(), false, 2);
		List<ReviewResponse> result = actual.getResult();
		for (int i = 0; i < result.size(); i++) {
			assertReviewResponse(result.get(i), reviews.get(i), likeCounts.get(i));
		}
	}

	@Test
	@DisplayName("[이달의 리뷰를 조회한다]")
	void findReviewOfMonth() {
		//given
		List<Review> reviews = List.of(createReview(1L), createReview(2L));
		given(reviewQueryRepository.findReviewByCreatedDate())
			.willReturn(reviews);

		//when
		ResponseWrapper<ReviewSimpleResponse> actual = reviewSearchService.findReviewOfMonth();

		//then
		List<ReviewSimpleResponse> result = actual.getResult();
		assertThat(result).hasSameSizeAs(reviews);
		for (int i = 0; i < result.size(); i++) {
			ReviewSimpleResponse response = result.get(i);
			Review review = reviews.get(i);
			assertAll(
				() -> assertThat(response.reviewId()).isEqualTo(review.getId()),
				() -> assertThat(response.userNickname()).isEqualTo(review.getWriter().getNicknameValue()),
				() -> assertThat(response.hairStyleName()).isEqualTo(review.getHairStyle().getName()),
				() -> assertThat(response.contents()).isEqualTo(review.getContentsValue())
			);
		}
	}

	@Test
	@DisplayName("[특정 헤어스타일의 리뷰를 조회한다]")
	void findReviewByHairStyle() {
		List<Review> reviews = List.of(createReview(1L));
		given(reviewQueryRepository.findReviewByHairStyle(1L))
			.willReturn(reviews);

		List<Long> likeCounts = List.of(10L);
		given(likeReviewService.getLikeCounts(List.of(1L)))
			.willReturn(likeCounts);

		//when
		ResponseWrapper<ReviewResponse> actual = reviewSearchService.findReviewByHairStyle(1L, 1L);

		//then
		List<ReviewResponse> result = actual.getResult();
		for (int i = 0; i < result.size(); i++) {
			assertReviewResponse(result.get(i), reviews.get(i), likeCounts.get(i));
		}
	}
}