package com.inq.wishhair.wesharewishhair.user.service.dto.response;

import org.springframework.data.domain.Slice;

import com.inq.wishhair.wesharewishhair.global.dto.response.PagedResponse;
import com.inq.wishhair.wesharewishhair.point.application.dto.PointResponse;
import com.inq.wishhair.wesharewishhair.point.domain.PointLog;
import com.inq.wishhair.wesharewishhair.user.domain.User;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class UserResponseAssembler {

	public static PagedResponse<PointResponse> toPagedResponse(Slice<PointLog> slice) {
		return new PagedResponse<>(transferContentToResponse(slice));
	}

	private static Slice<PointResponse> transferContentToResponse(Slice<PointLog> slice) {
		return slice.map(PointResponse::new);
	}

	public static UserInformation toUserInformation(User user) {
		return new UserInformation(
			user.getEmailValue(),
			user.getName(),
			user.getNicknameValue(),
			user.getSex()
		);
	}
}
