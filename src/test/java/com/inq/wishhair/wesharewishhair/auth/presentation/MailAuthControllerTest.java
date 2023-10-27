package com.inq.wishhair.wesharewishhair.auth.presentation;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.inq.wishhair.wesharewishhair.auth.application.MailAuthService;
import com.inq.wishhair.wesharewishhair.auth.presentation.dto.request.AuthKeyRequest;
import com.inq.wishhair.wesharewishhair.auth.presentation.dto.request.MailRequest;
import com.inq.wishhair.wesharewishhair.common.support.ApiTestSupport;
import com.inq.wishhair.wesharewishhair.global.config.SecurityConfig;
import com.inq.wishhair.wesharewishhair.user.application.utils.UserValidator;

@WebMvcTest(value = {MailAuthController.class, SecurityConfig.class})
@DisplayName("[MailAuthController 테스트] - API")
class MailAuthControllerTest extends ApiTestSupport {

	private static final String CHECK_DUPLICATED_EMAIL_URL = "/api/email/check";
	private static final String SEND_AUTHORIZATION_MAIL_URL = "/api/email/send";
	private static final String AUTHORIZE_KEY_URL = "/api/email/validate";
	private static final String EMAIL = "hello@naver.com";

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private UserValidator userValidator;
	@MockBean
	private MailAuthService mailAuthService;

	@Test
	@DisplayName("[이메일 중복검사 API 를 호출한다]")
	void checkDuplicateEmail() throws Exception {
		//given
		MailRequest mailRequest = new MailRequest(EMAIL);

		//when
		ResultActions result = mockMvc.perform(
			MockMvcRequestBuilders
				.post(CHECK_DUPLICATED_EMAIL_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJson(mailRequest))
		);

		//then
		result.andExpect(status().isOk());
	}

	@Test
	@DisplayName("[인증 메일 발송 API 를 호출한다]")
	void sendAuthorizationMail() throws Exception {
		//given
		MailRequest mailRequest = new MailRequest(EMAIL);

		//when
		ResultActions result = mockMvc.perform(
			MockMvcRequestBuilders
				.post(SEND_AUTHORIZATION_MAIL_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJson(mailRequest))
		);

		//then
		result.andExpect(status().isOk());
	}

	@Test
	@DisplayName("[인증코드 확인 API 를 호출한다]")
	void authorizeKey() throws Exception {
		//given
		AuthKeyRequest authKeyRequest = new AuthKeyRequest(EMAIL, "authcode");

		//when
		ResultActions result = mockMvc.perform(
			MockMvcRequestBuilders
				.post(AUTHORIZE_KEY_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJson(authKeyRequest))
		);

		//then
		result.andExpect(status().isOk());
	}
}