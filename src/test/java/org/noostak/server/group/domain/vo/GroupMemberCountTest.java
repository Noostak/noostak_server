package org.noostak.server.group.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.noostak.server.group.common.GroupErrorCode;
import org.noostak.server.group.common.GroupException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GroupMemberCountTest {

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @Test
        @DisplayName("초기 멤버 수가 0인 객체 생성")
        void shouldCreateWithZeroInitialValue() {
            // Given
            Long initialCount = 0L;

            // When
            GroupMemberCount count = GroupMemberCount.from(initialCount);

            // Then
            assertThat(count.value()).isEqualTo(0L);
        }

        @Test
        @DisplayName("멤버 수 증가")
        void shouldIncreaseMemberCount() {
            // Given
            GroupMemberCount count = GroupMemberCount.from(10L);

            // When
            GroupMemberCount increasedCount = count.increase();

            // Then
            assertThat(increasedCount.value()).isEqualTo(11L);
        }

        @Test
        @DisplayName("멤버 수 감소")
        void shouldDecreaseMemberCount() {
            // Given
            GroupMemberCount count = GroupMemberCount.from(10L);

            // When
            GroupMemberCount decreasedCount = count.decrease();

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
            GroupMemberCount count = GroupMemberCount.from(0L);

            // When & Then
            assertThatThrownBy(count::decrease)
                    .isInstanceOf(GroupException.class)
                    .hasMessageContaining(GroupErrorCode.MEMBER_COUNT_NEGATIVE.getMessage());
        }

        @Test
        @DisplayName("멤버 수가 최대값을 초과할 경우 예외 발생")
        void shouldThrowExceptionWhenCountExceedsMaxLimit() {
            // Given
            GroupMemberCount count = GroupMemberCount.from(50L);

            // When & Then
            assertThatThrownBy(count::increase)
                    .isInstanceOf(GroupException.class)
                    .hasMessageContaining(GroupErrorCode.MEMBER_COUNT_EXCEEDS_MAX_LIMIT.getMessage());
        }

        @Test
        @DisplayName("초기값이 음수일 경우 예외 발생")
        void shouldThrowExceptionWhenInitialValueIsNegative() {
            // Given
            Long initialCount = -1L;

            // When & Then
            assertThatThrownBy(() -> GroupMemberCount.from(initialCount))
                    .isInstanceOf(GroupException.class)
                    .hasMessageContaining(GroupErrorCode.MEMBER_COUNT_INITIAL_NEGATIVE.getMessage());
        }
    }
}
