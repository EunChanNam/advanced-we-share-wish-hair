package com.inq.wishhair.wesharewishhair.point.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import com.inq.wishhair.wesharewishhair.common.support.RepositoryTestSupport;
import com.inq.wishhair.wesharewishhair.point.fixture.PointLogFixture;

@DisplayName("[PointLogRepository 테스트] - Domain")
class PointLogRepositoryTest extends RepositoryTestSupport {

	@Autowired
	private PointLogRepository pointLogRepository;

	@Test
	@DisplayName("[사용자 아이디로 가장 최근 PointLog 를 조회한다]")
	void findByUserIdOrderByNew() {
		//given
		PointLog pointLog1 = pointLogRepository.save(PointLogFixture.getChargePointLog());
		PointLog pointLog2 = pointLogRepository.save(PointLogFixture.getChargePointLog());

		//when
		Slice<PointLog> actual = pointLogRepository.findByUserIdOrderByNew(
			pointLog1.getUser().getId(),
			PageRequest.of(0, 2)
		);

		//then
		assertThat(actual.getContent()).contains(pointLog1, pointLog2);
	}
}