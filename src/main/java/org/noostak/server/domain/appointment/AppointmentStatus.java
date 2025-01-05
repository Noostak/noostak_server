package org.noostak.server.domain.appointment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppointmentStatus {
    SCHEDULE("일정"),
    MEETING("회의"),
    SOPT("SOPT"),
    OTHER("기타");

    private final String message;
}
