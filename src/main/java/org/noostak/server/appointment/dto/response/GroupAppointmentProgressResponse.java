package org.noostak.server.appointment.dto.response;

import java.util.List;

public record GroupAppointmentProgressResponse(
        AppointmentGroupInfoResponse groupInfo,
        List<GroupAppointmentDetailResponse> appointmentDetails
) {
    public static GroupAppointmentProgressResponse of(
            AppointmentGroupInfoResponse groupInfo,
            List<GroupAppointmentDetailResponse> appointmentDetails
    ) {
        return new GroupAppointmentProgressResponse(groupInfo, appointmentDetails);
    }
}
