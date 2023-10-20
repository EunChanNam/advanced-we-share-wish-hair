package com.inq.wishhair.wesharewishhair.hairstyle.application.dto.response;

import com.inq.wishhair.wesharewishhair.hairstyle.domain.hashtag.HashTag;

public record HashTagResponse(
	String tag
) {
	public HashTagResponse(HashTag hashTag) {
		this(hashTag.getDescription());
	}
}
