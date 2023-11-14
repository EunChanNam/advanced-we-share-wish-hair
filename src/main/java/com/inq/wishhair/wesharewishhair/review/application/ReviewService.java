package com.inq.wishhair.wesharewishhair.review.application;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.HairStyle;
import com.inq.wishhair.wesharewishhair.hairstyle.application.HairStyleFindService;
import com.inq.wishhair.wesharewishhair.photo.application.PhotoService;
import com.inq.wishhair.wesharewishhair.review.application.dto.request.ReviewCreateRequest;
import com.inq.wishhair.wesharewishhair.review.application.dto.request.ReviewUpdateRequest;
import com.inq.wishhair.wesharewishhair.review.domain.entity.Contents;
import com.inq.wishhair.wesharewishhair.review.domain.entity.Review;
import com.inq.wishhair.wesharewishhair.review.domain.ReviewRepository;
import com.inq.wishhair.wesharewishhair.review.domain.likereview.LikeReviewRepository;
import com.inq.wishhair.wesharewishhair.review.event.PointChargeEvent;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;
import com.inq.wishhair.wesharewishhair.user.application.UserFindService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final LikeReviewRepository likeReviewRepository;
	private final ReviewFindService reviewFindService;
	private final PhotoService photoService;
	private final UserFindService userFindService;
	private final HairStyleFindService hairStyleFindService;
	private final ApplicationEventPublisher eventPublisher;

	private void validateIsWriter(Long userId, Review review) {
		if (!review.isWriter(userId)) {
			throw new WishHairException(ErrorCode.REVIEW_NOT_WRITER);
		}
	}

	private List<String> refreshPhotos(Review review, List<MultipartFile> files) {
		photoService.deletePhotosByReviewId(review);
		return photoService.uploadPhotos(files);
	}

	private Review generateReview(ReviewCreateRequest request, List<String> photos, User user, HairStyle hairStyle) {
		return Review.createReview(
			user,
			request.contents(),
			request.score(),
			photos,
			hairStyle
		);
	}

	@Transactional
	public Long createReview(ReviewCreateRequest request, Long userId) {

		List<String> photoUrls = photoService.uploadPhotos(request.files());
		User user = userFindService.findByUserId(userId);
		HairStyle hairStyle = hairStyleFindService.getById(request.hairStyleId());

		Review review = generateReview(request, photoUrls, user, hairStyle);
		eventPublisher.publishEvent(new PointChargeEvent(100, userId));

		return reviewRepository.save(review).getId();
	}

	@Transactional
	public boolean deleteReview(Long reviewId, Long userId) {
		Review review = reviewFindService.findWithPhotosById(reviewId);
		validateIsWriter(userId, review);

		likeReviewRepository.deleteByReviewId(reviewId);
		photoService.deletePhotosByReviewId(review);
		reviewRepository.delete(review);

		return true;
	}

	@Transactional
	public boolean updateReview(ReviewUpdateRequest request, Long userId) {
		Review review = reviewFindService.findWithPhotosById(request.reviewId());
		validateIsWriter(userId, review);

		Contents contents = new Contents(request.contents());
		List<String> storeUrls = refreshPhotos(review, request.files());
		review.updateReview(contents, request.score(), storeUrls);

		return true;
	}

	@Transactional
	public boolean deleteReviewByWriter(Long userId) {
		List<Review> reviews = reviewFindService.findWithPhotosByUserId(userId);
		List<Long> reviewIds = reviews.stream().map(Review::getId).toList();

		likeReviewRepository.deleteByReviewIdIn(reviewIds);
		photoService.deletePhotosByWriter(reviews);
		reviewRepository.deleteByIdIn(reviewIds);

		return true;
	}
}
