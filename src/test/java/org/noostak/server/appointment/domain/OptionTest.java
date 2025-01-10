package org.noostak.server.appointment.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

class OptionTest {

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @Test
        @DisplayName("연관 Appointment 초기화 성공")
        void shouldInitAppointmentSuccessfully() {
            // Given
            Option option = new Option();
            Appointment appointment = mock(Appointment.class);

            // When
            option.initAppointment(appointment);

            // Then
            assertThat(option.getAppointment()).isEqualTo(appointment);

        }

        @Test
        @DisplayName("연관 Appointment 해제 성공")
        void shouldClearAppointmentSuccessfully() {
            // Given
            Option option = new Option();
            Appointment appointment = mock(Appointment.class);
            option.initAppointment(appointment);

            // When
            option.clearAppointment();

            // Then
            assertThat(option.getAppointment()).isNull();
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @Test
        @DisplayName("이미 연관된 Appointment가 있는 경우 초기화 시도 실패")
        void shouldThrowExceptionWhenAlreadyLinkedToAppointment() {
            // Given
            Option option = new Option();
            Appointment appointment1 = mock(Appointment.class);
            Appointment appointment2 = mock(Appointment.class);
            option.initAppointment(appointment1);

            // When & Then
            assertThatThrownBy(() -> option.initAppointment(appointment2))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("[ERROR] 연관된 Appointment가 존재합니다.");
        }

        @Test
        @DisplayName("연관된 Appointment가 없는 경우 해제 시도 실패")
        void shouldThrowExceptionWhenClearingWithoutLinkedAppointment() {
            // Given
            Option option = new Option();

            // When & Then
            assertThatThrownBy(option::clearAppointment)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("[ERROR] 연관된 Appointment가 존재하지 않습니다.");
        }
    }
}
