package com.inq.wishhair.wesharewishhair.common.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
