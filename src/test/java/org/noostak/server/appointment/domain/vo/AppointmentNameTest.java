package org.noostak.server.appointment.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AppointmentNameTest {

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @ParameterizedTest
        @DisplayName("유효한 약속 이름으로 객체 생성")
        @CsvSource({
                "Team Meeting",
                "한글 약속",
                "Meeting123",
                "1234567890",
                "Lunch 🍴",
                "🎉 Party",
                "@Special_Event",
                "Valid-Name",
                "Group: Study",
                "sepecial#!@#!$"
        })
        void shouldCreateAppointmentNameSuccessfully(String validName) {
            // Given
            String appointmentName = validName;

            // When
            AppointmentName generatedName = AppointmentName.from(appointmentName);

            // Then
            assertThat(generatedName.value()).isEqualTo(validName);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @ParameterizedTest
        @DisplayName("약속 이름이 null인 경우 예외 발생")
        @NullSource
        void shouldThrowExceptionForNullName(String nullName) {
            // Given
            String appointmentName = nullName;

            // When & Then
            assertThatThrownBy(() -> AppointmentName.from(appointmentName))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR] 약속 이름은 비어 있을 수 없습니다.");
        }

        @ParameterizedTest
        @DisplayName("약속 이름이 빈 문자열인 경우 예외 발생")
        @CsvSource({
                "''"
        })
        void shouldThrowExceptionForEmptyString(String emptyName) {
            // Given
            String appointmentName = emptyName;

            // When & Then
            assertThatThrownBy(() -> AppointmentName.from(appointmentName))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR] 약속 이름은 비어 있을 수 없습니다.");
        }

        @ParameterizedTest
        @DisplayName("약속 이름이 공백 문자열인 경우 예외 발생")
        @CsvSource({
                "'   '"
        })
        void shouldThrowExceptionForBlankString(String blankName) {
            // Given
            String appointmentName = blankName;

            // When & Then
            assertThatThrownBy(() -> AppointmentName.from(appointmentName))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR] 약속 이름은 비어 있을 수 없습니다.");
        }

        @ParameterizedTest
        @DisplayName("약속 이름의 길이가 30자를 초과하는 경우 예외 발생")
        @CsvSource({
                "abcdefghijklmnopqrstuvwxyz12345",
                "1234567890123456789012345678901"
        })
        void shouldThrowExceptionForNameExceedingMaxLength(String invalidName) {
            // Given
            String appointmentName = invalidName;

            // When & Then
            assertThatThrownBy(() -> AppointmentName.from(appointmentName))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR] 약속 이름의 길이는 30글자를 넘을 수 없습니다.");
        }
    }
}
