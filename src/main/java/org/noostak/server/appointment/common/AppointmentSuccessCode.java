package org.noostak.server.appointment.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.server.global.success.handler.SuccessCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AppointmentSuccessCode implements SuccessCode {
    SUCCESS_CREATE_APPOINTMENT(HttpStatus.CREATED, "약속이 성공적으로 생성되었습니다."),

    SUCCESS_SAVE_AVAILABLE_TIME_SELECTION(HttpStatus.CREATED, "사용자의 가능한 시간이 저장되었습니다."),

    SUCCESS_GET_PROGRESS_APPOINTMENTS(HttpStatus.OK, "그룹 정보 및 약속 정보를 성공적으로 불러왔습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
