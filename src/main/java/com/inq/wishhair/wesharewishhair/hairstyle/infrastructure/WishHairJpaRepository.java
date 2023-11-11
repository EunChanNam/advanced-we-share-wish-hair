package com.inq.wishhair.wesharewishhair.hairstyle.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inq.wishhair.wesharewishhair.hairstyle.domain.wishhair.WishHair;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.wishhair.WishHairRepository;

public interface WishHairJpaRepository extends WishHairRepository, JpaRepository<WishHair, Long> {

	int deleteByHairStyleIdAndUserId(Long hairStyleId, Long userId);

	boolean existsByHairStyleIdAndUserId(Long hairStyleId, Long userId);
}
