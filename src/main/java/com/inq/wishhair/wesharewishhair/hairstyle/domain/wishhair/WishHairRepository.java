package com.inq.wishhair.wesharewishhair.hairstyle.domain.wishhair;

public interface WishHairRepository {

	WishHair save(WishHair wishHair);

	int deleteByHairStyleIdAndUserId(Long hairStyleId, Long userId);

	boolean existsByHairStyleIdAndUserId(Long hairStyleId, Long userId);
}
