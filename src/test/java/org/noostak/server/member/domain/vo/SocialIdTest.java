package org.noostak.server.member.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SocialIdTest {

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @ParameterizedTest
        @DisplayName("유효한 소셜 ID로 객체 생성")
        @CsvSource({
                "social123",
                "validId",
                "anotherValidId",
                "idWithNumbers123"
        })
        void shouldCreateSocialIdSuccessfully(String validValue) {
            SocialId socialId = SocialId.from(validValue);
            assertThat(socialId.value()).isEqualTo(validValue);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @ParameterizedTest
        @DisplayName("소셜 ID가 null이거나 비어 있는 경우")
        @NullAndEmptySource
        void shouldThrowExceptionForNullOrEmptySocialId(String invalidValue) {
            assertThatThrownBy(() -> SocialId.from(invalidValue))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR] 소셜 ID는 비어 있을 수 없습니다.");
        }
    }
}
