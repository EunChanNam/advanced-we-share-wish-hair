package com.inq.wishhair.wesharewishhair.auth.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthKeyRequest(
	@NotBlank
	String email,
	@NotNull
	String authKey
) {
}
