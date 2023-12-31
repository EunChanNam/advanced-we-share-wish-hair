package com.inq.wishhair.wesharewishhair.user.domain;

import java.util.Optional;

import com.inq.wishhair.wesharewishhair.user.domain.entity.Email;
import com.inq.wishhair.wesharewishhair.user.domain.entity.Nickname;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;

public interface UserRepository {

	User save(User user);

	Optional<User> findById(Long id);

	Optional<User> findByEmail(Email email);

	boolean existsByNickname(Nickname nickname);

	boolean existsByEmail(Email email);

	void deleteById(Long id);
}
