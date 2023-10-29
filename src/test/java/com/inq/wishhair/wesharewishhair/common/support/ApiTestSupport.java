package com.inq.wishhair.wesharewishhair.common.support;

import static com.inq.wishhair.wesharewishhair.common.fixture.AuthFixture.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inq.wishhair.wesharewishhair.auth.domain.AuthToken;
import com.inq.wishhair.wesharewishhair.auth.domain.AuthTokenManager;

public abstract class ApiTestSupport {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	protected MockMvc mockMvc;

	@MockBean
	protected AuthTokenManager authTokenManager;

	@BeforeEach
	public void setAuthorization() {
		given(authTokenManager.generate(any(Long.class))).willReturn(new AuthToken(TOKEN, TOKEN));
		given(authTokenManager.getId(anyString())).willReturn(1L);
	}

	public String toJson(Object object) throws JsonProcessingException {
		return objectMapper.writeValueAsString(object);
	}
}
