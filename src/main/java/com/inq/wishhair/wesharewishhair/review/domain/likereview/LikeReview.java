package com.inq.wishhair.wesharewishhair.review.domain.likereview;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    private Long userId;

    @JoinColumn
    private Long reviewId;

    //==생성 메서드==//
    private LikeReview(Long userId, Long reviewId) {
        this.userId = userId;
        this.reviewId = reviewId;
    }
    public static LikeReview addLike(Long userId, Long reviewId) {
        return new LikeReview(userId, reviewId);
    }
}
