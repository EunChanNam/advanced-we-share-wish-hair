package com.inq.wishhair.wesharewishhair.hairstyle.domain.wishhair;

import com.inq.wishhair.wesharewishhair.global.auditing.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WishHair extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long hairStyleId;

	private Long userId;

	private WishHair(final Long hairStyleId, final Long userId) {
		this.hairStyleId = hairStyleId;
		this.userId = userId;
	}

	//==Factory method==//
	public static WishHair createWishHair(final Long userId, final  Long hairStyleId) {
		return new WishHair(hairStyleId, userId);
	}
}
