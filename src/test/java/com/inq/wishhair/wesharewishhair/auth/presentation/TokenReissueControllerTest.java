package com.inq.wishhair.wesharewishhair.auth.presentation;

import static com.inq.wishhair.wesharewishhair.common.fixture.AuthFixture.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.inq.wishhair.wesharewishhair.auth.application.TokenReissueService;
import com.inq.wishhair.wesharewishhair.auth.application.dto.response.TokenResponse;
import com.inq.wishhair.wesharewishhair.common.support.ApiTestSupport;

@DisplayName("[TokenReissueController 테스트] - API")
class TokenReissueControllerTest extends ApiTestSupport {

	private static final String REISSUE_TOKEN_URL = "/api/token/reissue";

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private TokenReissueService tokenReissueService;

	@Test
	@DisplayName("[토큰 재발급 API 를 호출한다]")
	void reissueToken() throws Exception {
		//given
		TokenResponse tokenResponse = new TokenResponse("accessToken", "refreshToken");
		given(tokenReissueService.reissueToken(1L, TOKEN))
			.willReturn(tokenResponse);

		//when
		ResultActions result = mockMvc.perform(
			MockMvcRequestBuilders
				.post(REISSUE_TOKEN_URL)
				.header(AUTHORIZATION, ACCESS_TOKEN)
		);

		//then
		result.andExpectAll(
			status().isOk(),
			jsonPath("$.accessToken").value(tokenResponse.accessToken()),
			jsonPath("$.refreshToken").value(tokenResponse.refreshToken())
		);
	}
}