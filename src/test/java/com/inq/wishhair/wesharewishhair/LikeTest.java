package com.inq.wishhair.wesharewishhair;

import static org.assertj.core.api.Assertions.*;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.inq.wishhair.wesharewishhair.common.support.TestContainerSupport;
import com.inq.wishhair.wesharewishhair.review.application.LikeReviewService;

@SpringBootTest
@DisplayName("[좋아요 동시성 테스트]")
class LikeTest extends TestContainerSupport {

	@Autowired
	private LikeReviewService likeReviewService;

	@Test
	@DisplayName("[100개의 좋아요 동시 요청 모두 반영한다]")
	void success() throws InterruptedException {
		//given
		int threadCount = 100;
		ExecutorService service = Executors.newFixedThreadPool(threadCount);
		CountDownLatch latch = new CountDownLatch(threadCount);

		//데이터가 존재할 때는 동시성 해결
		likeReviewService.executeLike(1L, 1L);

		//when
		for (int i = 0; i < threadCount; i++) {
			service.execute(() -> {
				Random random = new Random();
				int id = random.nextInt(Integer.MAX_VALUE);

				likeReviewService.executeLike(1L, (long)id);
				latch.countDown();
			});
		}

		latch.await();

		//then
		Long likeCount = likeReviewService.getLikeCount(1L);
		assertThat(likeCount).isEqualTo(101);
	}
}
