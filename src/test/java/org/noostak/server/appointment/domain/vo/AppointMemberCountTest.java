package org.noostak.server.appointment.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AppointMemberCountTest {

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @Test
        @DisplayName("초기 멤버 수가 0인 객체 생성")
        void shouldCreateWithZeroInitialValue() {
            // Given
            Long initialCount = 0L;

            // When
            AppointMemberCount count = AppointMemberCount.from(initialCount);

            // Then
            assertThat(count.value()).isEqualTo(initialCount);
        }

        @Test
        @DisplayName("멤버 수 증가")
        void shouldIncreaseMemberCount() {
            // Given
            AppointMemberCount count = AppointMemberCount.from(10L);

            // When
            AppointMemberCount increasedCount = count.increase();

            // Then
            assertThat(increasedCount.value()).isEqualTo(11L);
        }

        @Test
        @DisplayName("멤버 수 감소")
        void shouldDecreaseMemberCount() {
            // Given
            AppointMemberCount count = AppointMemberCount.from(10L);

            // When
            AppointMemberCount decreasedCount = count.decrease();

            // Then
            assertThat(decreasedCount.value()).isEqualTo(9L);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @Test
        @DisplayName("멤버 수가 음수가 될 경우 예외 발생")
        void shouldThrowExceptionWhenCountIsNegative() {
            // Given
            AppointMemberCount count = AppointMemberCount.from(0L);

            // When & Then
            assertThatThrownBy(count::decrease)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR] 약속 멤버 수는 음수가 될 수 없습니다.");
        }

        @Test
        @DisplayName("멤버 수가 최대값을 초과할 경우 예외 발생")
        void shouldThrowExceptionWhenCountExceedsMaxLimit() {
            // Given
            AppointMemberCount count = AppointMemberCount.from(50L);

            // When & Then
            assertThatThrownBy(count::increase)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR] 약속 멤버 수는 최대 50명을 초과할 수 없습니다.");
        }

        @Test
        @DisplayName("초기값이 음수일 경우 예외 발생")
        void shouldThrowExceptionWhenInitialValueIsNegative() {
            // Given
            Long initialCount = -1L;

            // When & Then
            assertThatThrownBy(() -> AppointMemberCount.from(initialCount))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR] 약속 멤버 수는 음수가 될 수 없습니다.");
        }
    }
}
