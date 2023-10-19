package com.inq.wishhair.wesharewishhair.hairstyle.service.dto.response;

import static com.inq.wishhair.wesharewishhair.hairstyle.service.dto.response.HairStyleResponseAssembler.*;

import java.util.List;

import com.inq.wishhair.wesharewishhair.hairstyle.domain.HairStyle;
import com.inq.wishhair.wesharewishhair.photo.application.dto.response.PhotoInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HairStyleResponse {

	private Long hairStyleId;

	private String name;

	private List<PhotoInfo> photos;

	private List<HashTagResponse> hashTags;

	public HairStyleResponse(final HairStyle hairStyle) {
		this.hairStyleId = hairStyle.getId();
		this.name = hairStyle.getName();
		this.photos = hairStyle.getPhotos()
			.stream()
			.map(photo -> new PhotoInfo(photo.getStoreUrl()))
			.toList();
		this.hashTags = toHashTagResponses(hairStyle.getHashTags());
	}
}
