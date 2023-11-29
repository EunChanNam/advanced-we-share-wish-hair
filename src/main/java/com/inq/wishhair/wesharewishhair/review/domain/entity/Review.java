package com.inq.wishhair.wesharewishhair.review.domain.entity;

import java.util.ArrayList;
import java.util.List;

import com.inq.wishhair.wesharewishhair.global.auditing.BaseEntity;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.HairStyle;
import com.inq.wishhair.wesharewishhair.photo.domain.Photo;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Review extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private User writer;

	@Embedded
	private Contents contents;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Score score;

	@OneToMany(mappedBy = "review", cascade = CascadeType.PERSIST) // 사진을 값타입 컬렉션 처럼 사용
	private final List<Photo> photos = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hair_style_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private HairStyle hairStyle;

	private long likeCount = 0;

	private Review(User writer, String contents, Score score, List<String> photos, HairStyle hairStyle) {
		this.writer = writer;
		this.contents = new Contents(contents);
		this.score = score;
		applyPhotos(photos);
		this.hairStyle = hairStyle;
	}

	//==Factory method==//
	public static Review createReview(
		User user,
		String contents,
		Score score,
		List<String> photos,
		HairStyle hairStyle
	) {
		return new Review(user, contents, score, photos, hairStyle);
	}

	//==Business method==//
	public String getContentsValue() {
		return contents.getValue();
	}

	public boolean isWriter(Long userId) {
		return this.writer.getId().equals(userId);
	}

	private void applyPhotos(List<String> storeUrls) {
		storeUrls.forEach(storeUrl -> photos.add(Photo.createReviewPhoto(storeUrl, this)));
	}

	public void updateReview(Contents contents, Score score, List<String> storeUrls) {
		updateContents(contents);
		updateScore(score);
		updatePhotos(storeUrls);
	}

	public void addLike() {
		this.likeCount++;
	}

	private void updateContents(Contents contents) {
		this.contents = contents;
	}

	private void updateScore(Score score) {
		this.score = score;
	}

	private void updatePhotos(List<String> storeUrls) {
		this.photos.clear();
		applyPhotos(storeUrls);
	}
}
