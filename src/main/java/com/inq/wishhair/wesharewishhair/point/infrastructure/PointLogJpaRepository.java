package com.inq.wishhair.wesharewishhair.point.infrastructure;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.inq.wishhair.wesharewishhair.point.domain.PointLog;
import com.inq.wishhair.wesharewishhair.point.domain.PointLogRepository;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;

public interface PointLogJpaRepository extends PointLogRepository, JpaRepository<PointLog, Long> {

	Optional<PointLog> findByUserOrderByCreatedDateDesc(User user);

	@Modifying
	@Query("delete from PointLog p where p.user.id = :userId")
	void deleteByUserId(@Param("userId") Long userId);

	@Query("select p from PointLog p " +
		"where p.user.id = :userId " +
		"order by p.id desc")
	Slice<PointLog> findByUserIdOrderByNew(
		@Param("userId") Long userId,
		Pageable pageable
	);
}
