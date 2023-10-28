package com.inq.wishhair.wesharewishhair.user.domain.entity;

import java.util.regex.Pattern;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Embeddable
public class Password {

	private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$";
	private static final Pattern PASSWORD_MATCHER = Pattern.compile(PASSWORD_PATTERN);

	private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Column(name = "pw", nullable = false)
	private String value;

	public Password(String pw) {
		validatePasswordPattern(pw);
		this.value = pw;
	}

	public void confirmPassword(String password) {
		if (!passwordEncoder.matches(password, value)) {
			throw new WishHairException(ErrorCode.USER_WRONG_PASSWORD);
		}
	}

	private void validatePasswordPattern(String pw) {
		if (isNotValidPattern(pw)) {
			throw new WishHairException(ErrorCode.USER_INVALID_PASSWORD);
		}
	}

	private boolean isNotValidPattern(String pw) {
		return !PASSWORD_MATCHER.matcher(pw).matches();
	}
}
