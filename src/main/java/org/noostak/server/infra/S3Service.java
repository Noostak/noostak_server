package org.noostak.server.infra;

import org.noostak.server.global.config.AwsConfig;
import org.noostak.server.global.config.AwsProperties;
import org.noostak.server.infra.error.S3UploadErrorCode;
import org.noostak.server.infra.error.S3UploadException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class S3Service {

    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList("image/jpeg", "image/png", "image/jpg", "image/webp");

    private final AwsProperties awsProperties;
    private final AwsConfig awsConfig;

    public S3Service(AwsProperties awsProperties, AwsConfig awsConfig) {
        this.awsProperties = awsProperties;
        this.awsConfig = awsConfig;
    }

    public String uploadImage(String directoryPath, MultipartFile image) throws IOException {
        final String key = directoryPath + generateImageFileName();
        final S3Client s3Client = awsConfig.getS3Client();

        validateExtension(image);
        validateFileSize(image);

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(awsProperties.getS3BucketName())
                .key(key)
                .contentType(image.getContentType())
                .contentDisposition("inline")
                .build();

        RequestBody requestBody = RequestBody.fromBytes(image.getBytes());
        s3Client.putObject(request, requestBody);
        return key;
    }

    public void deleteImage(String key) throws IOException {
        final S3Client s3Client = awsConfig.getS3Client();

        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(awsProperties.getS3BucketName())
                .key(key)
                .build();

        s3Client.deleteObject(deleteRequest);
    }

    private String generateImageFileName() {
        return UUID.randomUUID() + ".jpg";
    }

    private void validateExtension(MultipartFile image) {
        String contentType = image.getContentType();
        if (!IMAGE_EXTENSIONS.contains(contentType)) {
            throw new S3UploadException(S3UploadErrorCode.INVALID_EXTENSION);
        }
    }

    private void validateFileSize(MultipartFile image) {
        if (image.getSize() > awsProperties.getMaxFileSize()) {
            throw new S3UploadException(S3UploadErrorCode.FILE_SIZE_EXCEEDED);
        }
    }
}