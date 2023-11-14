package com.inq.wishhair.wesharewishhair.review.presentation;

import static com.inq.wishhair.wesharewishhair.common.fixture.AuthFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.inq.wishhair.wesharewishhair.common.support.ApiTestSupport;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.HairStyle;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.HairStyleRepository;
import com.inq.wishhair.wesharewishhair.hairstyle.fixture.HairStyleFixture;
import com.inq.wishhair.wesharewishhair.photo.domain.PhotoStore;
import com.inq.wishhair.wesharewishhair.review.application.dto.request.ReviewCreateRequest;
import com.inq.wishhair.wesharewishhair.review.application.dto.request.ReviewUpdateRequest;
import com.inq.wishhair.wesharewishhair.review.domain.ReviewRepository;
import com.inq.wishhair.wesharewishhair.review.domain.entity.Review;
import com.inq.wishhair.wesharewishhair.review.fixture.ReviewFixture;
import com.inq.wishhair.wesharewishhair.review.infrastructure.ReviewJpaRepository;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;

@DisplayName("[Review API 테스트]")
class ReviewApiTest extends ApiTestSupport {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private ReviewJpaRepository reviewJpaRepository;
	@Autowired
	private HairStyleRepository hairStyleRepository;

	@MockBean
	private PhotoStore photoStore;

	private Review saveReview(User user) {
		HairStyle hairStyle = HairStyleFixture.getWomanHairStyle();
		hairStyleRepository.save(hairStyle);

		Review review = ReviewFixture.getReview(hairStyle, user);
		reviewRepository.save(review);
		return review;
	}

	@Test
	@DisplayName("[리뷰 생성 API 를 호출한다]")
	void createReview() throws Exception {
		//given
		User user = saveUser();

		HairStyle hairStyle = HairStyleFixture.getWomanHairStyle();
		hairStyleRepository.save(hairStyle);

		given(photoStore.uploadFiles(anyList()))
			.willReturn(List.of("url1"));

		ReviewCreateRequest request = ReviewFixture.getReviewCreateRequest(hairStyle.getId());

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("contents", request.contents());
		params.add("score", String.valueOf(request.score()));
		params.add("hairStyleId", String.valueOf(request.hairStyleId()));

		//when
		ResultActions actual = mockMvc.perform(
			MockMvcRequestBuilders
				.multipart(HttpMethod.POST, "/api/reviews")
				.file((MockMultipartFile)request.files().get(0))
				.header(AUTHORIZATION, BEARER + getAccessToken(user.getId()))
				.params(params)
		);

		//then
		actual.andExpect(status().isCreated());
		assertThat(reviewJpaRepository.findAll()).hasSize(1);
	}

	@Test
	@DisplayName("[리뷰 삭제 API 를 호출한다]")
	void deleteReview() throws Exception {
		//given
		given(photoStore.deleteFiles(anyList())).willReturn(true);
		given(photoStore.uploadFiles(anyList())).willReturn(List.of("url1"));

		User user = saveUser();
		Review review = saveReview(user);

		//when
		ResultActions actual = mockMvc.perform(
			MockMvcRequestBuilders
				.delete("/api/reviews/" + review.getId())
				.header(AUTHORIZATION, BEARER + getAccessToken(user.getId()))
		);

		//then
		actual.andExpect(status().isOk());
		assertThat(reviewRepository.findById(review.getId())).isNotPresent();
	}

	@Test
	@DisplayName("[리뷰 수정 API 를 호출한다]")
	void updateReview() throws Exception {
		//given
		given(photoStore.uploadFiles(anyList())).willReturn(List.of("url1"));

		User user = saveUser();
		Review review = saveReview(user);

		ReviewUpdateRequest request = ReviewFixture.getReviewUpdateRequest(review.getId());

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("contents", request.contents());
		params.add("score", String.valueOf(request.score()));
		params.add("reviewId", String.valueOf(request.reviewId()));

		//when
		ResultActions actual = mockMvc.perform(
			MockMvcRequestBuilders
				.multipart(HttpMethod.PATCH, "/api/reviews")
				.file((MockMultipartFile)request.files().get(0))
				.header(AUTHORIZATION, BEARER + getAccessToken(user.getId()))
				.params(params)
		);

		//then
		actual.andExpect(status().isOk());
		Review findReview = reviewRepository.findById(review.getId()).orElseThrow();
		assertAll(
			() -> assertThat(findReview.getScore()).isEqualTo(request.score()),
			() -> assertThat(findReview.getContentsValue()).isEqualTo(request.contents()),
			() -> assertThat(findReview.getPhotos().get(0).getStoreUrl()).isEqualTo("url1")
		);
	}
}