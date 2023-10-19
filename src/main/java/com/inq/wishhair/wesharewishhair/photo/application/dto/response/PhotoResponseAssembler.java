package com.inq.wishhair.wesharewishhair.photo.application.dto.response;

import java.util.List;

import com.inq.wishhair.wesharewishhair.photo.domain.Photo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PhotoResponseAssembler {

	public static List<PhotoInfo> toPhotoResponses(final List<Photo> photos) {
		return photos.stream().map(photo -> new PhotoInfo(photo.getStoreUrl())).toList();
	}
}
