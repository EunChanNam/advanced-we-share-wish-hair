package com.inq.wishhair.wesharewishhair.user.application.utils;

import org.springframework.stereotype.Service;

import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;
import com.inq.wishhair.wesharewishhair.user.domain.entity.Email;
import com.inq.wishhair.wesharewishhair.user.domain.entity.Nickname;
import com.inq.wishhair.wesharewishhair.user.domain.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserValidator {

	private final UserRepository userRepository;

	public void validateNicknameIsNotDuplicated(Nickname nickname) {
		if (userRepository.existsByNickname(nickname)) {
			throw new WishHairException(ErrorCode.USER_DUPLICATED_NICKNAME);
		}
	}

	public void validateEmailIsNotDuplicated(Email email) {
		if (userRepository.existsByEmail(email)) {
			throw new WishHairException(ErrorCode.USER_DUPLICATED_EMAIL);
		}
	}
}
