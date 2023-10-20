package com.inq.wishhair.wesharewishhair.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;
import com.inq.wishhair.wesharewishhair.user.domain.entity.Email;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;
import com.inq.wishhair.wesharewishhair.user.domain.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserFindService {

	private final UserRepository userRepository;

	public User findByUserId(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new WishHairException(ErrorCode.NOT_EXIST_KEY));
	}

	public User findByEmail(Email email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new WishHairException(ErrorCode.USER_NOT_FOUND_BY_EMAIL));
	}
}
