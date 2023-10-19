package com.inq.wishhair.wesharewishhair.auth.domain.entity;

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
public class AuthCode {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String email;

	private String code;

	public AuthCode(
		final String email,
		final String code
	) {
		this.email = email;
		this.code = code;
	}

	public void updateCode(final String code) {
		this.code = code;
	}
}
