package com.inq.wishhair.wesharewishhair.user.presentation;

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

import com.inq.wishhair.wesharewishhair.common.support.ApiTestSupport;
import com.inq.wishhair.wesharewishhair.user.application.UserInfoService;
import com.inq.wishhair.wesharewishhair.user.application.dto.response.MyPageResponse;
import com.inq.wishhair.wesharewishhair.user.application.dto.response.UserInfo;
import com.inq.wishhair.wesharewishhair.user.application.dto.response.UserInformation;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;
import com.inq.wishhair.wesharewishhair.user.fixture.UserFixture;

@DisplayName("[UserInfoController 테스트] - API")
class UserInfoControllerTest extends ApiTestSupport {

	private static final String BASE_URL = "/api/users";
	private static final String MY_PAGE_URL = BASE_URL + "/my_page";
	private static final String USER_INFORMATION = BASE_URL + "/info";
	private static final String USER_HOME_INFO = BASE_URL + "/home_info";

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private UserInfoService userInfoService;

	@Test
	@DisplayName("[마이페이지 정보 조회 API 를 호출한다]")
	void getMyPageInfo() throws Exception {
		//given
		User user = saveUser();

		MyPageResponse response = UserFixture.getMyPageResponse();
		given(userInfoService.getMyPageInfo(user.getId()))
			.willReturn(response);

		//when
		ResultActions actual = mockMvc.perform(
			MockMvcRequestBuilders
				.get(MY_PAGE_URL)
				.header(AUTHORIZATION, BEARER + getAccessToken(user.getId()))
		);

		//then
		actual.andExpectAll(
			status().isOk(),
			jsonPath("$.nickname").value(response.nickname()),
			jsonPath("$.sex").value(response.sex().name()),
			jsonPath("$.point").value(response.point()),
			jsonPath("$.reviews.size()").value(0)
		);
	}

	@Test
	@DisplayName("[회원정보 조회 API 를 호출한다]")
	void getUserInformation() throws Exception {
		//given
		User user = saveUser();

		UserInformation response = UserFixture.getUserInformation();
		given(userInfoService.getUserInformation(user.getId()))
			.willReturn(response);

		//when
		ResultActions actual = mockMvc.perform(
			MockMvcRequestBuilders
				.get(USER_INFORMATION)
				.header(AUTHORIZATION, BEARER + getAccessToken(user.getId()))
		);

		//then
		actual.andExpectAll(
			status().isOk(),
			jsonPath("$.email").value(response.email()),
			jsonPath("$.name").value(response.name()),
			jsonPath("$.nickname").value(response.nickname()),
			jsonPath("$.sex").value(response.sex().name())
		);
	}

	@Test
	@DisplayName("[홈페이지 회원정보 조회 API 를 호출한다]")
	void getUserInfo() throws Exception {
		//given
		User user = saveUser();

		UserInfo response = UserFixture.getUserInfo();
		given(userInfoService.getUserInfo(user.getId()))
			.willReturn(response);

		//when
		ResultActions actual = mockMvc.perform(
			MockMvcRequestBuilders
				.get(USER_HOME_INFO)
				.header(AUTHORIZATION, BEARER + getAccessToken(user.getId()))
		);

		//then
		actual.andExpectAll(
			status().isOk(),
			jsonPath("$.nickname").value(response.nickname()),
			jsonPath("$.faceShapeTag").value(response.faceShapeTag()),
			jsonPath("$.hasFaceShape").value(response.hasFaceShape())
		);
	}
}