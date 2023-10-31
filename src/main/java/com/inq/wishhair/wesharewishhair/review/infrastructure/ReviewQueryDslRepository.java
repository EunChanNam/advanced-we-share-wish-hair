package com.inq.wishhair.wesharewishhair.review.infrastructure;

import static com.inq.wishhair.wesharewishhair.global.utils.SortCondition.*;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.inq.wishhair.wesharewishhair.review.domain.ReviewQueryRepository;
import com.inq.wishhair.wesharewishhair.review.domain.entity.QReview;
import com.inq.wishhair.wesharewishhair.review.domain.entity.Review;
import com.inq.wishhair.wesharewishhair.review.domain.likereview.QLikeReview;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewQueryDslRepository implements ReviewQueryRepository {

	private final JPAQueryFactory factory;

	private final QReview review = new QReview("r");
	private final QLikeReview like = new QLikeReview("l");

	@Override
	public Optional<Review> findReviewById(Long id) {
		return Optional.ofNullable(
			factory
				.select(review)
				.from(review)
				.leftJoin(review.hairStyle)
				.fetchJoin()
				.leftJoin(review.writer)
				.fetchJoin()
				.where(review.id.eq(id))
				.fetchOne()
		);
	}

	@Override
	public Slice<Review> findReviewByPaging(Pageable pageable) {
		List<Review> result = factory
			.select(review)
			.from(review)
			.leftJoin(review.hairStyle)
			.fetchJoin()
			.leftJoin(review.writer)
			.fetchJoin()
			.orderBy(applyOrderBy(pageable))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1L)
			.fetch();

		return new SliceImpl<>(result, pageable, validateHasNext(pageable, result));
	}

	@Override
	public Slice<Review> findReviewByLike(Long userId, Pageable pageable) {
		List<Long> filteredReviewId = factory
			.select(review.id)
			.from(review)
			.leftJoin(like).on(review.id.eq(like.reviewId))
			.where(like.userId.eq(userId))
			.groupBy(review.id)
			.fetch();

		List<Review> result = factory
			.select(review)
			.from(review)
			.leftJoin(review.writer)
			.fetchJoin()
			.leftJoin(review.hairStyle)
			.fetchJoin()
			.where(review.id.in(filteredReviewId))
			.orderBy(review.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1L)
			.fetch();

		return new SliceImpl<>(result, pageable, validateHasNext(pageable, result));
	}

	@Override
	public Slice<Review> findReviewByUser(Long userId, Pageable pageable) {
		JPAQuery<Review> query = factory
			.select(review)
			.from(review)
			.leftJoin(review.hairStyle)
			.fetchJoin()
			.leftJoin(review.writer)
			.fetchJoin()
			.orderBy(applyOrderBy(pageable))
			.where(review.writer.id.eq(userId))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1L);

		List<Review> result = query.fetch();
		return new SliceImpl<>(result, pageable, validateHasNext(pageable, result));
	}

	@Override
	public List<Review> findReviewByCreatedDate() {
		LocalDateTime startDate = generateStartDate();
		LocalDateTime endDate = generateEndDate();

		return factory
			.select(review)
			.from(review)
			.leftJoin(review.hairStyle)
			.fetchJoin()
			.leftJoin(review.writer)
			.fetchJoin()
			.where(review.createdDate.between(startDate, endDate))
			.orderBy(review.likeCount.desc())
			.offset(0)
			.limit(4)
			.fetch();
	}

	@Override
	public List<Review> findReviewByHairStyle(Long hairStyleId) {
		return factory
			.select(review)
			.from(review)
			.leftJoin(review.hairStyle)
			.fetchJoin()
			.leftJoin(review.writer)
			.fetchJoin()
			.where(review.hairStyle.id.eq(hairStyleId))
			.orderBy(review.likeCount.desc())
			.offset(0)
			.limit(4)
			.fetch();
	}

	private OrderSpecifier<?>[] applyOrderBy(Pageable pageable) {
		List<OrderSpecifier<?>> orderBy = new LinkedList<>();
		String sort = pageable.getSort().toString().replace(": ", ".");

		switch (sort) {
			case LIKES_DESC -> {
				orderBy.add(review.likeCount.desc());
				orderBy.add(review.id.desc());
			}
			case DATE_DESC -> orderBy.add(review.id.desc());
			case DATE_ASC -> orderBy.add(review.id.asc());
		}
		return orderBy.toArray(OrderSpecifier[]::new);
	}

	private boolean validateHasNext(Pageable pageable, List<Review> result) {
		if (result.size() > pageable.getPageSize()) {
			result.remove(pageable.getPageSize());
			return true;
		}
		return false;
	}

	private LocalDateTime generateStartDate() {
		return LocalDateTime.now().minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0);
	}

	private LocalDateTime generateEndDate() {
		return LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0);
	}
}
