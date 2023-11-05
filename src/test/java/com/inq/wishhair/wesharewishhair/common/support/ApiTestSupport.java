package com.inq.wishhair.wesharewishhair.common.support;

import static com.inq.wishhair.wesharewishhair.common.fixture.AuthFixture.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inq.wishhair.wesharewishhair.auth.domain.AuthToken;
import com.inq.wishhair.wesharewishhair.auth.domain.AuthTokenManager;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public abstract class ApiTestSupport {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@MockBean
	protected AuthTokenManager authTokenManager;

	@BeforeEach
	public void setAuthorization() {
		given(authTokenManager.generate(any(Long.class))).willReturn(new AuthToken(TOKEN, TOKEN));
		given(authTokenManager.getId(anyString())).willReturn(1L);
	}

	protected void setAuthorization(Long userId) {
		given(authTokenManager.generate(any(Long.class))).willReturn(new AuthToken(TOKEN, TOKEN));
		given(authTokenManager.getId(anyString())).willReturn(userId);
	}

	public String toJson(Object object) throws JsonProcessingException {
		return objectMapper.writeValueAsString(object);
	}
}
