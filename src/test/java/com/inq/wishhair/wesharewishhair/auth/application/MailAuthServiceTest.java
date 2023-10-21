package com.inq.wishhair.wesharewishhair.auth.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import com.inq.wishhair.wesharewishhair.auth.application.utils.RandomGenerator;
import com.inq.wishhair.wesharewishhair.auth.domain.AuthCodeRepository;
import com.inq.wishhair.wesharewishhair.auth.domain.entity.AuthCode;
import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;

@ExtendWith(MockitoExtension.class)
@DisplayName("[AuthService Test] - Application Layer")
class MailAuthServiceTest {

	private static final String CODE = "1234";
	private static final String EMAIL = "hello@naver.com";

	private final MailAuthService mailAuthService;
	private final AuthCodeRepository authCodeRepository;

	public MailAuthServiceTest() {
		this.authCodeRepository = Mockito.mock(AuthCodeRepository.class);
		ApplicationEventPublisher eventPublisher = Mockito.mock(ApplicationEventPublisher.class);
		RandomGenerator randomGenerator = Mockito.mock(RandomGenerator.class);

		this.mailAuthService = new MailAuthService(
			eventPublisher, authCodeRepository, randomGenerator
		);

		given(randomGenerator.generateString()).willReturn(CODE);
	}

	@Nested
	@DisplayName("[인증 메일을 요청한다]")
	class requestMailAuthorization {

		@Test
		@DisplayName("[인증 메일을 요청하고 인증코드를 새로 생성한다]")
		void createNewAuthCode() {
			//given
			given(authCodeRepository.findByEmail(EMAIL))
				.willReturn(Optional.empty());

			//when
			mailAuthService.requestMailAuthorization(EMAIL);

			//then
			verify(authCodeRepository, times(1)).save(any(AuthCode.class));
		}

		@Test
		@DisplayName("[인증 메일을 요청하고 기존의 인증코드가 존재해서 기존 인증코드를 업데이트한다]")
		void updateAuthCode() {
			//given
			AuthCode authCode = new AuthCode(EMAIL, "code");
			given(authCodeRepository.findByEmail(EMAIL))
				.willReturn(Optional.of(authCode));

			//when
			mailAuthService.requestMailAuthorization(EMAIL);

			//then
			assertThat(authCode.getCode()).isEqualTo(CODE);
		}
	}

	@Nested
	@DisplayName("[인증코드가 올바른지 확인한다]")
	class checkAuthCode {

		private static final String CODE = "code";

		public checkAuthCode() {
			//given
			given(authCodeRepository.findByEmail(EMAIL))
				.willReturn(Optional.of(new AuthCode(EMAIL, CODE)));
		}

		@Test
		@DisplayName("[검사에 성공한다]")
		void success() {
			//when
			Executable when = () -> mailAuthService.checkAuthCode(EMAIL, CODE);

			//then
			assertDoesNotThrow(when);
		}

		@Test
		@DisplayName("잘못된 code 로 검사에 실패한다")
		void failByInvalidCode() {
			//when
			ThrowingCallable when = () -> mailAuthService.checkAuthCode(EMAIL, "hello");

			//then
			assertThatThrownBy(when)
				.isInstanceOf(WishHairException.class)
				.hasMessageContaining(ErrorCode.AUTH_INVALID_AUTH_CODE.getMessage());
		}
	}
}