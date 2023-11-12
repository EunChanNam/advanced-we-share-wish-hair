package com.inq.wishhair.wesharewishhair.review.domain;

import static com.inq.wishhair.wesharewishhair.global.utils.PageableGenerator.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;

import com.inq.wishhair.wesharewishhair.common.support.RepositoryTestSupport;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.HairStyle;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.HairStyleRepository;
import com.inq.wishhair.wesharewishhair.hairstyle.fixture.HairStyleFixture;
import com.inq.wishhair.wesharewishhair.review.domain.entity.Review;
import com.inq.wishhair.wesharewishhair.review.domain.likereview.LikeReview;
import com.inq.wishhair.wesharewishhair.review.domain.likereview.LikeReviewRepository;
import com.inq.wishhair.wesharewishhair.review.fixture.ReviewFixture;
import com.inq.wishhair.wesharewishhair.user.domain.UserRepository;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;
import com.inq.wishhair.wesharewishhair.user.fixture.UserFixture;

@DisplayName("[ReviewQueryRepository 테스트]")
class ReviewQueryRepositoryTest extends RepositoryTestSupport {

	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private ReviewQueryRepository reviewQueryRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private HairStyleRepository hairStyleRepository;
	@Autowired
	private LikeReviewRepository likeReviewRepository;

	private List<Review> reviews;
	private User user;
	private HairStyle hairStyle;

	@BeforeEach
	void setUp() {
		user = userRepository.save(UserFixture.getFixedWomanUser());
		hairStyle = hairStyleRepository.save(HairStyleFixture.getWomanHairStyle());
		reviews = List.of(
			ReviewFixture.getReview(hairStyle, user),
			ReviewFixture.getReview(hairStyle, user),
			ReviewFixture.getReview(hairStyle, user)
		);

		reviews.forEach(review -> reviewRepository.save(review));
	}

	@Test
	@DisplayName("[아이디로 리뷰를 조회한다(hairstyle, user 조인)]")
	void findReviewById() {
		//given
		Review review = reviews.get(0);

		//when
		Optional<Review> actual = reviewQueryRepository.findReviewById(review.getId());

		//then
		assertThat(actual).contains(review);
	}

	@Nested
	@DisplayName("[전체 리뷰를 페이징 조건에 따라 조회한다]")
	class findReviewByPaging {

		@Test
		@DisplayName("[오래된순으로 정렬한다]")
		void orderDateAsc() {
			//when
			Slice<Review> actual = reviewQueryRepository.findReviewByPaging(generateDateAscPageable(3));

			//then
			assertThat(actual.hasNext()).isFalse();
			assertThat(actual.getContent()).hasSize(3);
			assertThat(actual.getContent()).containsExactly(reviews.toArray(Review[]::new));
		}

		@Test
		@DisplayName("[최신순으로 정렬한다]")
		void orderDateDesc() {
			Slice<Review> actual = reviewQueryRepository.findReviewByPaging(generateDateDescPageable(3));

			//then
			assertThat(actual.hasNext()).isFalse();
			assertThat(actual.getContent()).hasSize(3);

			Review[] expected = reviews.stream()
				.sorted((a, b) -> Long.compare(b.getId(), a.getId()))
				.toArray(Review[]::new);
			assertThat(actual.getContent()).containsExactly(expected);
		}
	}

	@Test
	@DisplayName("[좋아요한 리뷰를 조회한다]")
	void findReviewByLike() {
		//given
		likeReviewRepository.save(
			LikeReview.addLike(user.getId(), reviews.get(0).getId())
		);
		likeReviewRepository.save(
			LikeReview.addLike(user.getId(), reviews.get(1).getId())
		);

		//when
		Slice<Review> actual = reviewQueryRepository.findReviewByLike(user.getId(), getDefaultPageable());

		//then
		assertThat(actual.hasNext()).isFalse();
		assertThat(actual.getContent()).hasSize(2);
		assertThat(actual).containsExactly(reviews.get(1), reviews.get(0));
	}

	@Test
	@DisplayName("[특정 작성자의 리뷰를 조회한다]")
	void findReviewByUser() {
		//when
		Slice<Review> actual = reviewQueryRepository.findReviewByUser(user.getId(), getDefaultPageable());

		//then
		assertThat(actual.hasNext()).isFalse();
		assertThat(actual.getContent()).hasSize(3);

		Review[] expected = reviews.stream()
			.sorted((a, b) -> Long.compare(b.getId(), a.getId()))
			.toArray(Review[]::new);
		assertThat(actual.getContent()).containsExactly(expected);
	}

	@Test
	@DisplayName("[지난달에 작성된 리뷰를 조회한다]")
	void findReviewByCreatedDate() {
		//when
		List<Review> actual = reviewQueryRepository.findReviewByCreatedDate();

		//then
		assertThat(actual).isEmpty();
	}

	@Test
	@DisplayName("[특정 헤어스타일의 리뷰를 조회한다]")
	void findReviewByHairStyle() {
		//when
		List<Review> actual = reviewQueryRepository.findReviewByHairStyle(hairStyle.getId());

		//then
		assertThat(actual).containsExactly(reviews.toArray(Review[]::new));
	}
}