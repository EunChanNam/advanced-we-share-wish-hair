package com.inq.wishhair.wesharewishhair.auth.presentation;

import static com.inq.wishhair.wesharewishhair.common.fixture.AuthFixture.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.inq.wishhair.wesharewishhair.auth.application.AuthService;
import com.inq.wishhair.wesharewishhair.auth.application.dto.response.LoginResponse;
import com.inq.wishhair.wesharewishhair.auth.presentation.dto.request.LoginRequest;
import com.inq.wishhair.wesharewishhair.common.support.ApiTestSupport;
import com.inq.wishhair.wesharewishhair.global.config.SecurityConfig;

@WebMvcTest(value = {AuthController.class, SecurityConfig.class})
@DisplayName("[AuthController 테스트] - API")
class AuthControllerTest extends ApiTestSupport {

	private static final String LOGIN_URL = "/api/auth/login";
	private static final String LOGOUT_URL = "/api/auth/logout";

	@MockBean
	private AuthService authService;

	@Test
	@DisplayName("[로그인 API 를 호출한다]")
	void login() throws Exception {
		//given
		String email = "email";
		String pw = "pw";
		LoginRequest loginRequest = new LoginRequest(email, pw);

		LoginResponse loginResponse = new LoginResponse("token", "token");
		given(authService.login(email, pw))
			.willReturn(loginResponse);

		//when
		ResultActions result = mockMvc.perform(
			MockMvcRequestBuilders
				.post(LOGIN_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJson(loginRequest))

		);

		//then
		result.andExpectAll(
			jsonPath("$.accessToken").value(loginResponse.accessToken()),
			jsonPath("$.refreshToken").value(loginResponse.refreshToken())
		);
	}

	@Test
	@DisplayName("[로그아웃 API 를 호출한다]")
	void logout() throws Exception {
		//when
		ResultActions result = mockMvc.perform(
			MockMvcRequestBuilders
				.post(LOGOUT_URL)
				.header(AUTHORIZATION, ACCESS_TOKEN)
		);

		//then
		result.andExpect(status().isOk());
	}
}