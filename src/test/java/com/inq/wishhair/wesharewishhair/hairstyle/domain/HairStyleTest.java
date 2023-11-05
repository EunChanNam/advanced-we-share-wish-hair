package com.inq.wishhair.wesharewishhair.hairstyle.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.inq.wishhair.wesharewishhair.hairstyle.domain.hashtag.HashTag;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.hashtag.Tag;
import com.inq.wishhair.wesharewishhair.photo.domain.Photo;
import com.inq.wishhair.wesharewishhair.user.domain.entity.Sex;

@DisplayName("[HairStyle 테스트] - Domain")
class HairStyleTest {

	@Test
	@DisplayName("[HairStyle 을 생성한다]")
	void createHairStyle() {
		//given
		String name = "name";
		Sex sex = Sex.MAN;
		List<String> photoUrls = List.of("url1", "url2");
		List<Tag> tags = List.of(Tag.CUTE, Tag.BANGS);

		//when
		HairStyle actual = HairStyle.createHairStyle(
			name,
			sex,
			photoUrls,
			tags
		);

		//then
		assertAll(
			() -> assertThat(actual.getName()).isEqualTo(name),
			() -> assertThat(actual.getSex()).isEqualTo(sex),
			() -> {
				List<HashTag> hashTags = actual.getHashTags();
				assertThat(hashTags).hasSize(2);
				List<Tag> actualTags = hashTags.stream().map(HashTag::getTag).toList();
				assertThat(actualTags).containsAll(tags);
			},
			() -> {
				List<Photo> photos = actual.getPhotos();
				List<String> actualPhotoUrls = photos.stream().map(Photo::getStoreUrl).toList();
				assertThat(actualPhotoUrls).containsAll(photoUrls);
			}
		);
	}
}