package com.inq.wishhair.wesharewishhair.auth.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inq.wishhair.wesharewishhair.auth.domain.AuthCodeRepository;
import com.inq.wishhair.wesharewishhair.auth.domain.entity.AuthCode;

public interface AuthCodeJpaRepository extends AuthCodeRepository, JpaRepository<AuthCode, Long> {

	void deleteById(Long id);

	@Override
	void deleteByEmail(String email);

	@Override
	Optional<AuthCode> findByEmail(String email);
}
