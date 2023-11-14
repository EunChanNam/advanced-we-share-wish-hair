package com.inq.wishhair.wesharewishhair.review.presentation;

import static com.inq.wishhair.wesharewishhair.common.fixture.AuthFixture.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.inq.wishhair.wesharewishhair.common.config.EmbeddedRedisConfig;
import com.inq.wishhair.wesharewishhair.common.support.ApiTestSupport;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.HairStyle;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.HairStyleRepository;
import com.inq.wishhair.wesharewishhair.hairstyle.fixture.HairStyleFixture;
import com.inq.wishhair.wesharewishhair.photo.domain.PhotoStore;
import com.inq.wishhair.wesharewishhair.review.domain.ReviewRepository;
import com.inq.wishhair.wesharewishhair.review.domain.entity.Review;
import com.inq.wishhair.wesharewishhair.review.fixture.ReviewFixture;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;

@DisplayName("[ReviewSearchController 테스트]")
@Import(EmbeddedRedisConfig.class)
class ReviewSearchApiTest extends ApiTestSupport {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private HairStyleRepository hairStyleRepository;

	@MockBean
	private PhotoStore photoStore;

	@BeforeEach
	void setUp() {
		given(photoStore.uploadFiles(anyList()))
			.willReturn(List.of("url1"));
	}

	private HairStyle saveHairStyle() {
		HairStyle hairStyle = HairStyleFixture.getWomanHairStyle();
		return hairStyleRepository.save(hairStyle);
	}

	private Review saveReview(User user, HairStyle hairStyle) {
		Review review = ReviewFixture.getReview(hairStyle, user);
		reviewRepository.save(review);
		return review;
	}

	@Test
	@DisplayName("[리뷰 단건조회 API 를 호출한다]")
	void findReview() throws Exception {
		//given
		User user = saveUser();
		HairStyle hairStyle = saveHairStyle();

		Review review = saveReview(user, hairStyle);

		//when
		ResultActions actual = mockMvc.perform(
			MockMvcRequestBuilders
				.get("/api/reviews/" + review.getId())
				.header(AUTHORIZATION, BEARER + getAccessToken(user.getId()))
		);

		//then
		actual.andExpectAll(
			status().isOk(),
			jsonPath("$.reviewResponse").exists(),
			jsonPath("$.isLiking").value(false)
		);
	}

	@Test
	@DisplayName("[모든 리뷰를 조회한다]")
	void findPagingReviews() throws Exception {
		//given
		User user = saveUser();
		HairStyle hairStyle = saveHairStyle();

		saveReview(user, hairStyle);
		saveReview(user, hairStyle);
		saveReview(user, hairStyle);

		//when
		ResultActions actual = mockMvc.perform(
			MockMvcRequestBuilders
				.get("/api/reviews")
				.header(AUTHORIZATION, BEARER + getAccessToken(user.getId()))
				.param("size", "10")
		);

		//then
		actual.andExpectAll(
			status().isOk(),
			jsonPath("$.paging.hasNext").value(false),
			jsonPath("$.result.size()").value(3)
		);
	}

	@Test
	@DisplayName("[사용자 작성 리뷰조회 API 를 호출한다]")
	void findMyReviews() throws Exception {
		//given
		User user = saveUser();
		HairStyle hairStyle = saveHairStyle();

		saveReview(user, hairStyle);
		saveReview(user, hairStyle);
		saveReview(user, hairStyle);

		//when
		ResultActions actual = mockMvc.perform(
			MockMvcRequestBuilders
				.get("/api/reviews/my")
				.header(AUTHORIZATION, BEARER + getAccessToken(user.getId()))
				.param("size", "10")
		);

		//then
		actual.andExpectAll(
			status().isOk(),
			jsonPath("$.paging.hasNext").value(false),
			jsonPath("$.result.size()").value(3)
		);
	}

	@Test
	@DisplayName("[이달의 리뷰 조회 API 를 호출한다]")
	void findReviewOfMonth() throws Exception {
		//given
		User user = saveUser();
		HairStyle hairStyle = saveHairStyle();

		saveReview(user, hairStyle);
		saveReview(user, hairStyle);
		saveReview(user, hairStyle);

		//when
		ResultActions actual = mockMvc.perform(
			MockMvcRequestBuilders
				.get("/api/reviews/month")
				.header(AUTHORIZATION, BEARER + getAccessToken(user.getId()))
				.param("size", "10")
		);

		//then
		actual.andExpectAll(
			status().isOk(),
			jsonPath("$.result.size()").value(0)
		);
	}

	@Test
	@DisplayName("[특정 헤어스타일의 리뷰 조회 API 를 호출한다]")
	void findHairStyleReview() throws Exception {
		//given
		User user = saveUser();
		HairStyle hairStyle = saveHairStyle();

		saveReview(user, hairStyle);
		saveReview(user, hairStyle);
		saveReview(user, hairStyle);

		//when
		ResultActions actual = mockMvc.perform(
			MockMvcRequestBuilders
				.get("/api/reviews/hair_style/" + hairStyle.getId())
				.header(AUTHORIZATION, BEARER + getAccessToken(user.getId()))
		);

		//then
		actual.andExpectAll(
			status().isOk(),
			jsonPath("$.result.size()").value(3)
		);
	}
}