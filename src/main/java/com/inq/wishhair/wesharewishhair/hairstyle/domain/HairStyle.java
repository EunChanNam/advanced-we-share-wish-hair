package com.inq.wishhair.wesharewishhair.hairstyle.domain;

import java.util.ArrayList;
import java.util.List;

import com.inq.wishhair.wesharewishhair.hairstyle.domain.hashtag.HashTag;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.hashtag.Tag;
import com.inq.wishhair.wesharewishhair.photo.domain.Photo;
import com.inq.wishhair.wesharewishhair.user.domain.entity.Sex;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HairStyle {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, updatable = false)
	private String name;

	@OneToMany(mappedBy = "hairStyle", cascade = CascadeType.PERSIST)
	private final List<Photo> photos = new ArrayList<>();

	@OneToMany(mappedBy = "hairStyle", cascade = CascadeType.PERSIST)
	private final List<HashTag> hashTags = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	private Sex sex;

	//==생성 메서드==//
	private HairStyle(
		final String name,
		final Sex sex,
		final List<String> photos,
		final List<Tag> tags
	) {
		this.name = name;
		this.sex = sex;
		applyPhotos(photos);
		applyHasTags(tags);
	}

	public static HairStyle createHairStyle(
		final String name,
		final Sex sex,
		final List<String> photos,
		final List<Tag> tags
	) {
		return new HairStyle(name, sex, photos, tags);
	}

	//==편의 메서드--//
	private void applyHasTags(final List<Tag> tags) {
		tags.forEach(tag -> hashTags.add(HashTag.of(this, tag)));
	}

	private void applyPhotos(final List<String> storeUrls) {
		storeUrls.forEach(storeUrl -> photos.add(Photo.createHairStylePhoto(storeUrl, this)));
	}
}
