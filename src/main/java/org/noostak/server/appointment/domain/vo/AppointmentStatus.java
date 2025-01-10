package org.noostak.server.appointment.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppointmentStatus {
    CONFIRMED("확정"),
    PROGRESS("진행중");

    private final String message;
}
