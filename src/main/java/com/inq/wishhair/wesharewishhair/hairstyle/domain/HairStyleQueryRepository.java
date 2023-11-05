package com.inq.wishhair.wesharewishhair.hairstyle.domain;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.inq.wishhair.wesharewishhair.hairstyle.utils.HairRecommendCondition;

public interface HairStyleQueryRepository {

	List<HairStyle> findByRecommend(HairRecommendCondition condition, Pageable pageable);

	List<HairStyle> findByFaceShape(HairRecommendCondition condition, Pageable pageable);

	Slice<HairStyle> findByWish(Long userId, Pageable pageable);
}
