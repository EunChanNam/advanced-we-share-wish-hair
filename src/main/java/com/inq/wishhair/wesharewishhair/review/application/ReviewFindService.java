package com.inq.wishhair.wesharewishhair.review.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;
import com.inq.wishhair.wesharewishhair.review.domain.entity.Review;
import com.inq.wishhair.wesharewishhair.review.domain.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewFindService {

	private final ReviewRepository reviewRepository;

	public Review findWithPhotosById(Long id) {
		return reviewRepository.findWithPhotosById(id)
			.orElseThrow(() -> new WishHairException(ErrorCode.NOT_EXIST_KEY));
	}

	public List<Review> findWithPhotosByUserId(Long userId) {
		return reviewRepository.findWithPhotosByWriterId(userId);
	}

	public Review getWithLockById(Long id) {
		return reviewRepository.findWithLockById(id).orElseThrow();
	}
}
