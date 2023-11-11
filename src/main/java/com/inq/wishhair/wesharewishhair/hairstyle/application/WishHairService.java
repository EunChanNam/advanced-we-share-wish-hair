package com.inq.wishhair.wesharewishhair.hairstyle.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inq.wishhair.wesharewishhair.hairstyle.application.dto.response.WishHairResponse;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.wishhair.WishHair;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.wishhair.WishHairRepository;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishHairService {

	private final WishHairRepository wishHairRepository;

	private boolean existWishHair(
		final Long hairStyleId,
		final Long userId
	) {
		return wishHairRepository.existsByHairStyleIdAndUserId(hairStyleId, userId);
	}

	@Transactional
	public boolean executeWish(
		final Long hairStyleId,
		final Long userId
	) {
		try {
			wishHairRepository.save(WishHair.createWishHair(userId, hairStyleId));
		} catch (EntityExistsException e) {
			return false;
		}
		return true;
	}

	@Transactional
	public boolean cancelWish(
		final Long hairStyleId,
		final Long userId
	) {
		int deletedCount = wishHairRepository.deleteByHairStyleIdAndUserId(hairStyleId, userId);
		return deletedCount != 0;
	}

	public WishHairResponse checkIsWishing(
		final Long hairStyleId,
		final Long userId
	) {
		return new WishHairResponse(existWishHair(hairStyleId, userId));
	}
}
