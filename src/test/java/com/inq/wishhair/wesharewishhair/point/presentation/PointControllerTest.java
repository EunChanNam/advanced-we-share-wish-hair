package com.inq.wishhair.wesharewishhair.point.presentation;

import static com.inq.wishhair.wesharewishhair.common.fixture.AuthFixture.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

@DisplayName("[PointController 테스트 - API]")
class PointControllerTest extends ApiTestSupport {

	private static final String POINT_USE_URL = "/api/points/use";
	private static final String POINT_QUERY_URL = "/api/points";

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
		Long userId = userRepository.save(user).getId();
		pointLogRepository.save(PointLogFixture.getUsePointLog(user));

		//when
		ResultActions result = mockMvc.perform(
			MockMvcRequestBuilders
				.post(POINT_USE_URL)
				.header(AUTHORIZATION, BEARER + getAccessToken(userId))
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJson(PointLogFixture.getPointUseRequest()))
		);

		//then
		result.andExpect(status().isOk());
	}

	@Test
	@DisplayName("[포인트 로그를 페이징 정보를 통해 조회한다]")
	void findPointHistories() throws Exception {
		//given
		User user = UserFixture.getFixedManUser();
		Long userId = userRepository.save(user).getId();
		pointLogRepository.save(PointLogFixture.getUsePointLog(user));

		//when
		ResultActions result = mockMvc.perform(
			MockMvcRequestBuilders
				.get(POINT_QUERY_URL)
				.header(AUTHORIZATION, BEARER + getAccessToken(userId))
		);

		//then
		result.andExpectAll(
			status().isOk(),
			jsonPath("$.paging").exists(),
			jsonPath("$.result").isNotEmpty(),
			jsonPath("$.result.size()").value(1)
		);
	}
}