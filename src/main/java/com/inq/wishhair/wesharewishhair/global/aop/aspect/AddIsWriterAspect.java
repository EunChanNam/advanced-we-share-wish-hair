package com.inq.wishhair.wesharewishhair.global.aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import com.inq.wishhair.wesharewishhair.global.dto.response.ListResponse;
import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;
import com.inq.wishhair.wesharewishhair.review.application.dto.response.ReviewDetailResponse;
import com.inq.wishhair.wesharewishhair.review.application.dto.response.ReviewResponse;

@Aspect
public class AddIsWriterAspect {

	@Pointcut("execution(com.inq.wishhair.wesharewishhair.global.dto.response.PagedResponse *(..)) ||" +
		"execution(com.inq.wishhair.wesharewishhair.global.dto.response.ResponseWrapper *(..))")
	private void listResponsePointcut() {
	}

	@Pointcut("execution(com.inq.wishhair.wesharewishhair.review.application.dto.response.ReviewDetailResponse *(..))")
	private void reviewDetailResponsePointcut() {
	}

	@Pointcut("@annotation(com.inq.wishhair.wesharewishhair.global.aop.annotation.AddisWriter)")
	private void addWriterAnnotation() {
	}

	@SuppressWarnings("unchecked")
	@Around("listResponsePointcut() && addWriterAnnotation() && args(userId, ..)")
	public Object addIsWriterToPagedResponse(ProceedingJoinPoint joinPoint, Long userId) throws Throwable {
		ListResponse<?> result = (ListResponse<?>)joinPoint.proceed();
		if (!result.getResult().isEmpty() && !(result.getResult().get(0) instanceof ReviewResponse)) {
			throw new WishHairException(ErrorCode.AOP_GENERIC_EXCEPTION);
		}
		ListResponse<ReviewResponse> castedResult = (ListResponse<ReviewResponse>)result;
		castedResult.getResult().forEach((response -> response.addIsWriter(userId)));
		return castedResult;
	}

	@Around("reviewDetailResponsePointcut() && addWriterAnnotation() && args(userId, ..)")
	public Object addIsWriterToReviewDetailResponse(ProceedingJoinPoint joinPoint, Long userId) throws Throwable {
		ReviewDetailResponse result = (ReviewDetailResponse)joinPoint.proceed();

		result.reviewResponse().addIsWriter(userId);
		return result;
	}
}
