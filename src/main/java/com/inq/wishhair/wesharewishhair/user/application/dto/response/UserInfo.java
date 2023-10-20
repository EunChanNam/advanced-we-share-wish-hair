package com.inq.wishhair.wesharewishhair.user.application.dto.response;

import com.inq.wishhair.wesharewishhair.user.domain.entity.User;

public record UserInfo(
	String nickname,
	boolean hasFaceShape,
	String faceShapeTag
) {
	public UserInfo(User user) {
		this(
			user.getNicknameValue(),
			user.existFaceShape(),
			user.existFaceShape() ? user.getFaceShapeTag().getDescription() : null
		);
	}
}
