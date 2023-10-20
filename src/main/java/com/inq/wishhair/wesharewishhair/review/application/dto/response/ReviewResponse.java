package com.inq.wishhair.wesharewishhair.review.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inq.wishhair.wesharewishhair.hairstyle.application.dto.response.HashTagResponse;
import com.inq.wishhair.wesharewishhair.photo.application.dto.response.PhotoInfo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewResponse {

	private Long reviewId;
	private String hairStyleName;
	private String userNickname;
	private String score;
	private String contents;
	private LocalDateTime createdDate;
	private List<PhotoInfo> photos;
	private long likes;
	private List<HashTagResponse> hashTags;
	private boolean isWriter;
	@JsonIgnore
	private Long writerId;
	public void addIsWriter(Long userId) {
		this.isWriter = writerId.equals(userId);
	}
}
