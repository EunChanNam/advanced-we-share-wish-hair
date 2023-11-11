package com.inq.wishhair.wesharewishhair.auth.presentation;

import static com.inq.wishhair.wesharewishhair.common.fixture.AuthFixture.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.inq.wishhair.wesharewishhair.auth.domain.TokenRepository;
import com.inq.wishhair.wesharewishhair.auth.domain.entity.Token;
import com.inq.wishhair.wesharewishhair.common.support.ApiTestSupport;
import com.inq.wishhair.wesharewishhair.user.domain.UserRepository;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;
import com.inq.wishhair.wesharewishhair.user.fixture.UserFixture;

@DisplayName("[TokenReissueController 테스트] - API")
class TokenReissueControllerTest extends ApiTestSupport {

	private static final String REISSUE_TOKEN_URL = "/api/tokens/reissue";

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TokenRepository tokenRepository;

	@Test
	@DisplayName("[토큰 재발급 API 를 호출한다]")
	void reissueToken() throws Exception {
		//given
		User user = UserFixture.getFixedManUser();
		Long userId = userRepository.save(user).getId();

		String token = getAccessToken(userId);
		tokenRepository.save(Token.issue(userId, token));

		//when
		ResultActions result = mockMvc.perform(
			MockMvcRequestBuilders
				.post(REISSUE_TOKEN_URL)
				.header(AUTHORIZATION, BEARER + token)
		);

		//then
		result.andExpectAll(
			status().isOk(),
			jsonPath("$.accessToken").exists(),
			jsonPath("$.refreshToken").exists()
		);
	}
}