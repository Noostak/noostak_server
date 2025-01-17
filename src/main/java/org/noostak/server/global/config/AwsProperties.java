package org.noostak.server.global.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws-property")
public class AwsProperties {

    private final String s3BucketName;
    private final String accessKey;
    private final String secretKey;
    private final String awsRegion;
    private final Long maxFileSize;

    public AwsProperties(String s3BucketName, String accessKey, String secretKey, String awsRegion, Long maxFileSize) {
        this.s3BucketName = s3BucketName;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.awsRegion = awsRegion;
        this.maxFileSize = maxFileSize;
    }

    public String getS3BucketName() {
        return s3BucketName;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getAwsRegion() {
        return awsRegion;
    }

    public Long getMaxFileSize() {
        return maxFileSize;
    }
}
