package com.inq.wishhair.wesharewishhair.user.presentation.dto.request;

import com.inq.wishhair.wesharewishhair.user.domain.entity.Sex;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SignUpRequest {

	@NotNull
	private String email;

	@NotNull
	private String pw;

	@NotNull
	private String name;

	@NotNull
	private String nickname;

	@NotNull
	private Sex sex;
}
