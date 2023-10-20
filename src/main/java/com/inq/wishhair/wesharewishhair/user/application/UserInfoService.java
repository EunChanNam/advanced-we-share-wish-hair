package com.inq.wishhair.wesharewishhair.user.application;

import static com.inq.wishhair.wesharewishhair.user.application.dto.response.UserResponseAssembler.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inq.wishhair.wesharewishhair.global.utils.PageableGenerator;
import com.inq.wishhair.wesharewishhair.point.domain.PointLog;
import com.inq.wishhair.wesharewishhair.point.domain.PointLogRepository;
import com.inq.wishhair.wesharewishhair.review.service.ReviewSearchService;
import com.inq.wishhair.wesharewishhair.review.service.dto.response.ReviewResponse;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;
import com.inq.wishhair.wesharewishhair.user.application.dto.response.MyPageResponse;
import com.inq.wishhair.wesharewishhair.user.application.dto.response.UserInfo;
import com.inq.wishhair.wesharewishhair.user.application.dto.response.UserInformation;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserInfoService {

	private final UserFindService userFindService;
	private final ReviewSearchService reviewSearchService;
	private final PointLogRepository pointLogRepository;

	public MyPageResponse getMyPageInfo(Long userId) {

		Pageable pageable = PageableGenerator.generateDateDescPageable(3);

		List<ReviewResponse> reviewResponses = reviewSearchService.findLikingReviews(userId, pageable).getResult();
		User user = userFindService.findByUserId(userId);

		int point = pointLogRepository.findByUserOrderByCreatedDateDesc(user)
			.map(PointLog::getPoint)
			.orElse(0);

		return new MyPageResponse(user, reviewResponses, point);
	}

	public UserInformation getUserInformation(Long userId) {
		return toUserInformation(userFindService.findByUserId(userId));
	}

	public UserInfo getUserInfo(Long userId) {
		return new UserInfo(userFindService.findByUserId(userId));
	}
}
