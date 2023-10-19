package com.inq.wishhair.wesharewishhair.review.controller.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.inq.wishhair.wesharewishhair.review.domain.Score;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReviewUpdateRequest {

	@NotNull
	private Long reviewId;

	@NotNull
	private String contents;

	@NotNull
	private Score score;

	private List<MultipartFile> files;
}
