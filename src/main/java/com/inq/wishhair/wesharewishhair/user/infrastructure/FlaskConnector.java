package com.inq.wishhair.wesharewishhair.user.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;
import com.inq.wishhair.wesharewishhair.hairstyle.domain.hashtag.Tag;
import com.inq.wishhair.wesharewishhair.user.domain.AiConnector;

@Component
public class FlaskConnector implements AiConnector {

	private static final String URL = "/fileupload";
	private static final String FILES = "files";

	private final String requestUri;
	private final RestTemplate restTemplate;

	public FlaskConnector(
		@Value("${flask.domain}") String domain,
		RestTemplate restTemplate
	) {
		this.requestUri = domain + URL;
		this.restTemplate = restTemplate;
	}

	@Override
	public Tag detectFaceShape(MultipartFile file) {
		validateFileExist(file);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add(FILES, file.getResource());

		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

		ResponseEntity<String> response = postRequest(request);
		validateResponseStatusIsOk(response.getStatusCode());

		return Tag.valueOf(response.getBody());
	}

	private ResponseEntity<String> postRequest(HttpEntity<MultiValueMap<String, Object>> request) {
		try {
			return restTemplate.postForEntity(requestUri, request, String.class);
		} catch (RestClientException e) {
			throw new WishHairException(ErrorCode.FLASK_SERVER_EXCEPTION);
		}
	}

	private void validateResponseStatusIsOk(HttpStatusCode status) {
		if (!status.is2xxSuccessful()) {
			throw new WishHairException(ErrorCode.FLASK_RESPONSE_ERROR);
		}
	}

	private void validateFileExist(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new WishHairException(ErrorCode.EMPTY_FILE_EX);
		}
	}
}
