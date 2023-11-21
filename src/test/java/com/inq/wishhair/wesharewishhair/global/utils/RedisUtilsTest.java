package com.inq.wishhair.wesharewishhair.global.utils;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import com.inq.wishhair.wesharewishhair.common.support.TestContainerSupport;

/**
 * 주의사항 : 각 테스트 케이스별로 저장하는 데이터를 다르게 해야함(레디스 서버는 복구가 안되기 때문)
 */
@SpringBootTest
@DisplayName("[RedisUtils 테스트]")
class RedisUtilsTest extends TestContainerSupport {

	@Autowired
	private RedisUtils redisUtils;

	@Autowired
	private RedisTemplate<String, Long> redisTemplate;

	@Test
	@DisplayName("[Redis 서버에 데이터를 저장한다]")
	void setData() {
		//when
		redisUtils.setData(1L, 10L);

		//then
		Long expected = redisTemplate.opsForValue().get(String.valueOf(1L));
		assertThat(expected).isEqualTo(10L);
	}

	@Test
	@DisplayName("[key 해당하는 값을 1 증가 시킨다]")
	void increaseData() {
		//given
		redisUtils.setData(2L, 10L);

		//when
		redisUtils.increaseData(2L);

		//then
		Long expected = redisTemplate.opsForValue().get(String.valueOf(2L));
		assertThat(expected).isEqualTo(11L);
	}

	@Test
	@DisplayName("[key 해당하는 값을 1 감소 시킨다]")
	void decreaseData() {
		//given
		redisUtils.setData(3L, 10L);

		//when
		redisUtils.decreaseData(3L);

		//then
		Long expected = redisTemplate.opsForValue().get(String.valueOf(3L));
		assertThat(expected).isEqualTo(9L);
	}

	@Test
	@DisplayName("[key 해당하는 값을 가져온다]")
	void getData() {
		//given
		redisUtils.setData(4L, 10L);

		//when
		Optional<Long> actual = redisUtils.getData(4L);

		//then
		assertThat(actual).contains(10L);
	}
}