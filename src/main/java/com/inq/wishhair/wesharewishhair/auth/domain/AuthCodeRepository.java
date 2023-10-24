package com.inq.wishhair.wesharewishhair.auth.domain;

import java.util.Optional;

import com.inq.wishhair.wesharewishhair.auth.domain.entity.AuthCode;

public interface AuthCodeRepository {

	AuthCode save(AuthCode authCode);

	Optional<AuthCode> findByEmail(String email);
}
