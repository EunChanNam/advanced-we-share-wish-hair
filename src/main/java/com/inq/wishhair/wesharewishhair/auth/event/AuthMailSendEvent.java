package com.inq.wishhair.wesharewishhair.auth.event;

import com.inq.wishhair.wesharewishhair.user.domain.entity.Email;

public record AuthMailSendEvent(Email email, String authKey) {
}
