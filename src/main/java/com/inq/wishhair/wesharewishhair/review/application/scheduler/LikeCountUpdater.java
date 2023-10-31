package com.inq.wishhair.wesharewishhair.review.application.scheduler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.inq.wishhair.wesharewishhair.review.domain.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LikeCountUpdater {

	private static final Comparator<Long> ASC = Long::compare;

	private final ReviewRepository reviewRepository;
	private final Set<Long> updateReviewSet;

	@Scheduled(fixedRate = 2 * 60 * 60 * 1000) // 2시간마다 실행
	public void syncLikeCount() {
		List<Integer> orderedLikeCounts = reviewRepository.countLikeReviewByIdsOrderById(updateReviewSet);
		List<Long> orderedReviewIds = getOrderedReviewIds();

		int bound = orderedReviewIds.size();
		for (int i = 0; i < bound; i++) {
			int likeCount = orderedLikeCounts.get(i);
			Long reviewId = orderedReviewIds.get(i);

			reviewRepository.updateLikeCountById(reviewId, likeCount);
		}

		updateReviewSet.clear();
	}

	private List<Long> getOrderedReviewIds() {
		List<Long> reviewIds = new ArrayList<>(updateReviewSet);
		reviewIds.sort(ASC);

		return reviewIds;
	}
}
