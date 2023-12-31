package com.inq.wishhair.wesharewishhair.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.inq.wishhair.wesharewishhair.auth.presentation.AuthController;
import com.inq.wishhair.wesharewishhair.auth.presentation.MailAuthController;
import com.inq.wishhair.wesharewishhair.auth.presentation.TokenReissueController;
import com.inq.wishhair.wesharewishhair.hairstyle.presentation.HairStyleSearchController;
import com.inq.wishhair.wesharewishhair.hairstyle.presentation.WishHairController;
import com.inq.wishhair.wesharewishhair.review.presentation.LikeReviewController;
import com.inq.wishhair.wesharewishhair.review.presentation.ReviewController;
import com.inq.wishhair.wesharewishhair.review.presentation.ReviewSearchController;
import com.inq.wishhair.wesharewishhair.point.presentation.PointController;
import com.inq.wishhair.wesharewishhair.user.presentation.UserController;
import com.inq.wishhair.wesharewishhair.user.presentation.UserInfoController;

@RestControllerAdvice(assignableTypes = {
	UserController.class, AuthController.class, HairStyleSearchController.class,
	ReviewController.class, WishHairController.class, AuthController.class,
	TokenReissueController.class, MailAuthController.class,
	UserInfoController.class, LikeReviewController.class, ReviewSearchController.class,
	PointController.class
})
public class ApiExceptionHandler {

	@ExceptionHandler(WishHairException.class)
	public ResponseEntity<ErrorResponse> handleWishHairException(WishHairException e) {
		return convert(e.getErrorCode());
	}

	@ExceptionHandler({MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class})
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException() {
		return convert(ErrorCode.GLOBAL_VALIDATION_ERROR);
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<ErrorResponse> handelNotSupportedUriException() {
		return convert(ErrorCode.GLOBAL_NOT_SUPPORTED_URI);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorResponse> handlerHttpRequestMethodNotSupportedException() {
		return convert(ErrorCode.GLOBAL_NOT_SUPPORTED_METHOD);
	}

	//    @ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse> handleAnyException() {
		return convert(ErrorCode.GLOBAL_INTERNAL_SERVER_ERROR);
	}

	private ResponseEntity<ErrorResponse> convert(ErrorCode errorCode) {
		return ResponseEntity.status(errorCode.getHttpStatus()).body(new ErrorResponse(errorCode));
	}

}
