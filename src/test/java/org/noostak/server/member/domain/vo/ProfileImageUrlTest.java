package org.noostak.server.member.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProfileImageUrlTest {

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @ParameterizedTest
        @DisplayName("유효한 URL로 객체 생성")
        @CsvSource({
                "http://example.com/image.jpg",
                "https://example.com/image.png",
                "https://subdomain.example.com/path/to/image.jpeg",
                "https://example.com?query=image.jpg"
        })
        void shouldCreateProfileImageUrlSuccessfully(String validUrl) {
            ProfileImageUrl profileImageUrl = ProfileImageUrl.from(validUrl);
            assertThat(profileImageUrl.value()).isEqualTo(validUrl);
        }

        @ParameterizedTest
        @DisplayName("URL이 null인 경우 기본 객체 생성")
        @NullSource
        void shouldAllowNullUrl(String nullUrl) {
            ProfileImageUrl profileImageUrl = ProfileImageUrl.from(nullUrl);
            assertThat(profileImageUrl.value()).isNull(); 
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @ParameterizedTest
        @DisplayName("유효하지 않은 형식의 URL")
        @CsvSource({
                "example.com/image.jpg",
                "ftp://example.com/image.png",
                "//example.com/image.jpg",
                "httpexample.com/image.jpg",
                "https:/example.com/image.jpg"
        })
        void shouldThrowExceptionForInvalidUrlFormat(String invalidUrl) {
            assertThatThrownBy(() -> ProfileImageUrl.from(invalidUrl))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR] 프로필 이미지 URL은 유효한 URL이어야 합니다.");
        }
    }
}
