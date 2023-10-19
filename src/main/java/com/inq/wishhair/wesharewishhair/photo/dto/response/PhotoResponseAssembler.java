package com.inq.wishhair.wesharewishhair.photo.dto.response;

import java.util.List;

import com.inq.wishhair.wesharewishhair.photo.domain.Photo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class PhotoResponseAssembler {

	public static List<PhotoResponse> toPhotoResponses(List<Photo> photos) {
		return photos.stream().map(PhotoResponse::new).toList();
	}
}
