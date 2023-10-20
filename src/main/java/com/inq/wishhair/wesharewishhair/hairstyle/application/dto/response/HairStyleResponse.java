package com.inq.wishhair.wesharewishhair.hairstyle.application.dto.response;

import java.util.List;

import com.inq.wishhair.wesharewishhair.hairstyle.domain.HairStyle;
import com.inq.wishhair.wesharewishhair.photo.application.dto.response.PhotoInfo;
import com.inq.wishhair.wesharewishhair.photo.application.dto.response.PhotoResponseAssembler;

public record HairStyleResponse(
	Long hairStyleId,
	String name,
	List<PhotoInfo> photos,
	List<HashTagResponse> hashTags
) {
	public HairStyleResponse(final HairStyle hairStyle) {
		this(
			hairStyle.getId(),
			hairStyle.getName(),
			PhotoResponseAssembler.toPhotoResponses(hairStyle.getPhotos()),
			HairStyleResponseAssembler.toHashTagResponses(hairStyle.getHashTags())
		);
	}
}
