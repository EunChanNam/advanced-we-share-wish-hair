package com.inq.wishhair.wesharewishhair.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Token {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId;

	@Column(nullable = false, unique = true)
	private String refreshToken;

	private Token(final Long userId, final String refreshToken) {
		this.userId = userId;
		this.refreshToken = refreshToken;
	}

	//Factory method
	public static Token issue(final Long userId, final String refreshToken) {
		return new Token(userId, refreshToken);
	}

	//Business method
	public void updateRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
