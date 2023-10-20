package com.inq.wishhair.wesharewishhair.user.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.inq.wishhair.wesharewishhair.auth.domain.TokenRepository;
import com.inq.wishhair.wesharewishhair.global.dto.response.SimpleResponseWrapper;
import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.hashtag.Tag;
import com.inq.wishhair.wesharewishhair.review.application.ReviewService;
import com.inq.wishhair.wesharewishhair.user.presentation.dto.request.PasswordRefreshRequest;
import com.inq.wishhair.wesharewishhair.user.presentation.dto.request.PasswordUpdateRequest;
import com.inq.wishhair.wesharewishhair.user.presentation.dto.request.SignUpRequest;
import com.inq.wishhair.wesharewishhair.user.presentation.dto.request.UserUpdateRequest;
import com.inq.wishhair.wesharewishhair.user.domain.entity.Email;
import com.inq.wishhair.wesharewishhair.user.domain.entity.FaceShape;
import com.inq.wishhair.wesharewishhair.user.domain.entity.Nickname;
import com.inq.wishhair.wesharewishhair.user.domain.entity.Password;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;
import com.inq.wishhair.wesharewishhair.user.domain.UserRepository;
import com.inq.wishhair.wesharewishhair.point.domain.PointLogRepository;
import com.inq.wishhair.wesharewishhair.user.application.utils.UserValidator;
import com.inq.wishhair.wesharewishhair.user.domain.AiConnector;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

	private final UserRepository userRepository;
	private final UserFindService userFindService;
	private final UserValidator userValidator;
	private final PasswordEncoder passwordEncoder;
	private final ReviewService reviewService;
	private final TokenRepository tokenRepository;
	private final AiConnector connector;
	private final PointLogRepository pointLogRepository;

	@Transactional
	public Long createUser(SignUpRequest request) {

		User user = generateUser(request);
		userValidator.validateNicknameIsNotDuplicated(user.getNickname());

		User saveUser = userRepository.save(user);

		return saveUser.getId();
	}

	@Transactional
	public void deleteUser(Long userId) {
		tokenRepository.deleteByUserId(userId);
		reviewService.deleteReviewByWriter(userId);
		pointLogRepository.deleteByUserId(userId);
		userRepository.deleteById(userId);
	}

	@Transactional
	public void refreshPassword(PasswordRefreshRequest request) {
		User user = userFindService.findByEmail(new Email(request.getEmail()));

		user.updatePassword(Password.encrypt(request.getNewPassword(), passwordEncoder));
	}

	@Transactional
	public void updateUser(Long userId, UserUpdateRequest request) {
		User user = userFindService.findByUserId(userId);
		Nickname newNickname = new Nickname(request.getNickname());

		userValidator.validateNicknameIsNotDuplicated(newNickname);

		user.updateNickname(newNickname);
		user.updateSex(request.getSex());
	}

	@Transactional
	public SimpleResponseWrapper<String> updateFaceShape(Long userId, MultipartFile file) {
		User user = userFindService.findByUserId(userId);
		Tag faceShapeTag = connector.detectFaceShape(file);

		user.updateFaceShape(new FaceShape(faceShapeTag));
		return new SimpleResponseWrapper<>(user.getFaceShapeTag().getDescription());
	}

	@Transactional
	public void updatePassword(Long userId, PasswordUpdateRequest request) {
		User user = userFindService.findByUserId(userId);
		confirmPassword(user, request.getOldPassword());

		user.updatePassword(Password.encrypt(request.getNewPassword(), passwordEncoder));
	}

	private User generateUser(SignUpRequest request) {
		return User.createUser(
			request.getEmail(),
			Password.encrypt(request.getPw(), passwordEncoder),
			request.getName(),
			request.getNickname(),
			request.getSex());
	}

	private void confirmPassword(User user, String password) {
		if (!passwordEncoder.matches(password, user.getPasswordValue())) {
			throw new WishHairException(ErrorCode.USER_WRONG_PASSWORD);
		}
	}
}
