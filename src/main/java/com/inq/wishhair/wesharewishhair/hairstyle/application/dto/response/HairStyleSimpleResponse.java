package com.inq.wishhair.wesharewishhair.hairstyle.application.dto.response;

import com.inq.wishhair.wesharewishhair.hairstyle.domain.HairStyle;

public record HairStyleSimpleResponse(
	Long hairStyleId,
	String hairStyleName
) {
	public HairStyleSimpleResponse(final HairStyle hairStyle) {
		this(hairStyle.getId(), hairStyle.getName());
	}
}
