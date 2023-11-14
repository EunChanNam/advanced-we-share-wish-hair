package com.inq.wishhair.wesharewishhair.review.config.converter;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;
import com.inq.wishhair.wesharewishhair.review.domain.entity.Score;

@DisplayName("[ScoreConverter 테스트]")
class ScoreConverterTest {

	private final ScoreConverter scoreConverter = new ScoreConverter();

	@Nested
	@DisplayName("[리뷰 점수를 enum 으로 변환한다]")
	class convert {

		@ParameterizedTest(name = "{0}")
		@ValueSource(strings = {"0.0", "0.5", "1.0", "1.5", "2.0", "2.5", "3.0", "3.5", "4.0", "4.5"})
		@DisplayName("[성공적으로 변환한다]")
		void success(String score) {
			//when
			Score actual = scoreConverter.convert(score);

			//then
			assertThat(actual).isNotNull();
			assertThat(actual.getValue()).isEqualTo(score);
		}

		@Test
		@DisplayName("[잘못된 입력으로 실패한다]")
		void fail() {
			//given
			String score = "10.0";

			//when
			ThrowingCallable when = () -> scoreConverter.convert(score);

			//then
			assertThatThrownBy(when)
				.isInstanceOf(WishHairException.class)
				.hasMessageContaining(ErrorCode.SCORE_MISMATCH.getMessage());
		}
	}
}