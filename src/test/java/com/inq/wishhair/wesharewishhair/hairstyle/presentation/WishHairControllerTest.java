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
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;
import com.inq.wishhair.wesharewishhair.user.fixture.UserFixture;

@DisplayName("[WishHairApi 테스트]")
class WishHairControllerTest extends ApiTestSupport {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private HairStyleRepository hairStyleRepository;
	@Autowired
	private WishHairRepository wishHairRepository;

	private final HairStyle hairStyle = HairStyleFixture.getWomanHairStyle(
		"A", List.of(Tag.CUTE, Tag.LIGHT, Tag.OBLONG)
	);

	@BeforeEach
	void setUp() {
		hairStyleRepository.save(hairStyle);
	}

	private Long setUser() {
		User user = UserFixture.getFixedWomanUser();
		return userRepository.save(user).getId();
	}

	@Test
	@DisplayName("[찜 API 를 호출한다]")
	void executeWish() throws Exception {
		//given
		Long userId = setUser();

		//when
		ResultActions actual = mockMvc.perform(
			MockMvcRequestBuilders
				.post("/api/hair_styles/wish/" + hairStyle.getId())
				.header(AUTHORIZATION, BEARER + getAccessToken(userId))
		);

		//then
		actual.andExpect(status().isOk());
	}

	@Test
	@DisplayName("[찜 취소 API 를 호출한다]")
	void cancelWish() throws Exception {
		//given
		Long userId = setUser();
		wishHairRepository.save(WishHair.createWishHair(userId, hairStyle.getId()));

		//when
		ResultActions actual = mockMvc.perform(
			MockMvcRequestBuilders
				.delete("/api/hair_styles/wish/" + hairStyle.getId())
				.header(AUTHORIZATION, BEARER + getAccessToken(userId))
		);

		//then
		actual.andExpect(status().isOk());
	}

	@Test
	@DisplayName("[찜 확인 API 를 호출한다]")
	void checkIsWishing() throws Exception {
		//given
		Long userId = setUser();
		wishHairRepository.save(WishHair.createWishHair(userId, hairStyle.getId()));

		//when
		ResultActions actual = mockMvc.perform(
			MockMvcRequestBuilders
				.get("/api/hair_styles/wish/" + hairStyle.getId())
				.header(AUTHORIZATION, BEARER + getAccessToken(userId))
		);

		//then
		actual.andExpectAll(
			status().isOk(),
			jsonPath("$.isWishing").value(true)
		);
	}
}