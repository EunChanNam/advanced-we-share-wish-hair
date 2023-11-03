package com.inq.wishhair.wesharewishhair.review.application.dto.response;

import static com.inq.wishhair.wesharewishhair.hairstyle.application.dto.response.HairStyleResponseAssembler.*;
import static com.inq.wishhair.wesharewishhair.photo.application.dto.response.PhotoResponseAssembler.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import com.inq.wishhair.wesharewishhair.global.dto.response.PagedResponse;
import com.inq.wishhair.wesharewishhair.global.dto.response.ResponseWrapper;
import com.inq.wishhair.wesharewishhair.review.domain.entity.Review;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReviewResponseAssembler {

	public static PagedResponse<ReviewResponse> toPagedReviewResponse(Slice<Review> slice, List<Long> likeCounts) {
		return new PagedResponse<>(transferContentToResponse(slice, likeCounts));
	}

	private static Slice<ReviewResponse> transferContentToResponse(Slice<Review> slice, List<Long> likeCounts) {
		List<Review> reviews = slice.getContent();

		List<ReviewResponse> reviewResponses = new ArrayList<>();
		for (int i = 0; i < reviews.size(); i++) {
			Review review = reviews.get(i);
			Long likeCount = likeCounts.get(i);

			reviewResponses.add(toReviewResponse(review, likeCount));
		}

		return new SliceImpl<>(reviewResponses, slice.getPageable(), slice.hasNext());
	}

	public static ReviewResponse toReviewResponse(Review review, Long likeCount) {

		return ReviewResponse.builder()
			.reviewId(review.getId())
			.hairStyleName(review.getHairStyle().getName())
			.userNickname(review.getWriter().getNicknameValue())
			.score(review.getScore().getValue())
			.contents(review.getContentsValue())
			.createdDate(review.getCreatedDate())
			.photos(toPhotoResponses(review.getPhotos()))
			.likes(likeCount)
			.hashTags(toHashTagResponses(review.getHairStyle().getHashTags()))
			.writerId(review.getWriter().getId())
			.build();
	}

	public static ReviewDetailResponse toReviewDetailResponse(
		Review review,
		Long likeCount,
		boolean isLiking
	) {
		return new ReviewDetailResponse(toReviewResponse(review, likeCount), isLiking);
	}

	public static ResponseWrapper<ReviewSimpleResponse> toWrappedSimpleResponse(List<Review> reviews) {
		List<ReviewSimpleResponse> responses = reviews.stream().map(ReviewSimpleResponse::new).toList();
		return new ResponseWrapper<>(responses);
	}

	public static ResponseWrapper<ReviewResponse> toWrappedReviewResponse(
		List<Review> responses,
		List<Long> likeCounts
	) {
		List<ReviewResponse> reviewResponses = new ArrayList<>();

		for (int i = 0; i < responses.size(); i++) {
			ReviewResponse reviewResponse = toReviewResponse(responses.get(i), likeCounts.get(i));
			reviewResponses.add(reviewResponse);
		}

		return new ResponseWrapper<>(reviewResponses);
	}
}
