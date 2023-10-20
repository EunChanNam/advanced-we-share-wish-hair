package com.inq.wishhair.wesharewishhair.user.presentation.dto.request;

import com.inq.wishhair.wesharewishhair.user.domain.entity.Sex;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserUpdateRequest {

	private String nickname;

	private Sex sex;
}
