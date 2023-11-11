package com.inq.wishhair.wesharewishhair.hairstyle.fixture;

import java.util.List;

import org.springframework.test.util.ReflectionTestUtils;

import com.inq.wishhair.wesharewishhair.hairstyle.domain.HairStyle;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.hashtag.Tag;
import com.inq.wishhair.wesharewishhair.user.domain.entity.Sex;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HairStyleFixture {

	private static final String NAME = "hair_style_name";
	private static final List<Tag> TAGS = List.of(Tag.PERM, Tag.H_LONG, Tag.SQUARE, Tag.UPSTAGE);
	private static final List<String> IMAGE_URLS = List.of("hello1.jpg", "hello2.jpg");

	public static HairStyle getWomanHairStyle() {
		return HairStyle.createHairStyle(
			NAME,
			Sex.WOMAN,
			IMAGE_URLS,
			TAGS
		);
	}

	public static HairStyle getWomanHairStyle(Long id) {
		HairStyle hairStyle = getWomanHairStyle();
		ReflectionTestUtils.setField(hairStyle, "id", id);
		return hairStyle;
	}


	public static HairStyle getWomanHairStyle(String name, List<Tag> tags) {
		return HairStyle.createHairStyle(
			name,
			Sex.WOMAN,
			IMAGE_URLS,
			tags
		);
	}
}
