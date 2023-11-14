package com.inq.wishhair.wesharewishhair.review.application.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.inq.wishhair.wesharewishhair.review.domain.entity.Score;

import jakarta.validation.constraints.NotNull;

public record ReviewUpdateRequest(
	@NotNull
	Long reviewId,
	@NotNull
	String contents,
	@NotNull
	Score score,
	List<MultipartFile> files
) {
}
