package com.inq.wishhair.wesharewishhair.user.domain.point;

import java.time.LocalDateTime;

import com.inq.wishhair.wesharewishhair.global.auditing.BaseEntity;
import com.inq.wishhair.wesharewishhair.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointHistory extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, updatable = false)
	@Enumerated(EnumType.STRING)
	private PointType pointType;

	@Column(nullable = false, updatable = false)
	private int dealAmount;

	@Column(nullable = false, updatable = false)
	private int point;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false, updatable = false)
	private User user;

	//==생성 메서드==//
	private PointHistory(User user, PointType pointType, int dealAmount, int point) {
		this.pointType = pointType;
		this.dealAmount = dealAmount;
		this.point = point;
		this.user = user;
		this.createdDate = LocalDateTime.now();
	}

	public static PointHistory createPointHistory(User user, PointType pointType, int dealAmount, int point) {
		return new PointHistory(user, pointType, dealAmount, point);
	}
}
