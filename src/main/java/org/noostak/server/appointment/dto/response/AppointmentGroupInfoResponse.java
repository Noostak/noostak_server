package org.noostak.server.appointment.dto.response;

public record AppointmentGroupInfoResponse(
        String groupName,
        String groupImageUrl,
        Long groupMemberCount,
        String inviteCode
) {
    public static AppointmentGroupInfoResponse of(
            String groupName,
            String groupImageUrl,
            Long groupMemberCount,
            String inviteCode
    ) {
        return new AppointmentGroupInfoResponse(groupName, groupImageUrl, groupMemberCount, inviteCode);
    }
}
