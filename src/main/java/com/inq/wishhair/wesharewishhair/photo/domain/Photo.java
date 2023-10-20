package com.inq.wishhair.wesharewishhair.photo.domain;

import com.inq.wishhair.wesharewishhair.hairstyle.domain.HairStyle;
import com.inq.wishhair.wesharewishhair.review.domain.entity.Review;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Photo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hair_style_id")
	private HairStyle hairStyle;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "review_id")
	private Review review;

	@Column(nullable = false, updatable = false, unique = true)
	private String storeUrl;

	//==생성 메서드==//
	private Photo(String storeUrl) {
		this.storeUrl = storeUrl;
	}

	public static Photo createReviewPhoto(final String storeUrl, final Review review) {
		Photo photo = new Photo(storeUrl);
		photo.review = review;
		return photo;
	}

	public static Photo createHairStylePhoto(final String storeUrl, final HairStyle hairStyle) {
		Photo photo = new Photo(storeUrl);
		photo.hairStyle = hairStyle;
		return photo;
	}
}
