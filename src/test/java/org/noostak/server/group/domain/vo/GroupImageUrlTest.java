package org.noostak.server.group.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.noostak.server.group.common.GroupErrorCode;
import org.noostak.server.group.common.GroupException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GroupImageUrlTest {

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
        void shouldCreateGroupImageUrlSuccessfully(String validUrl) {
            // Given
            String url = validUrl;

            // When
            GroupImageUrl groupImageUrl = GroupImageUrl.from(url);

            // Then
            assertThat(groupImageUrl.value()).isEqualTo(validUrl);
        }

        @ParameterizedTest
        @DisplayName("URL이 null인 경우 기본 객체 생성")
        @NullSource
        void shouldAllowNullUrl(String nullUrl) {
            // Given
            String url = nullUrl;

            // When
            GroupImageUrl groupImageUrl = GroupImageUrl.from(url);

            // Then
            assertThat(groupImageUrl.value()).isNull();
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
            // Given
            String url = invalidUrl;

            // When & Then
            assertThatThrownBy(() -> GroupImageUrl.from(url))
                    .isInstanceOf(GroupException.class)
                    .hasMessageContaining(GroupErrorCode.INVALID_GROUP_IMAGE_URL.getMessage());
        }
    }
}
