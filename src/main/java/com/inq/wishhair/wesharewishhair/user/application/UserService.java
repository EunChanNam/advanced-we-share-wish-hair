package com.inq.wishhair.wesharewishhair.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.inq.wishhair.wesharewishhair.auth.domain.TokenRepository;
import com.inq.wishhair.wesharewishhair.global.dto.response.SimpleResponseWrapper;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.hashtag.Tag;
import com.inq.wishhair.wesharewishhair.point.domain.PointLogRepository;
import com.inq.wishhair.wesharewishhair.review.application.ReviewService;
import com.inq.wishhair.wesharewishhair.user.application.utils.UserValidator;
import com.inq.wishhair.wesharewishhair.user.domain.AiConnector;
import com.inq.wishhair.wesharewishhair.user.domain.UserRepository;
import com.inq.wishhair.wesharewishhair.user.domain.entity.Email;
import com.inq.wishhair.wesharewishhair.user.domain.entity.Nickname;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;
import com.inq.wishhair.wesharewishhair.user.presentation.dto.request.PasswordRefreshRequest;
import com.inq.wishhair.wesharewishhair.user.presentation.dto.request.PasswordUpdateRequest;
import com.inq.wishhair.wesharewishhair.user.presentation.dto.request.SignUpRequest;
import com.inq.wishhair.wesharewishhair.user.presentation.dto.request.UserUpdateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

	private final UserRepository userRepository;
	private final UserFindService userFindService;
	private final UserValidator userValidator;
	private final ReviewService reviewService;
	private final TokenRepository tokenRepository;
	private final AiConnector connector;
	private final PointLogRepository pointLogRepository;

	public Long createUser(SignUpRequest request) {

		User user = generateUser(request);
		userValidator.validateNicknameIsNotDuplicated(user.getNickname());

		User saveUser = userRepository.save(user);

		return saveUser.getId();
	}

	public void deleteUser(Long userId) {
		tokenRepository.deleteByUserId(userId);
		reviewService.deleteReviewByWriter(userId);
		pointLogRepository.deleteByUserId(userId);
		userRepository.deleteById(userId);
	}

	public void refreshPassword(PasswordRefreshRequest request) {
		User user = userFindService.getByEmail(new Email(request.email()));

		user.updatePassword(request.newPassword());
	}

	public void updateUser(Long userId, UserUpdateRequest request) {
		User user = userFindService.getById(userId);

		userValidator.validateNicknameIsNotDuplicated(new Nickname(request.nickname()));

		user.updateNickname(request.nickname());
		user.updateSex(request.sex());
	}

	public SimpleResponseWrapper<String> updateFaceShape(Long userId, MultipartFile file) {
		User user = userFindService.getById(userId);
		Tag faceShapeTag = connector.detectFaceShape(file);

		user.updateFaceShape(faceShapeTag);
		return new SimpleResponseWrapper<>(user.getFaceShapeTag().getDescription());
	}

	public void updatePassword(Long userId, PasswordUpdateRequest request) {
		User user = userFindService.getById(userId);
		user.confirmPassword(request.oldPassword());

		user.updatePassword(request.newPassword());
	}

	private User generateUser(SignUpRequest request) {
		return User.of(
			request.email(),
			request.pw(),
			request.name(),
			request.nickname(),
			request.sex());
	}
}
