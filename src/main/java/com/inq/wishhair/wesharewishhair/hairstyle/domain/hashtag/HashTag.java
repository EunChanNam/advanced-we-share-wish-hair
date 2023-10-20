package com.inq.wishhair.wesharewishhair.hairstyle.domain.hashtag;

import com.inq.wishhair.wesharewishhair.hairstyle.domain.HairStyle;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class HashTag {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, updatable = false)
	private Tag tag;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hair_style_id")
	private HairStyle hairStyle;

	private HashTag(final HairStyle hairStyle, final Tag tag) {
		this.hairStyle = hairStyle;
		this.tag = tag;
	}

	//==Factory method==//
	public static HashTag of(final HairStyle hairStyle, final Tag tag) {
		return new HashTag(hairStyle, tag);
	}

	//==Utility method==//
	public String getDescription() {
		return tag.getDescription();
	}
}
