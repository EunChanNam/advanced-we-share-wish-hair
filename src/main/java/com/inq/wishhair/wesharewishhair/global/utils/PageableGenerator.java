package com.inq.wishhair.wesharewishhair.global.utils;

import static com.inq.wishhair.wesharewishhair.global.utils.SortCondition.*;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PageableGenerator {

	public static Pageable getDefaultPageable() {
		return PageRequest.of(0, 4);
	}

	public static Pageable generateSimplePageable(int size) {
		return PageRequest.of(0, size);
	}

	public static Pageable generateDateDescPageable(int size) {
		return PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, DATE));
	}

	public static Pageable generateDateAscPageable(int size) {
		return PageRequest.of(0, size, Sort.by(Sort.Direction.ASC, DATE));
	}
}
