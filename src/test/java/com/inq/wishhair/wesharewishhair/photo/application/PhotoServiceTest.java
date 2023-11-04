package com.inq.wishhair.wesharewishhair.photo.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.inq.wishhair.wesharewishhair.common.utils.FileMockingUtils;
import com.inq.wishhair.wesharewishhair.photo.domain.Photo;
import com.inq.wishhair.wesharewishhair.photo.domain.PhotoRepository;
import com.inq.wishhair.wesharewishhair.photo.domain.PhotoStore;
import com.inq.wishhair.wesharewishhair.review.domain.entity.Review;
import com.inq.wishhair.wesharewishhair.review.fixture.ReviewFixture;

@DisplayName("[PhotoService 테스트] - Application")
class PhotoServiceTest {

	private final PhotoService photoService;
	private final PhotoStore photoStore;

	public PhotoServiceTest() {
		PhotoRepository photoRepository = Mockito.mock(PhotoRepository.class);
		this.photoStore = Mockito.mock(PhotoStore.class);
		this.photoService = new PhotoService(photoStore, photoRepository);
	}

	@Test
	void uploadPhotos() throws IOException {
		//given
		List<MultipartFile> files = FileMockingUtils.createMockMultipartFiles();

		List<String> urls = List.of("test_url1", "test_url2");
		given(photoStore.uploadFiles(anyList()))
			.willReturn(urls);

		//when
		List<String> actual = photoService.uploadPhotos(files);

		//then
		assertThat(actual).isEqualTo(urls);
	}

	@Test
	void deletePhotosByReviewId() {
		//given
		Review review = ReviewFixture.getEmptyReview(1L);
		Photo photo = Photo.createReviewPhoto("url1", review);

		ReflectionTestUtils.setField(review, "photos", List.of(photo));

		//when
		boolean actual = photoService.deletePhotosByReviewId(review);

		//then
		assertThat(actual).isTrue();
		verify(photoStore, times(1)).deleteFiles(anyList());
	}

	@Test
	void deletePhotosByWriter() {
		//given
		Review review1 = ReviewFixture.getEmptyReview(1L);
		Review review2 = ReviewFixture.getEmptyReview(2L);
		Photo photo1 = Photo.createReviewPhoto("url1", review1);
		Photo photo2 = Photo.createReviewPhoto("url1", review2);

		ReflectionTestUtils.setField(review1, "photos", List.of(photo1));
		ReflectionTestUtils.setField(review2, "photos", List.of(photo2));

		//when
		boolean actual = photoService.deletePhotosByWriter(List.of(review1, review2));

		//then
		assertThat(actual).isTrue();
		verify(photoStore, times(2)).deleteFiles(anyList());
	}
}