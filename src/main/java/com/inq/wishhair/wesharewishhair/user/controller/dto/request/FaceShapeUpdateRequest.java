package com.inq.wishhair.wesharewishhair.user.controller.dto.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FaceShapeUpdateRequest {

	private MultipartFile file;
}
