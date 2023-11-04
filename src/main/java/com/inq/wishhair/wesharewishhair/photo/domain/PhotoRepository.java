package com.inq.wishhair.wesharewishhair.photo.domain;

import java.util.List;

public interface PhotoRepository {

	void deleteAllByReview(Long reviewId);

	void deleteAllByReviews(List<Long> reviewIds);
}
