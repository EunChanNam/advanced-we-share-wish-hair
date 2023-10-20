package com.inq.wishhair.wesharewishhair.point.domain;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.inq.wishhair.wesharewishhair.user.domain.entity.User;

public interface PointLogRepository {

	PointLog save(PointLog pointLog);

	Optional<PointLog> findByUserOrderByCreatedDateDesc(User user);

	void deleteByUserId(Long userId);

	Slice<PointLog> findByUserIdOrderByNew(Long userId, Pageable pageable);
}
