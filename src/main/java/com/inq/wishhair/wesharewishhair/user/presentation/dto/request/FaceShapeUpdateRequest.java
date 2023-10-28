package com.inq.wishhair.wesharewishhair.user.presentation.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record FaceShapeUpdateRequest(
	MultipartFile file
) {
}
