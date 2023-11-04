package com.inq.wishhair.wesharewishhair.photo.domain;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository {

	Photo save(Photo photo);

	Optional<Photo> findById(Long id);

	List<Photo> findAll();

	void deleteAllByReview(Long reviewId);

	void deleteAllByReviews(List<Long> reviewIds);
}
