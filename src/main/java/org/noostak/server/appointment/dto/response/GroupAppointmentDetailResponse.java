package org.noostak.server.appointment.dto.response;

public record GroupAppointmentDetailResponse(
        Long appointmentId,
        String appointmentName,
        Long appointmentMemberCount
) {
    public static GroupAppointmentDetailResponse of(
            Long appointmentId,
            String appointmentName,
            Long appointmentMemberCount
    ) {
        return new GroupAppointmentDetailResponse(appointmentId, appointmentName, appointmentMemberCount);
    }
}
