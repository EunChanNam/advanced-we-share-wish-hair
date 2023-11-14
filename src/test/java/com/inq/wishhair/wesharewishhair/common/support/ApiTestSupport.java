package com.inq.wishhair.wesharewishhair.common.support;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inq.wishhair.wesharewishhair.auth.domain.AuthTokenManager;
import com.inq.wishhair.wesharewishhair.user.domain.UserRepository;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;
import com.inq.wishhair.wesharewishhair.user.fixture.UserFixture;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public abstract class ApiTestSupport {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private AuthTokenManager authTokenManager;
	@Autowired
	private UserRepository userRepository;

	@MockBean
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	void setPasswordEncoder() {
		given(passwordEncoder.encode(any())).willReturn("password");
		given(passwordEncoder.matches(any(), any())).willReturn(true);
	}

	protected String getAccessToken(Long userId) {
		return authTokenManager.generate(userId).accessToken();
	}

	protected User saveUser() {
		User user = UserFixture.getFixedWomanUser();
		return userRepository.save(user);
	}

	public String toJson(Object object) throws JsonProcessingException {
		return objectMapper.writeValueAsString(object);
	}
}
