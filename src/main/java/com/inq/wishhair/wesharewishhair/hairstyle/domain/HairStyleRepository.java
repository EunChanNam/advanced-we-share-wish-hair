package com.inq.wishhair.wesharewishhair.hairstyle.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inq.wishhair.wesharewishhair.hairstyle.infra.query.HairStyleQueryRepository;


public interface HairStyleRepository extends JpaRepository<HairStyle, Long>, HairStyleQueryRepository {

    List<HairStyle> findAllByOrderByName();
}
