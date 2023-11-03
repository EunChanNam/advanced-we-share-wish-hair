package com.inq.wishhair.wesharewishhair.point.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

import com.inq.wishhair.wesharewishhair.global.dto.response.PagedResponse;
import com.inq.wishhair.wesharewishhair.global.dto.response.Paging;
import com.inq.wishhair.wesharewishhair.point.application.dto.PointResponse;
import com.inq.wishhair.wesharewishhair.point.domain.PointLog;
import com.inq.wishhair.wesharewishhair.point.domain.PointLogRepository;
import com.inq.wishhair.wesharewishhair.point.fixture.PointLogFixture;

@DisplayName("[PointSearchService 테스트] - Application")
class PointSearchServiceTest {

	private final PointSearchService pointSearchService;
	private final PointLogRepository pointLogRepository;

	public PointSearchServiceTest() {
		this.pointLogRepository = Mockito.mock(PointLogRepository.class);
		this.pointSearchService = new PointSearchService(pointLogRepository);
	}

	private void assertPointResponse(PointResponse actual, PointLog expected) {
		assertAll(
			() -> assertThat(actual.point()).isEqualTo(expected.getPoint()),
			() -> assertThat(actual.pointType()).isEqualTo(expected.getPointType().getDescription()),
			() -> assertThat(actual.dealAmount()).isEqualTo(expected.getDealAmount())
		);
	}

	@Test
	@DisplayName("[사용자의 PointLog 를 조회한다]")
	void getPointHistories() {
		//given
		Pageable pageable = PageRequest.of(0, 2);
		List<PointLog> pointLogs = List.of(PointLogFixture.getChargePointLog(), PointLogFixture.getUsePointLog());
		SliceImpl<PointLog> slicePointLogs = new SliceImpl<>(
			pointLogs,
			pageable,
			false
		);

		given(pointLogRepository.findByUserIdOrderByNew(1L, pageable))
			.willReturn(slicePointLogs);

		//when
		PagedResponse<PointResponse> actual = pointSearchService.getPointHistories(1L, pageable);

		//then
		Paging paging = actual.getPaging();
		assertAll(
			() -> assertThat(paging.hasNext()).isFalse(),
			() -> assertThat(paging.getPage()).isZero(),
			() -> assertThat(paging.getContentSize()).isEqualTo(slicePointLogs.getContent().size())
		);

		List<PointResponse> responses = actual.getResult();
		assertAll(
			() -> assertThat(responses).hasSameSizeAs(slicePointLogs.getContent()),
			() -> assertPointResponse(responses.get(0), pointLogs.get(0)),
			() -> assertPointResponse(responses.get(1), pointLogs.get(1))
		);
	}
}