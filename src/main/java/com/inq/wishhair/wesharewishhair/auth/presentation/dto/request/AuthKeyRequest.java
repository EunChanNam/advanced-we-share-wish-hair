package com.inq.wishhair.wesharewishhair.auth.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

public record AuthKeyRequest(
	@NotNull
	String authKey
) {
}
