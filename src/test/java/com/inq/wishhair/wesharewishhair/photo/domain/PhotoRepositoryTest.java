package com.inq.wishhair.wesharewishhair.photo.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import com.inq.wishhair.wesharewishhair.common.support.RepositoryTestSupport;
import com.inq.wishhair.wesharewishhair.review.domain.entity.Review;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@DisplayName("[PhotoRepository 테스트] - Domain")
class PhotoRepositoryTest extends RepositoryTestSupport {

	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private PhotoRepository photoRepository;

	@Test
	@DisplayName("[리뷰 아이디를 가진 Photo 를 삭제한다]")
	void deleteAllByReview() {
		//given
		Review review = new Review();
		ReflectionTestUtils.setField(review, "id", 1L);
		Photo photo = photoRepository.save(Photo.createReviewPhoto("url", review));

		//when
		photoRepository.deleteAllByReview(1L);
		entityManager.clear();

		//then
		Optional<Photo> actual = photoRepository.findById(photo.getId());
		assertThat(actual).isNotPresent();
	}

	@Test
	@DisplayName("[리뷰 아이디 리스트에 포함된 Photo 를 삭제한다]")
	void deleteAllByReviews() {
		//given
		Review review1 = new Review();
		ReflectionTestUtils.setField(review1, "id", 1L);
		Review review2 = new Review();
		ReflectionTestUtils.setField(review2, "id", 2L);
		photoRepository.save(Photo.createReviewPhoto("url1", review1));
		photoRepository.save(Photo.createReviewPhoto("url2", review2));

		//when
		photoRepository.deleteAllByReviews(List.of(1L, 2L));
		entityManager.clear();

		//then
		List<Photo> actual = photoRepository.findAll();
		assertThat(actual).isEmpty();
	}
}