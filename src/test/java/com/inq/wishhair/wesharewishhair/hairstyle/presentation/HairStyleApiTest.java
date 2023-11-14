package com.inq.wishhair.wesharewishhair.hairstyle.presentation;

import static com.inq.wishhair.wesharewishhair.common.fixture.AuthFixture.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.inq.wishhair.wesharewishhair.common.support.ApiTestSupport;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.HairStyle;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.HairStyleRepository;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.hashtag.Tag;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.wishhair.WishHair;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.wishhair.WishHairRepository;
import com.inq.wishhair.wesharewishhair.hairstyle.fixture.HairStyleFixture;
import com.inq.wishhair.wesharewishhair.user.domain.UserRepository;
import com.inq.wishhair.wesharewishhair.user.domain.entity.FaceShape;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;
import com.inq.wishhair.wesharewishhair.user.fixture.UserFixture;

@DisplayName("[HairStyle Api 테스트]")
class HairStyleApiTest extends ApiTestSupport {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private HairStyleRepository hairStyleRepository;
	@Autowired
	private WishHairRepository wishHairRepository;

	List<HairStyle> hairStyles = List.of(
		HairStyleFixture.getWomanHairStyle("A", List.of(Tag.CUTE, Tag.LIGHT, Tag.OBLONG)),
		HairStyleFixture.getWomanHairStyle("B", List.of(Tag.CUTE, Tag.BANGS, Tag.OBLONG)),
		HairStyleFixture.getWomanHairStyle("C", List.of(Tag.CUTE, Tag.SIMPLE, Tag.ROUND))
	);

	@BeforeEach
	void setUp() {
		hairStyles.forEach(hairStyle -> hairStyleRepository.save(hairStyle));
	}

	@Test
	@DisplayName("[헤어스타일 메인 추천 API 를 호출한다]")
	void respondRecommendedHairStyle() throws Exception {
		//given
		User user = UserFixture.getFixedWomanUser();
		user.updateFaceShape(new FaceShape(Tag.OBLONG));
		Long userId = userRepository.save(user).getId();

		//when
		ResultActions actual = mockMvc.perform(
			MockMvcRequestBuilders
				.get("/api/hair_styles/recommend?tags=CUTE&tags=LIGHT")
				.header(AUTHORIZATION, BEARER + getAccessToken(userId))
		);

		//then
		actual.andExpectAll(
			status().isOk(),
			jsonPath("$.result.size()").value(2)
		);
	}

	@Test
	@DisplayName("[헤어스타일 홈화면 추천 API 를 호출한다]")
	void findHairStyleByFaceShape() throws Exception {
		//given
		User user = UserFixture.getFixedWomanUser();
		user.updateFaceShape(new FaceShape(Tag.OBLONG));
		Long userId = userRepository.save(user).getId();

		//when
		ResultActions actual = mockMvc.perform(
			MockMvcRequestBuilders
				.get("/api/hair_styles/home")
				.header(AUTHORIZATION, BEARER + getAccessToken(userId))
		);

		//then
		actual.andExpectAll(
			status().isOk(),
			jsonPath("$.result.size()").value(2)
		);
	}

	@Test
	@DisplayName("[찜한 헤어스타일 조회 API 를 호출한다]")
	void findWishHairStyles() throws Exception {
		//given
		User user = UserFixture.getFixedWomanUser();
		Long userId = userRepository.save(user).getId();

		wishHairRepository.save(WishHair.createWishHair(userId, hairStyles.get(0).getId()));
		wishHairRepository.save(WishHair.createWishHair(userId, hairStyles.get(2).getId()));

		//when
		ResultActions actual = mockMvc.perform(
			MockMvcRequestBuilders
				.get("/api/hair_styles/wish")
				.header(AUTHORIZATION, BEARER + getAccessToken(userId))
		);

		//then
		actual.andExpectAll(
			status().isOk(),
			jsonPath("$.result.size()").value(2)
		);
	}

	@Test
	@DisplayName("[모든 헤어스타일 간단한 정보 조회 API 를 호출한다]")
	void findAllHairStyles() throws Exception {
		//given
		User user = UserFixture.getFixedWomanUser();
		Long userId = userRepository.save(user).getId();

		//when
		ResultActions actual = mockMvc.perform(
			MockMvcRequestBuilders
				.get("/api/hair_styles")
				.header(AUTHORIZATION, BEARER + getAccessToken(userId))
		);

		//then
		actual.andExpectAll(
			status().isOk(),
			jsonPath("$.result.size()").value(3)
		);
	}
}