package com.inq.wishhair.wesharewishhair.hairstyle.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.inq.wishhair.wesharewishhair.hairstyle.domain.wishhair.WishHair;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.wishhair.WishHairRepository;

public interface WishHairJpaRepository extends WishHairRepository, JpaRepository<WishHair, Long> {

	@Modifying
	@Query("delete from WishHair w where w.hairStyleId = :hairStyleId and w.userId = :userId")
	void deleteByHairStyleIdAndUserId(@Param("hairStyleId") Long hairStyleId,
		@Param("userId") Long userId);

	boolean existsByHairStyleIdAndUserId(Long hairStyleId, Long userId);
}
