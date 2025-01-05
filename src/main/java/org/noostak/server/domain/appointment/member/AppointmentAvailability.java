package org.noostak.server.domain.appointment.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppointmentAvailability {
    AVAILABLE("약속 가능"),
    UNAVAILABLE("약속 불가능");

    private final String message;
}

