package com.inq.wishhair.wesharewishhair.auth.infrastructure.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.springframework.stereotype.Component;

import com.inq.wishhair.wesharewishhair.auth.application.utils.RandomGenerator;

@Component
public class AuthRandomGenerator implements RandomGenerator {

	@Override
	public String generateString() {
		try {
			SecureRandom random = SecureRandom.getInstanceStrong();
			return String.valueOf(random.nextInt(8999) + 1000);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("NoSuchAlgorithmException");
		}
	}
}
