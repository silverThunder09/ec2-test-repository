package com.ec2test.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private static final Duration PRESIGNED_URL_EXPIRATION = Duration.ofDays(7);

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${app.s3.bucket}")
    private String bucket;

    public String uploadProfileImage(Long memberId, MultipartFile file) {
        validateImage(file);

        String key = createProfileImageKey(memberId, file.getOriginalFilename());

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );

            return key;
        } catch (IOException exception) {
            throw new IllegalStateException("프로필 이미지 업로드에 실패했습니다.", exception);
        }
    }

    public PresignedUrlResult createProfileImageDownloadUrl(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(PRESIGNED_URL_EXPIRATION)
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest =
                s3Presigner.presignGetObject(presignRequest);

        return new PresignedUrlResult(
                presignedRequest.url().toString(),
                LocalDateTime.now().plusDays(7)
        );
    }

    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 이미지 파일이 비어 있습니다.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
        }
    }

    private String createProfileImageKey(Long memberId, String originalFilename) {
        String extension = extractExtension(originalFilename);
        return "members/" + memberId + "/profile/" + UUID.randomUUID() + extension;
    }

    private String extractExtension(String originalFilename) {
        if (originalFilename == null || !originalFilename.contains(".")) {
            return "";
        }

        return originalFilename.substring(originalFilename.lastIndexOf("."));
    }

    @Getter
    @AllArgsConstructor
    public static class PresignedUrlResult {

        private String url;
        private LocalDateTime expiresAt;
    }
}
