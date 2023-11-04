package com.inq.wishhair.wesharewishhair.point.presentation;

import static com.inq.wishhair.wesharewishhair.common.fixture.AuthFixture.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.inq.wishhair.wesharewishhair.common.support.ApiTestSupport;
import com.inq.wishhair.wesharewishhair.point.domain.PointLogRepository;
import com.inq.wishhair.wesharewishhair.point.fixture.PointLogFixture;
import com.inq.wishhair.wesharewishhair.user.domain.UserRepository;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;
import com.inq.wishhair.wesharewishhair.user.fixture.UserFixture;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("[PointController 테스트 - API]")
class PointControllerTest extends ApiTestSupport {

	private static final String POINT_USE_URL = "/api/points/use";

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private PointLogRepository pointLogRepository;
	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("[포인트 사용 API 를 호출한다]")
	void usePoint() throws Exception {
		//given
		User user = UserFixture.getFixedManUser();
		userRepository.save(user);
		pointLogRepository.save(PointLogFixture.getUsePointLog(user));

		//when
		ResultActions result = mockMvc.perform(
			MockMvcRequestBuilders
				.post(POINT_USE_URL)
				.header(AUTHORIZATION, ACCESS_TOKEN)
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJson(PointLogFixture.getPointUseRequest()))
		);

		//then
		result.andExpect(status().isOk());
	}
}