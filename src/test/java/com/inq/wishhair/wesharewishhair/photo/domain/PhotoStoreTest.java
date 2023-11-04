package com.inq.wishhair.wesharewishhair.photo.domain;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.inq.wishhair.wesharewishhair.common.utils.FileMockingUtils;
import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;
import com.inq.wishhair.wesharewishhair.photo.infrastructure.S3PhotoStore;

@DisplayName("[PhotoStore 테스트] - Domain")
class PhotoStoreTest {

	private static final String BUCKET_NAME = "bucket";

	private final PhotoStore photoStore;
	private final AmazonS3Client amazonS3Client;

	public PhotoStoreTest() {
		this.amazonS3Client = Mockito.mock(AmazonS3Client.class);
		this.photoStore = new S3PhotoStore(amazonS3Client, "bucket");
	}

	@Nested
	@DisplayName("[이미지를 업로드한다]")
	class uploadFiles {

		@Test
		@DisplayName("[성공적으로 업로드한다]")
		void success() throws IOException {
			//given
			MultipartFile file = FileMockingUtils.createMockMultipartFile("hello1.jpg");

			URL url = URI.create("http://localhost:8080/test/url").toURL();
			given(amazonS3Client.getUrl(eq(BUCKET_NAME), anyString()))
				.willReturn(url);

			//when
			List<String> actual = photoStore.uploadFiles(List.of(file));

			//then
			assertThat(actual).hasSize(1);
			assertThat(actual.get(0)).isEqualTo(url.toString());
		}

		@Test
		@DisplayName("[이미지 업로드에 실패한다]")
		void fail() throws IOException {
			//given
			MultipartFile file = FileMockingUtils.createMockMultipartFile("hello1.jpg");

			given(amazonS3Client.putObject(any(PutObjectRequest.class)))
				.willThrow(new WishHairException(ErrorCode.FILE_TRANSFER_EX));

			//when
			ThrowingCallable when = () -> photoStore.uploadFiles(List.of(file));

			//then
			assertThatThrownBy(when)
				.isInstanceOf(WishHairException.class)
				.hasMessageContaining(ErrorCode.FILE_TRANSFER_EX.getMessage());
		}
	}

	@Test
	@DisplayName("[이미지를 삭제한다]")
	void fail() {
		//given
		String url = "http://localhost:8080/" + UUID.randomUUID();

		//when
		boolean actual = photoStore.deleteFiles(List.of(url));

		//then
		assertThat(actual).isTrue();
	}
}