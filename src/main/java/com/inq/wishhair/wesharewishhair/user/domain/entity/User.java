package com.inq.wishhair.wesharewishhair.user.domain.entity;

import com.inq.wishhair.wesharewishhair.hairstyle.domain.hashtag.Tag;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private Email email;

	@Embedded
	private Password password;

	@Column(nullable = false)
	private String name;

	@Embedded
	private Nickname nickname;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Sex sex;

	@Embedded
	private FaceShape faceShape;

	private User(final Email email,
		final Password password,
		final String name,
		final Nickname nickname,
		final Sex sex
	) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.nickname = nickname;
		this.sex = sex;
		this.faceShape = null;
	}

	//=Factory method==//
	public static User createUser(
		final String email,
		final Password password,
		final String name,
		final String nickname,
		final Sex sex
	) {
		return new User(
			new Email(email),
			password,
			name,
			new Nickname(nickname),
			sex
		);
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Sex getSex() {
		return sex;
	}

	public String getEmailValue() {
		return email.getValue();
	}

	public Nickname getNickname() {
		return nickname;
	}

	public Tag getFaceShapeTag() {
		return faceShape.getTag();
	}

	public FaceShape getFaceShape() {
		return faceShape;
	}

	public boolean existFaceShape() {
		return faceShape != null;
	}

	public void updateFaceShape(FaceShape faceShape) {
		this.faceShape = faceShape;
	}

	public void updatePassword(Password password) {
		this.password = password;
	}

	public void updateNickname(Nickname nickname) {
		this.nickname = nickname;
	}

	public void updateSex(Sex sex) {
		this.sex = sex;
	}

	public String getPasswordValue() {
		return password.getValue();
	}

	public String getNicknameValue() {
		return nickname.getValue();
	}
}
