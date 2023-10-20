package com.inq.wishhair.wesharewishhair.user.presentation.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PasswordUpdateRequest {

	private String oldPassword;

	private String newPassword;
}
