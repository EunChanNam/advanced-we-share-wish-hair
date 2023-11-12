package com.inq.wishhair.wesharewishhair.common.support;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.inq.wishhair.wesharewishhair.global.config.QueryDslConfig;
import com.inq.wishhair.wesharewishhair.hairstyle.infrastructure.query.HairStyleQueryDslRepository;
import com.inq.wishhair.wesharewishhair.review.infrastructure.ReviewQueryDslRepository;

@Import({QueryDslConfig.class, HairStyleQueryDslRepository.class, ReviewQueryDslRepository.class})
@DataJpaTest
public abstract class RepositoryTestSupport {
}
