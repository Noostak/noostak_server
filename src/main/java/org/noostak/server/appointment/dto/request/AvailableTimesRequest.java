package org.noostak.server.appointment.dto.request;

import org.noostak.server.appointment.domain.AppointmentMemberDateTime;

import java.util.List;

public record AvailableTimesRequest(
        List<AppointmentMemberDateTime> availableTimes
) {
    public static AvailableTimesRequest of(List<AppointmentMemberDateTime> availableTimes) {
        return new AvailableTimesRequest(availableTimes);
    }
}
