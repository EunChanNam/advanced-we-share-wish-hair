package com.inq.wishhair.wesharewishhair.photo.domain;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface PhotoStore {

	List<String> uploadFiles(List<MultipartFile> files);

	boolean deleteFiles(List<String> storeUrls);
}
