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
import org.springframework.context.annotation.Import;

import com.inq.wishhair.wesharewishhair.common.config.EmbeddedRedisConfig;
import com.inq.wishhair.wesharewishhair.review.application.LikeReviewService;

@SpringBootTest
@Import(EmbeddedRedisConfig.class)
@DisplayName("[좋아요 동시성 테스트]")
class LikeTest {

	@Autowired
	private LikeReviewService likeReviewService;

	// @Test
	// @DisplayName("[좋아요 정보가 Redis 에 없을 때, 동시적 요청이 들어오면 동시성 이슈가 발생한다]")
	// void fail() throws InterruptedException {
	// 	//given
	// 	int threadCount = 100;
	// 	ExecutorService service = Executors.newFixedThreadPool(threadCount);
	// 	CountDownLatch latch = new CountDownLatch(threadCount);
	//
	// 	//when
	// 	for (int i = 0; i < threadCount; i++) {
	// 		service.execute(() -> {
	// 			Random random = new Random();
	// 			int id = random.nextInt(Integer.MAX_VALUE);
	//
	// 			likeReviewService.executeLike(1L, (long)id);
	// 			latch.countDown();
	// 		});
	// 	}
	//
	// 	latch.await();
	//
	// 	//then -> 동시성 이슈 발생으로 100개의 좋아요 보다 적은 likeCount
	// 	Long likeCount = likeReviewService.getLikeCount(1L);
	// 	assertThat(likeCount).isLessThan(100L);
	// }

	@Test
	@DisplayName("[좋아요 정보가 Redis 에 있을 때에는 동시성 이슈가 발생하지 않는다.]")
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
