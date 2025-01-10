package org.noostak.server.appointment.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class AppointmentName {

    private final String appointmentName;

    protected AppointmentName() {
        this.appointmentName = null;
    }

    private AppointmentName(String appointmentName) {
        validateAppointmentName(appointmentName);
        this.appointmentName = appointmentName;
    }

    public static AppointmentName from(String appointmentName) {
        return new AppointmentName(appointmentName);
    }

    public String value() {
        return appointmentName;
    }

    private void validateAppointmentName(String appointmentName) {
        validateEmpty(appointmentName);
        validateLength(appointmentName);
    }

    private void validateEmpty(String appointmentName) {
        if (appointmentName == null || appointmentName.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 약속 이름은 비어 있을 수 없습니다.");
        }
    }

    private void validateLength(String appointmentName) {
        if (appointmentName.length() > 30) {
            throw new IllegalArgumentException("[ERROR] 약속 이름의 길이는 30글자를 넘을 수 없습니다.");
        }
    }

    @Override
    public String toString() {
        return appointmentName;
    }
}
