package com.inq.wishhair.wesharewishhair.photo.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.inq.wishhair.wesharewishhair.photo.domain.Photo;
import com.inq.wishhair.wesharewishhair.photo.domain.PhotoRepository;
import com.inq.wishhair.wesharewishhair.photo.domain.PhotoStore;
import com.inq.wishhair.wesharewishhair.review.domain.entity.Review;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PhotoService {

	private final PhotoStore photoStore;
	private final PhotoRepository photoRepository;

	public List<String> uploadPhotos(final List<MultipartFile> files) {
		return photoStore.uploadFiles(files);
	}

	@Transactional
	public void deletePhotosByReviewId(final Review review) {
		deletePhotosInCloud(review);
		photoRepository.deleteAllByReview(review.getId());
	}

	@Transactional
	public void deletePhotosByWriter(final List<Review> reviews) {
		reviews.forEach(this::deletePhotosInCloud);
		photoRepository.deleteAllByReviews(reviews.stream().map(Review::getId).toList());
	}

	private void deletePhotosInCloud(final Review review) {
		List<String> storeUrls = review.getPhotos().stream().map(Photo::getStoreUrl).toList();
		photoStore.deleteFiles(storeUrls);
	}
}
