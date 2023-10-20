package com.inq.wishhair.wesharewishhair.hairstyle.infrastructure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inq.wishhair.wesharewishhair.hairstyle.domain.HairStyle;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.HairStyleRepository;

public interface HairStyleJpaRepository extends HairStyleRepository, JpaRepository<HairStyle, Long> {

	List<HairStyle> findAllByOrderByName();
}
