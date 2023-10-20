package com.inq.wishhair.wesharewishhair.hairstyle.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;
import com.inq.wishhair.wesharewishhair.hairstyle.application.dto.response.WishHairResponse;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.wishhair.WishHair;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.wishhair.WishHairRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishHairService {

	private final WishHairRepository wishHairRepository;

	@Transactional
	public void executeWish(
		final Long hairStyleId,
		final Long userId
	) {
		validateDoesNotExistWishHair(hairStyleId, userId);

		wishHairRepository.save(WishHair.createWishHair(userId, hairStyleId));
	}

	@Transactional
	public void cancelWish(
		final Long hairStyleId,
		final Long userId
	) {
		validateDoesWishHairExist(hairStyleId, userId);

		wishHairRepository.deleteByHairStyleIdAndUserId(hairStyleId, userId);
	}

	public WishHairResponse checkIsWishing(
		final Long hairStyleId,
		final Long userId
	) {
		return new WishHairResponse(existWishHair(hairStyleId, userId));
	}

	private boolean existWishHair(
		final Long hairStyleId,
		final Long userId
	) {
		return wishHairRepository.existsByHairStyleIdAndUserId(hairStyleId, userId);
	}

	private void validateDoesWishHairExist(
		final Long hairStyleId,
		final Long userId
	) {
		if (!existWishHair(hairStyleId, userId)) {
			throw new WishHairException(ErrorCode.WISH_HAIR_NOT_EXIST);
		}
	}

	private void validateDoesNotExistWishHair(
		final Long hairStyleId,
		final Long userId
	) {
		if (existWishHair(hairStyleId, userId)) {
			throw new WishHairException(ErrorCode.WISH_HAIR_ALREADY_EXIST);
		}
	}
}
