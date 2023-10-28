package com.inq.wishhair.wesharewishhair.user.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inq.wishhair.wesharewishhair.user.domain.entity.Email;
import com.inq.wishhair.wesharewishhair.user.domain.entity.Nickname;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;
import com.inq.wishhair.wesharewishhair.user.domain.UserRepository;

public interface UserJpaRepository extends UserRepository, JpaRepository<User, Long> {

	Optional<User> findByEmail(Email email);

	boolean existsByNickname(Nickname nickname);

	boolean existsByEmail(Email email);

	void deleteById(Long id);
}
