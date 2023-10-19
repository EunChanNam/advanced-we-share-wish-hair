package com.inq.wishhair.wesharewishhair.global.dto.response;

import java.util.List;

import org.springframework.data.domain.Slice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PagedResponse<T> implements ListResponse<T> {

	private List<T> result;

	private Paging paging;

	public PagedResponse(Slice<T> slice) {
		result = slice.getContent();
		paging = new Paging(result.size(), slice.getNumber(), slice.hasNext());
	}
}
