package com.inq.wishhair.wesharewishhair.hairstyle.domain;

import java.util.List;
import java.util.Optional;

public interface HairStyleRepository {

	HairStyle save(HairStyle hairStyle);

	Optional<HairStyle> findById(Long id);

	List<HairStyle> findAllByOrderByName();
}
