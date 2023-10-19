package com.inq.wishhair.wesharewishhair.user.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(Email email);

	boolean existsByNickname(Nickname nickname);

	boolean existsByEmail(Email email);
}
