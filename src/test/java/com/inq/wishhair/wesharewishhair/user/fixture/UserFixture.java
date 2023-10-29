package com.inq.wishhair.wesharewishhair.user.fixture;

import java.util.ArrayList;

import com.inq.wishhair.wesharewishhair.hairstyle.domain.hashtag.Tag;
import com.inq.wishhair.wesharewishhair.user.application.dto.response.MyPageResponse;
import com.inq.wishhair.wesharewishhair.user.application.dto.response.UserInfo;
import com.inq.wishhair.wesharewishhair.user.application.dto.response.UserInformation;
import com.inq.wishhair.wesharewishhair.user.domain.entity.Sex;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;
import com.inq.wishhair.wesharewishhair.user.presentation.dto.request.PasswordRefreshRequest;
import com.inq.wishhair.wesharewishhair.user.presentation.dto.request.PasswordUpdateRequest;
import com.inq.wishhair.wesharewishhair.user.presentation.dto.request.SignUpRequest;
import com.inq.wishhair.wesharewishhair.user.presentation.dto.request.UserUpdateRequest;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserFixture {

	private static final String EMAIL = "hello@naver.com";
	private static final String PW = "hello1234@";
	private static final String NAME = "hello";
	private static final String NICKNAME = "hello";

	public static User getFixedManUser() {
		return User.of(
			EMAIL,
			PW,
			NAME,
			NICKNAME,
			Sex.MAN
		);
	}

	public static User getFixedWomanUser() {
		return User.of(
			EMAIL,
			PW,
			NAME,
			NICKNAME,
			Sex.WOMAN
		);
	}

	public static SignUpRequest getSignUpRequest() {
		return new SignUpRequest(
			EMAIL,
			PW,
			NAME,
			NICKNAME,
			Sex.MAN
		);
	}

	public static PasswordRefreshRequest getPasswordRefreshRequest() {
		return new PasswordRefreshRequest(
			EMAIL,
			"newPassword1234!"
		);
	}

	public static UserUpdateRequest getUserUpdateRequest() {
		return new UserUpdateRequest(
			"newNick",
			Sex.WOMAN
		);
	}

	public static PasswordUpdateRequest getPasswordUpdateRequest() {
		return new PasswordUpdateRequest(
			PW,
			"newPassword1234!"
		);
	}

	public static MyPageResponse getMyPageResponse() {
		return new MyPageResponse(
			getFixedManUser(),
			new ArrayList<>(),
			1000
		);
	}

	public static UserInformation getUserInformation() {
		return new UserInformation(
			EMAIL,
			NAME,
			NICKNAME,
			Sex.MAN
		);
	}

	public static UserInfo getUserInfo() {
		User user = getFixedManUser();
		user.updateFaceShape(Tag.ROUND);

		return new UserInfo(user);
	}
}
