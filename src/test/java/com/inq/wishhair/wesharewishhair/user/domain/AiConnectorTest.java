package com.inq.wishhair.wesharewishhair.user.domain;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.inq.wishhair.wesharewishhair.common.utils.FileMockingUtils;
import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.hashtag.Tag;
import com.inq.wishhair.wesharewishhair.user.infrastructure.FlaskConnector;

@DisplayName("[AiConnector 테스트] - Domain")
class AiConnectorTest {

	private static final String DOMAIN = "https://hello_domain/";
	private static final String URL = "/fileupload";

	private final AiConnector aiConnector;
	private final RestTemplate restTemplate;

	public AiConnectorTest() {
		this.restTemplate = Mockito.mock(RestTemplate.class);
		this.aiConnector = new FlaskConnector(DOMAIN, restTemplate);
	}

	@Nested
	@DisplayName("[AI 서버를 통해 얼굴형을 탐지한다]")
	class detectFaceShape {

		@Test
		@DisplayName("[얼굴형 탐지에 성공한다]")
		void success() throws IOException {
			//given
			MultipartFile file = FileMockingUtils.createMockMultipartFile("hello1.jpg");
			Tag tag = Tag.ROUND;
			given(restTemplate.postForEntity(eq(DOMAIN + URL), any(), eq(String.class)))
				.willReturn(ResponseEntity.ok(tag.name()));

			//when
			Tag actual = aiConnector.detectFaceShape(file);

			//then
			assertThat(actual).isEqualTo(tag);
		}

		@Nested
		@DisplayName("[얼굴형 탐지에 실패한다]")
		class fail {

			@Test
			@DisplayName("[AI 서버와의 통신 오류로 실패한다]")
			void failByConnect() throws IOException {
				//given
				MultipartFile file = FileMockingUtils.createMockMultipartFile("hello1.jpg");
				given(restTemplate.postForEntity(eq(DOMAIN + URL), any(), eq(String.class)))
					.willThrow(new RestClientException("msg"));

				//when
				ThrowingCallable when = () -> aiConnector.detectFaceShape(file);

				//then
				assertThatThrownBy(when)
					.isInstanceOf(WishHairException.class)
					.hasMessageContaining(ErrorCode.FLASK_SERVER_EXCEPTION.getMessage());
			}

			@Test
			@DisplayName("[AI 서버의 실패 응답으로 실패한다]")
			void failByFailResponse() throws IOException {
				//given
				MultipartFile file = FileMockingUtils.createMockMultipartFile("hello1.jpg");
				given(restTemplate.postForEntity(eq(DOMAIN + URL), any(), eq(String.class)))
					.willReturn(ResponseEntity.badRequest().build());

				//when
				ThrowingCallable when = () -> aiConnector.detectFaceShape(file);

				//then
				assertThatThrownBy(when)
					.isInstanceOf(WishHairException.class)
					.hasMessageContaining(ErrorCode.FLASK_RESPONSE_ERROR.getMessage());
			}
		}
	}
}