package com.inq.wishhair.wesharewishhair.user.application;

import static com.inq.wishhair.wesharewishhair.user.fixture.UserFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.inq.wishhair.wesharewishhair.auth.domain.TokenRepository;
import com.inq.wishhair.wesharewishhair.common.support.MockTestSupport;
import com.inq.wishhair.wesharewishhair.common.utils.FileMockingUtils;
import com.inq.wishhair.wesharewishhair.global.dto.response.SimpleResponseWrapper;
import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.hashtag.Tag;
import com.inq.wishhair.wesharewishhair.point.domain.PointLogRepository;
import com.inq.wishhair.wesharewishhair.review.application.ReviewService;
import com.inq.wishhair.wesharewishhair.user.application.utils.UserValidator;
import com.inq.wishhair.wesharewishhair.user.domain.AiConnector;
import com.inq.wishhair.wesharewishhair.user.domain.UserRepository;
import com.inq.wishhair.wesharewishhair.user.domain.entity.Email;
import com.inq.wishhair.wesharewishhair.user.domain.entity.User;
import com.inq.wishhair.wesharewishhair.user.presentation.dto.request.UserUpdateRequest;

@DisplayName("[UserService 테스트] - Application")
class UserServiceTest extends MockTestSupport {

	@InjectMocks
	private UserService userService;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private UserRepository userRepository;
	@Mock
	private UserFindService userFindService;
	@Mock
	private UserValidator userValidator;
	@Mock
	private ReviewService reviewService;
	@Mock
	private TokenRepository tokenRepository;
	@Mock
	private AiConnector connector;
	@Mock
	private PointLogRepository pointLogRepository;

	@Test
	@DisplayName("[회원가입을 한다]")
	void createUser() {
		//given
		User user = getFixedManUser();
		ReflectionTestUtils.setField(user, "id", 1L);

		given(userRepository.save(any(User.class)))
			.willReturn(user);

		//when
		Long actual = userService.createUser(getSignUpRequest());

		//then
		assertThat(actual).isEqualTo(1L);
	}

	@Test
	@DisplayName("[회원탈퇴를 한다]")
	void deleteUser() {
		//when
		Executable when = () -> userService.deleteUser(1L);

		//then
		assertDoesNotThrow(when);
	}

	@Test
	@DisplayName("[비밀번호를 갱신한다(비밀번호 찾기)]")
	void refreshPassword() {
		//given
		User user = getFixedManUser();
		given(userFindService.findByEmail(any(Email.class)))
			.willReturn(user);

		//when
		Executable when = () -> userService.refreshPassword(getPasswordRefreshRequest());

		//then
		assertDoesNotThrow(when);
	}

	@Test
	@DisplayName("[회원정보를 변경한다]")
	void updateUser() {
		//given
		Long userId = 1L;
		UserUpdateRequest request = getUserUpdateRequest();
		User user = getFixedManUser();

		given(userFindService.findByUserId(userId))
			.willReturn(user);

		//when
		Executable when = () -> userService.updateUser(userId, request);

		//then
		assertDoesNotThrow(when);
	}

	@Test
	@DisplayName("[얼굴형 정보를 업데이트한다]")
	void updateFaceShape() throws IOException {
		//given
		MultipartFile file = FileMockingUtils.createMockMultipartFile("hello2.jpg");
		User user = getFixedManUser();
		Tag tag = Tag.OBLONG;

		given(userFindService.findByUserId(1L))
			.willReturn(user);
		given(connector.detectFaceShape(file))
			.willReturn(tag);

		//when
		SimpleResponseWrapper<String> actual = userService.updateFaceShape(1L, file);

		//then
		assertThat(actual.getResult()).isEqualTo(tag.getDescription());
	}

	@Nested
	@DisplayName("[비밀번호를 변경한다(비밀번호 변경)]")
	class updatePassword {

		@Test
		@DisplayName("[성공적으로 변경한다]")
		void success() {
			//given
			User user = getFixedManUser();

			given(userFindService.findByUserId(1L))
				.willReturn(user);

			given(passwordEncoder.matches(any(), any()))
				.willReturn(true);

			//when
			Executable when = () -> userService.updatePassword(1L, getPasswordUpdateRequest());

			//then
			assertDoesNotThrow(when);
		}

		@Test
		@DisplayName("[비밀번호가 일치하지 않아 실패한다]")
		void fail() {
			//given
			User user = getFixedManUser();

			given(userFindService.findByUserId(1L))
				.willReturn(user);

			given(passwordEncoder.matches(any(), any()))
				.willReturn(false);

			//when
			ThrowingCallable when = () -> userService.updatePassword(1L, getPasswordUpdateRequest());

			//then
			assertThatThrownBy(when)
				.isInstanceOf(WishHairException.class)
				.hasMessageContaining(ErrorCode.USER_WRONG_PASSWORD.getMessage());
		}
	}
}