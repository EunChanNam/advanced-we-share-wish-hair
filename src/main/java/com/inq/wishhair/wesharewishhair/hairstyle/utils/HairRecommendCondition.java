package com.inq.wishhair.wesharewishhair.hairstyle.utils;

import java.util.List;

import com.inq.wishhair.wesharewishhair.hairstyle.domain.hashtag.Tag;
import com.inq.wishhair.wesharewishhair.user.domain.entity.FaceShape;
import com.inq.wishhair.wesharewishhair.user.domain.entity.Sex;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HairRecommendCondition {

	private List<Tag> tags;
	private Tag userFaceShape;
	private Sex sex;

	public static HairRecommendCondition mainRecommend(List<Tag> tags, Tag userFaceShape, Sex sex) {
		return new HairRecommendCondition(tags, userFaceShape, sex);
	}

	public static HairRecommendCondition subRecommend(FaceShape faceShape, Sex sex) {
		if (faceShape == null || faceShape.getTag() == null) {
			return new HairRecommendCondition(null, null, sex);
		}
		return new HairRecommendCondition(null, faceShape.getTag(), sex);
	}
}
