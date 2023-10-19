package com.inq.wishhair.wesharewishhair.hairstyle.domain.wishhair;

import java.time.LocalDateTime;

import com.inq.wishhair.wesharewishhair.global.auditing.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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

	@JoinColumn
	private Long hairStyleId;

	@JoinColumn
	private Long userId;

	//==생성 메서드==//
	private WishHair(Long hairStyleId, Long userId) {
		this.hairStyleId = hairStyleId;
		this.userId = userId;
		this.createdDate = LocalDateTime.now();
	}

	public static WishHair createWishHair(Long userId, Long hairStyleId) {
		return new WishHair(hairStyleId, userId);
	}
}
