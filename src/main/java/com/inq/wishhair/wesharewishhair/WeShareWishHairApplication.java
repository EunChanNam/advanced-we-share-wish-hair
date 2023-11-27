package com.inq.wishhair.wesharewishhair;

import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.inq.wishhair.wesharewishhair.hairstyle.domain.HairStyle;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.HairStyleRepository;
import com.inq.wishhair.wesharewishhair.user.domain.UserRepository;
import com.inq.wishhair.wesharewishhair.user.domain.entity.Password;
import com.inq.wishhair.wesharewishhair.user.domain.entity.Sex;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@SpringBootApplication
public class WeShareWishHairApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeShareWishHairApplication.class, args);
	}

	@Component
	@Profile("prod")
	@RequiredArgsConstructor
	static class TestData {

		private final UserRepository userRepository;
		private final HairStyleRepository hairStyleRepository;
		private final PasswordEncoder passwordEncoder;

		@PostConstruct
		public void setUp() {
			userRepository.save(User.createUser(
				"namhm23@naver.com",
				Password.encrypt("hello123@", passwordEncoder),
				"userA",
				"hello",
				Sex.MAN
			));

			hairStyleRepository.save(HairStyle.createHairStyle("hairStyleA", Sex.MAN, new ArrayList<>(), new ArrayList<>()));
		}
	}

}
