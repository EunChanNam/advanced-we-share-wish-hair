package com.inq.wishhair.wesharewishhair.user.presentation;

import static com.inq.wishhair.wesharewishhair.common.fixture.AuthFixture.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import com.inq.wishhair.wesharewishhair.common.support.ApiTestSupport;
import com.inq.wishhair.wesharewishhair.common.utils.FileMockingUtils;
import com.inq.wishhair.wesharewishhair.user.application.UserService;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;
import com.inq.wishhair.wesharewishhair.user.fixture.UserFixture;
import com.inq.wishhair.wesharewishhair.user.presentation.dto.request.PasswordRefreshRequest;
import com.inq.wishhair.wesharewishhair.user.presentation.dto.request.PasswordUpdateRequest;
import com.inq.wishhair.wesharewishhair.user.presentation.dto.request.SignUpRequest;
import com.inq.wishhair.wesharewishhair.user.presentation.dto.request.UserUpdateRequest;

@DisplayName("[UserController 테스트] - API")
class UserControllerTest extends ApiTestSupport {

	private static final String BASE_URL = "/api/users";
	private static final String PASSWORD_REFRESH_URL = BASE_URL + "/refresh/password";
	private static final String PASSWORD_UPDATE_URL = BASE_URL + "/password";
	private static final String UPDATE_FACE_SHAPE_URL = BASE_URL + "/face_shape";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@Test
	@DisplayName("[회원가입 API 를 호출한다]")
	void signUp() throws Exception {
		//given
		SignUpRequest request = UserFixture.getSignUpRequest();
		given(userService.createUser(request)).willReturn(1L);

		//then
		ResultActions actual = mockMvc.perform(
			MockMvcRequestBuilders
				.post(BASE_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJson(request))
		);

		//then
		actual.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("[회원탈퇴 API 를 호출한다]")
	void deleteUser() throws Exception {
		//given
		User user = saveUser();

		//then
		ResultActions actual = mockMvc.perform(
			MockMvcRequestBuilders
				.delete(BASE_URL)
				.header(AUTHORIZATION, BEARER + getAccessToken(user.getId()))
		);

		//then
		actual.andExpect(status().isOk());
	}

	@Test
	@DisplayName("[비밀번호 갱신(비밀번호 찾기) API 를 호출한다]")
	void refreshPassword() throws Exception {
		//given
		PasswordRefreshRequest request = UserFixture.getPasswordRefreshRequest();

		//when
		ResultActions actual = mockMvc.perform(
			MockMvcRequestBuilders
				.patch(PASSWORD_REFRESH_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJson(request))
		);

		//then
		actual.andExpect(status().isOk());
	}

	@Test
	@DisplayName("[회원정보 수정 API 를 호출한다]")
	void updateUser() throws Exception {
		//given
		User user = saveUser();
		UserUpdateRequest request = UserFixture.getUserUpdateRequest();

		//when
		ResultActions actual = mockMvc.perform(
			MockMvcRequestBuilders
				.patch(BASE_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJson(request))
				.header(AUTHORIZATION, BEARER + getAccessToken(user.getId()))
		);

		//then
		actual.andExpect(status().isOk());
	}

	@Test
	@DisplayName("[비밀번호 변경 API 를 호출한다]")
	void updatePassword() throws Exception {
		//given
		User user = saveUser();
		PasswordUpdateRequest request = UserFixture.getPasswordUpdateRequest();

		//when
		ResultActions actual = mockMvc.perform(
			MockMvcRequestBuilders
				.patch(PASSWORD_UPDATE_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJson(request))
				.header(AUTHORIZATION, BEARER + getAccessToken(user.getId()))
		);

		//then
		actual.andExpect(status().isOk());
	}

	@Test
	@DisplayName("[얼굴형 정보 업데이트 API 를 호출한다]")
	void updateFaceShape() throws Exception {
		//given
		User user = saveUser();
		MultipartFile file = FileMockingUtils.createMockMultipartFile("hello1.jpg");

		//when
		ResultActions actual = mockMvc.perform(
			MockMvcRequestBuilders
				.multipart(HttpMethod.PATCH, UPDATE_FACE_SHAPE_URL)
				.file((MockMultipartFile)file)
				.header(AUTHORIZATION, BEARER + getAccessToken(user.getId()))
		);

		//then
		actual.andExpect(status().isOk());
	}
}