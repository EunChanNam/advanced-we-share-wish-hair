package com.inq.wishhair.wesharewishhair.hairstyle.domain;

import static com.inq.wishhair.wesharewishhair.global.utils.PageableGenerator.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;

import com.inq.wishhair.wesharewishhair.common.support.RepositoryTestSupport;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.hashtag.Tag;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.wishhair.WishHair;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.wishhair.WishHairRepository;
import com.inq.wishhair.wesharewishhair.hairstyle.fixture.HairStyleFixture;
import com.inq.wishhair.wesharewishhair.hairstyle.utils.HairRecommendCondition;
import com.inq.wishhair.wesharewishhair.user.domain.entity.Sex;

@DisplayName("[HairStyleQueryRepository 테스트] - Domain")
public class HairStyleQueryRepositoryTest extends RepositoryTestSupport {

	@Autowired
	private HairStyleQueryRepository hairStyleQueryRepository;
	@Autowired
	private HairStyleRepository hairStyleRepository;
	@Autowired
	private WishHairRepository wishHairRepository;

	private List<HairStyle> hairStyles;

	@BeforeEach
	void setUp() {
		hairStyles = List.of(
			HairStyleFixture.getWomanHairStyle("name1", List.of(Tag.ROUND, Tag.CUTE, Tag.SIMPLE)),
			HairStyleFixture.getWomanHairStyle("name2", List.of(Tag.SQUARE, Tag.UPSTAGE, Tag.HARD)),
			HairStyleFixture.getWomanHairStyle("name3", List.of(Tag.ROUND, Tag.BANGS, Tag.SIMPLE)),
			HairStyleFixture.getWomanHairStyle("name4", List.of(Tag.ROUND, Tag.CURLY, Tag.SIMPLE))
		);

		hairStyles.forEach(hairStyle -> hairStyleRepository.save(hairStyle));
	}

	private void wishHairStyles(List<Integer> indexes) {
		indexes.forEach(index ->
			wishHairRepository.save(WishHair.createWishHair(1L, hairStyles.get(index).getId()))
		);
	}

	private void assertHairStylesMatch(List<HairStyle> actuals, List<HairStyle> expectedList) {
		assertThat(actuals).hasSameSizeAs(expectedList);
		for (int i = 0; i < actuals.size(); i++) {
			HairStyle actual = actuals.get(i);
			HairStyle expected = expectedList.get(i);

			assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	@DisplayName("헤어스타일을 태그와 성별, 얼굴형 태그를 통해서 헤어스타일을 조회한다")
	class findByRecommend {
		@Test
		@DisplayName("사용자의 얼굴형에 해당되지 않은 헤어스타일은 조회되지 않는다")
		void test1() {
			//given
			HairRecommendCondition condition = new HairRecommendCondition(
				List.of(Tag.CUTE, Tag.SIMPLE), Tag.HEART, Sex.WOMAN
			);

			//when
			List<HairStyle> actual = hairStyleQueryRepository.findByRecommend(condition, getDefaultPageable());

			//then
			assertHairStylesMatch(actual, List.of());
		}

		@Test
		@DisplayName("조회된 헤어스타일은 일치하는 해시태그의 개수, 이름으로 정렬된다")
		void test4() {
			//given
			HairRecommendCondition condition = new HairRecommendCondition(
				List.of(Tag.CUTE, Tag.SIMPLE), Tag.ROUND, Sex.WOMAN
			);

			//when
			List<HairStyle> actual = hairStyleQueryRepository.findByRecommend(condition, getDefaultPageable());

			//then
			assertHairStylesMatch(actual, List.of(hairStyles.get(0), hairStyles.get(2), hairStyles.get(3)));
		}
	}

	@Nested
	@DisplayName("얼굴형 헤어스타일 추천 쿼리")
	class findByFaceShape {
		@Test
		@DisplayName("얼굴형 태그로 헤어를 검색하고, 찜 수와 이름으로 정렬한다")
		void test5() {
			//given
			HairRecommendCondition condition = new HairRecommendCondition(
				null, Tag.ROUND, Sex.WOMAN
			);

			wishHairStyles(List.of(0, 0, 2, 3));

			//when
			List<HairStyle> result = hairStyleQueryRepository.findByFaceShape(condition, getDefaultPageable());

			//then
			assertHairStylesMatch(
				result,
				List.of(
					hairStyles.get(0),
					hairStyles.get(2),
					hairStyles.get(3)
				));
		}

		@Test
		@DisplayName("얼굴형 태그 없이 검색 후 찜 수와 이름으로 정렬한다")
		void test6() {
			//given
			HairRecommendCondition condition = new HairRecommendCondition(
				null, null, Sex.WOMAN
			);

			wishHairStyles(List.of(0, 1, 1, 2));

			//when
			List<HairStyle> result = hairStyleQueryRepository.findByFaceShape(condition, getDefaultPageable());

			//then
			assertHairStylesMatch(
				result,
				List.of(
					hairStyles.get(1),
					hairStyles.get(0),
					hairStyles.get(2),
					hairStyles.get(3)
				)
			);
		}
	}

	@Test
	@DisplayName("찜한 헤어스타일을 생성된 순서로 조회한다")
	void success() {
		//given
		wishHairStyles(List.of(3, 0, 1));

		//when
		Slice<HairStyle> result = hairStyleQueryRepository.findByWish(1L, getDefaultPageable());

		//then
		assertThat(result.hasNext()).isFalse();
		assertHairStylesMatch(
			result.getContent(),
			List.of(
				hairStyles.get(1),
				hairStyles.get(0),
				hairStyles.get(3)
			)
		);
	}
}
