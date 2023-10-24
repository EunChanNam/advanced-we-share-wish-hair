package com.inq.wishhair.wesharewishhair.global.interceptor.interceptor;

import static com.inq.wishhair.wesharewishhair.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.inq.wishhair.wesharewishhair.auth.stub.AuthTokenMangerStub;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@DisplayName("[AuthenticationInterceptor 테스트] - Global")
class AuthenticationInterceptorTest {

	private static final String AUTHORIZATION = "Authorization";

	private final AuthenticationInterceptor interceptor;
	private final HttpServletRequest mockRequest;
	private final HttpServletResponse mockResponse;

	public AuthenticationInterceptorTest() {
		mockRequest = Mockito.mock(HttpServletRequest.class);
		mockResponse = Mockito.mock(HttpServletResponse.class);
		this.interceptor = new AuthenticationInterceptor(new AuthTokenMangerStub());
	}

	@Nested
	@DisplayName("[요청에 대해서 인증을 수행한다]")
	class preHandle {

		@Test
		@DisplayName("[인증에 성공한다]")
		void success() {
			//given
			given(mockRequest.getHeader(AUTHORIZATION)).willReturn("Bearer token");

			//when
			boolean actual = interceptor.preHandle(mockRequest, mockResponse, new Object());

			//then
			assertThat(actual).isTrue();
		}

		@Nested
		@DisplayName("[인증에 실패한다]")
		class fail {

			@Test
			@DisplayName("[인증헤더 값이 존재하지 않아 실패한다]")
			void failByNoHeader() {
				//given
				given(mockRequest.getHeader(AUTHORIZATION)).willReturn("");

				//when
				ThrowingCallable when = () -> interceptor.preHandle(
					mockRequest, mockResponse, new Object()
				);

				//then
				assertThatThrownBy(when)
					.isInstanceOf(WishHairException.class)
					.hasMessageContaining(AUTH_REQUIRED_LOGIN.getMessage());
			}

			@Test
			@DisplayName("[인증헤더 값의 형식이 올바르지 않아 실패한다]")
			void failByHeaderFormat() {
				//given
				given(mockRequest.getHeader(AUTHORIZATION)).willReturn("token");

				//when
				ThrowingCallable when = () -> interceptor.preHandle(
					mockRequest, mockResponse, new Object()
				);

				//then
				assertThatThrownBy(when)
					.isInstanceOf(WishHairException.class)
					.hasMessageContaining(AUTH_INVALID_AUTHORIZATION_HEADER.getMessage());
			}
		}
	}
}