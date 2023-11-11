package com.inq.wishhair.wesharewishhair.hairstyle.application;

import org.springframework.stereotype.Service;

import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.HairStyle;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.HairStyleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HairStyleFindService {

	private final HairStyleRepository hairStyleRepository;

	public HairStyle getById(Long id) {
		return hairStyleRepository.findById(id)
			.orElseThrow(() -> new WishHairException(ErrorCode.NOT_EXIST_KEY));
	}
}
