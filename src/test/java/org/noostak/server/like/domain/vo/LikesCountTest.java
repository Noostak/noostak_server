package org.noostak.server.like.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LikesCountTest {

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @Test
        @DisplayName("초기 좋아요 수가 0인 객체 생성")
        void shouldCreateWithZeroInitialValue() {
            // Given
            Long initialCount = 0L;

            // When
            LikesCount likesCount = LikesCount.from(initialCount);

            // Then
            assertThat(likesCount.value()).isEqualTo(initialCount);
        }

        @Test
        @DisplayName("좋아요 수 증가")
        void shouldIncreaseLikesCount() {
            // Given
            LikesCount likesCount = LikesCount.from(10L);

            // When
            LikesCount increasedLikesCount = likesCount.increase();

            // Then
            assertThat(increasedLikesCount.value()).isEqualTo(11L);
        }

        @Test
        @DisplayName("좋아요 수 감소")
        void shouldDecreaseLikesCount() {
            // Given
            LikesCount likesCount = LikesCount.from(10L);

            // When
            LikesCount decreasedLikesCount = likesCount.decrease();

            // Then
            assertThat(decreasedLikesCount.value()).isEqualTo(9L);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @Test
        @DisplayName("좋아요 수가 음수가 될 경우 예외 발생")
        void shouldThrowExceptionWhenCountIsNegative() {
            // Given
            LikesCount likesCount = LikesCount.from(0L);

            // When & Then
            assertThatThrownBy(likesCount::decrease)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR] 좋아요 수는 음수가 될 수 없습니다.");
        }

        @Test
        @DisplayName("좋아요 수가 최대값을 초과할 경우 예외 발생")
        void shouldThrowExceptionWhenCountExceedsMaxLimit() {
            // Given
            LikesCount likesCount = LikesCount.from(50L);

            // When & Then
            assertThatThrownBy(likesCount::increase)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR] 좋아요 수는 최대 50를 초과할 수 없습니다.");
        }

        @Test
        @DisplayName("초기값이 음수일 경우 예외 발생")
        void shouldThrowExceptionWhenInitialValueIsNegative() {
            // Given
            Long initialCount = -1L;

            // When & Then
            assertThatThrownBy(() -> LikesCount.from(initialCount))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR] 좋아요 수는 음수가 될 수 없습니다.");
        }
    }
}
