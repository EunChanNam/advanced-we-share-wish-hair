package com.inq.wishhair.wesharewishhair.photo.infrastructure;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.inq.wishhair.wesharewishhair.global.exception.ErrorCode;
import com.inq.wishhair.wesharewishhair.global.exception.WishHairException;
import com.inq.wishhair.wesharewishhair.photo.domain.PhotoStore;

@Component
public class S3PhotoStore implements PhotoStore {

	private final AmazonS3Client amazonS3Client;
	private final String buketName;

	private void deleteFile(final String storeUrl) {
		int point = storeUrl.indexOf('/', 10) + 1;
		String fileKey = storeUrl.substring(point);
		amazonS3Client.deleteObject(buketName, fileKey);
	}

	private String createStoreFilename(final String originalFilename) {
		String ext = getExt(originalFilename);
		return UUID.randomUUID() + ext;
	}

	private String getExt(final String originalFilename) {
		int index = originalFilename.lastIndexOf(".");
		return originalFilename.substring(index);
	}

	public S3PhotoStore(final AmazonS3Client amazonS3Client,
		@Value("${cloud.aws.s3.bucket}") String buketName) {
		this.amazonS3Client = amazonS3Client;
		this.buketName = buketName;
	}

	public List<String> uploadFiles(final List<MultipartFile> files) {
		List<String> storeUrls = new ArrayList<>();
		files.forEach(file -> storeUrls.add(uploadFile(file)));
		return storeUrls;
	}

	private String uploadFile(final MultipartFile file) {
		String originalFilename = file.getOriginalFilename();
		String storeFilename = createStoreFilename(originalFilename);

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(file.getContentType());
		metadata.setContentLength(file.getSize());

		try (InputStream inputStream = file.getInputStream()) {
			PutObjectRequest putObjectRequest = new PutObjectRequest(
				buketName,
				storeFilename,
				inputStream,
				metadata).withCannedAcl(CannedAccessControlList.PublicRead);

			amazonS3Client.putObject(putObjectRequest);

			return amazonS3Client.getUrl(buketName, storeFilename).toString();
		} catch (IOException e) {
			throw new WishHairException(ErrorCode.FILE_TRANSFER_EX);
		}
	}

	public boolean deleteFiles(final List<String> storeUrls) {
		storeUrls.forEach(this::deleteFile);
		return true;
	}
}