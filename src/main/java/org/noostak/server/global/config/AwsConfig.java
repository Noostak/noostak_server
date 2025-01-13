package org.noostak.server.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {

    private final String accessKey;
    private final String secretKey;
    private final String regionString;

    public AwsConfig(@Value("${aws-property.access-key}") final String accessKey,
                     @Value("${aws-property.secret-key}") final String secretKey,
                     @Value("${aws-property.aws-region}") final String regionString) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.regionString = regionString;
    }
}
