package com.inq.wishhair.wesharewishhair.user.domain;

import org.springframework.web.multipart.MultipartFile;

import com.inq.wishhair.wesharewishhair.hairstyle.domain.hashtag.Tag;

@FunctionalInterface
public interface AiConnector {
	Tag detectFaceShape(MultipartFile file);
}
