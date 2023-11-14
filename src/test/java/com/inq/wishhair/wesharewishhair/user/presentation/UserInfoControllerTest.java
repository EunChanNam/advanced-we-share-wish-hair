package com.inq.wishhair.wesharewishhair.user.presentation;

import static com.inq.wishhair.wesharewishhair.common.fixture.AuthFixture.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.inq.wishhair.wesharewishhair.common.support.ApiTestSupport;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;

@DisplayName("[UserInfoController 테스트] - API")
class UserInfoControllerTest extends ApiTestSupport {

	private static final String BASE_URL = "/api/users";
	private static final String MY_PAGE_URL = BASE_URL + "/my_page";
	private static final String USER_INFORMATION = BASE_URL + "/info";
	private static final String USER_HOME_INFO = BASE_URL + "/home_info";

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("[마이페이지 정보 조회 API 를 호출한다]")
	void getMyPageInfo() throws Exception {
		//given
		User user = saveUser();

		//when
		ResultActions actual = mockMvc.perform(
			MockMvcRequestBuilders
				.get(MY_PAGE_URL)
				.header(AUTHORIZATION, BEARER + getAccessToken(user.getId()))
		);

		//then
		actual.andExpectAll(
			status().isOk(),
			jsonPath("$.nickname").value(user.getNicknameValue()),
			jsonPath("$.sex").value(user.getSex().name()),
			jsonPath("$.point").value(0),
			jsonPath("$.reviews.size()").value(0)
		);
	}

	@Test
	@DisplayName("[회원정보 조회 API 를 호출한다]")
	void getUserInformation() throws Exception {
		//given
		User user = saveUser();

		//when
		ResultActions actual = mockMvc.perform(
			MockMvcRequestBuilders
				.get(USER_INFORMATION)
				.header(AUTHORIZATION, BEARER + getAccessToken(user.getId()))
		);

		//then
		actual.andExpectAll(
			status().isOk(),
			jsonPath("$.email").value(user.getEmailValue()),
			jsonPath("$.name").value(user.getName()),
			jsonPath("$.nickname").value(user.getNicknameValue()),
			jsonPath("$.sex").value(user.getSex().name())
		);
	}

	@Test
	@DisplayName("[홈페이지 회원정보 조회 API 를 호출한다]")
	void getUserInfo() throws Exception {
		//given
		User user = saveUser();

		//when
		ResultActions actual = mockMvc.perform(
			MockMvcRequestBuilders
				.get(USER_HOME_INFO)
				.header(AUTHORIZATION, BEARER + getAccessToken(user.getId()))
		);

		//then
		actual.andExpectAll(
			status().isOk(),
			jsonPath("$.nickname").value(user.getNicknameValue()),
			jsonPath("$.hasFaceShape").value(false)
		);
	}
}