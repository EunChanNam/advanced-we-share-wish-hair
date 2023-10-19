package com.inq.wishhair.wesharewishhair.global.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SortCondition {
	public static final String LIKES = "likes";
	public static final String LIKES_DESC = "likes.DESC";
	public static final String DATE = "createdDate";
	public static final String DATE_DESC = "createdDate.DESC";
	public static final String DATE_ASC = "createdDate.ASC";
}
