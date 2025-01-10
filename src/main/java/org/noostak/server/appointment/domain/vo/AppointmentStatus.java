package org.noostak.server.appointment.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppointmentStatus {
    IMPORTANT("중요"),
    SCHEDULE("일정"),
    HOBBY("취미"),
    OTHER("기타");

    private final String message;
}
