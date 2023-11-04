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

import com.inq.wishhair.wesharewishhair.review.application.LikeReviewService;

// @SpringBootTest
@DisplayName("[좋아요 동시성 테스트]")
class LikeTest {

	// @Autowired
	private LikeReviewService likeReviewService;

	// @Test
	@DisplayName("[100개의 동시요청에서 100개의 좋아요 개수를 기록한다]")
	void test() throws InterruptedException {
		//given
		int threadCount = 1000;
		ExecutorService service = Executors.newFixedThreadPool(50);
		CountDownLatch latch = new CountDownLatch(threadCount);
		for (int i = 0; i < threadCount; i++) {
			service.execute(() -> {
				Random random = new Random();
				int id = random.nextInt(Integer.MAX_VALUE);

				likeReviewService.executeLike(201L, (long)id);
				latch.countDown();
			});
		}

		latch.await();
		Long likeCount = likeReviewService.getLikeCount(1L);
		assertThat(likeCount).isEqualTo(102);
	}
}
