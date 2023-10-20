package com.inq.wishhair.wesharewishhair.review.application.dto.response;

import static com.inq.wishhair.wesharewishhair.hairstyle.service.dto.response.HairStyleResponseAssembler.*;
import static com.inq.wishhair.wesharewishhair.photo.application.dto.response.PhotoResponseAssembler.*;

import java.util.List;

import org.springframework.data.domain.Slice;

import com.inq.wishhair.wesharewishhair.global.dto.response.PagedResponse;
import com.inq.wishhair.wesharewishhair.global.dto.response.ResponseWrapper;
import com.inq.wishhair.wesharewishhair.review.application.query.dto.ReviewQueryResponse;
import com.inq.wishhair.wesharewishhair.review.domain.entity.Review;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReviewResponseAssembler {

	public static PagedResponse<ReviewResponse> toPagedReviewResponse(Slice<ReviewQueryResponse> slice) {
		return new PagedResponse<>(transferContentToResponse(slice));
	}

	private static Slice<ReviewResponse> transferContentToResponse(Slice<ReviewQueryResponse> slice) {
		return slice.map(ReviewResponseAssembler::toReviewResponse);
	}

	public static ReviewResponse toReviewResponse(ReviewQueryResponse queryResponse) {
		Review review = queryResponse.review();

		return ReviewResponse.builder()
			.reviewId(review.getId())
			.hairStyleName(review.getHairStyle().getName())
			.userNickname(review.getWriter().getNicknameValue())
			.score(review.getScore().getValue())
			.contents(review.getContentsValue())
			.createdDate(review.getCreatedDate())
			.photos(toPhotoResponses(review.getPhotos()))
			.likes(queryResponse.likes())
			.hashTags(toHashTagResponses(review.getHairStyle().getHashTags()))
			.writerId(review.getWriter().getId())
			.build();
	}

	public static ReviewDetailResponse toReviewDetailResponse(ReviewQueryResponse queryResponse, boolean isLiking) {
		return new ReviewDetailResponse(toReviewResponse(queryResponse), isLiking);
	}

	public static ResponseWrapper<ReviewSimpleResponse> toWrappedSimpleResponse(List<Review> reviews) {
		List<ReviewSimpleResponse> responses = reviews.stream().map(ReviewSimpleResponse::new).toList();
		return new ResponseWrapper<>(responses);
	}

	public static ResponseWrapper<ReviewResponse> toWrappedReviewResponse(List<ReviewQueryResponse> responses) {
		List<ReviewResponse> reviewResponses = responses.stream()
			.map(ReviewResponseAssembler::toReviewResponse)
			.toList();
		return new ResponseWrapper<>(reviewResponses);
	}
}
