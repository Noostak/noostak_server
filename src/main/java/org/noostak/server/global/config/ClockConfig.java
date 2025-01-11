package org.noostak.server.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ClockConfig {

    /**
     * 시스템 기본 시간대를 사용하는 Clock 빈 생성
     */
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
