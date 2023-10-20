package com.inq.wishhair.wesharewishhair.review.presentation.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.inq.wishhair.wesharewishhair.review.domain.entity.Score;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewCreateRequest {

	@NotNull
	private String contents;

	@NotNull
	private Score score;

	private List<MultipartFile> files;

	@NotNull
	private Long hairStyleId;
}
