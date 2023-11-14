package com.inq.wishhair.wesharewishhair.review.presentation;

import static com.inq.wishhair.wesharewishhair.common.fixture.AuthFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.inq.wishhair.wesharewishhair.common.config.EmbeddedRedisConfig;
import com.inq.wishhair.wesharewishhair.common.support.ApiTestSupport;
import com.inq.wishhair.wesharewishhair.review.domain.likereview.LikeReview;
import com.inq.wishhair.wesharewishhair.review.domain.likereview.LikeReviewRepository;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;

@DisplayName("[LikeReview API 테스트]")
@Import(EmbeddedRedisConfig.class)
class LikeReviewControllerTest extends ApiTestSupport {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private LikeReviewRepository likeReviewRepository;

	@Test
	@DisplayName("[좋아요 실행 API 를 호출한다]")
	void executeLike() throws Exception {
		//given
		User user = saveUser();

		//when
		ResultActions actual = mockMvc.perform(
			MockMvcRequestBuilders
				.post("/api/reviews/like/1")
				.header(AUTHORIZATION, BEARER + getAccessToken(user.getId()))
		);

		//then
		actual.andExpect(status().isOk());
		boolean userExist = likeReviewRepository.existsByUserIdAndReviewId(user.getId(), 1L);
		assertThat(userExist).isTrue();
	}

	@Test
	@DisplayName("[좋아요 취소 API 를 호출한다]")
	void cancelLike() throws Exception {
		//given
		User user = saveUser();
		likeReviewRepository.save(LikeReview.addLike(user.getId(), 2L));

		//when
		ResultActions actual = mockMvc.perform(
			MockMvcRequestBuilders
				.delete("/api/reviews/like/2")
				.header(AUTHORIZATION, BEARER + getAccessToken(user.getId()))
		);

		//then
		actual.andExpect(status().isOk());
		boolean userExist = likeReviewRepository.existsByUserIdAndReviewId(user.getId(), 2L);
		assertThat(userExist).isFalse();
	}

	@Test
	@DisplayName("[좋아요 상태 확인 API 를 호출한다]")
	void checkIsLiking() throws Exception {
		//given
		User user = saveUser();
		likeReviewRepository.save(LikeReview.addLike(user.getId(), 3L));

		//when
		ResultActions actual = mockMvc.perform(
			MockMvcRequestBuilders
				.get("/api/reviews/like/3")
				.header(AUTHORIZATION, BEARER + getAccessToken(user.getId()))
		);

		//then
		actual.andExpectAll(
			status().isOk(),
			jsonPath("$.isLiking").value(true)
		);
	}
}