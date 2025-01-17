package org.noostak.server.group.dto.response;

import lombok.Builder;
import org.noostak.server.group.domain.Group;

@Builder
public record GroupCreateResponse(
        Long groupId,
        Long groupLeaderId,
        String groupName,
        String groupImageUrl,
        String inviteCode
) {
    public static GroupCreateResponse of(final Group group) {
        return GroupCreateResponse.builder()
                .groupId(group.getGroupId())
                .groupLeaderId(group.getGroupLeaderId())
                .groupName(group.getName().value())
                .groupImageUrl(group.getUrl().value())
                .inviteCode(group.getGroupInviteCode().value())
                .build();
    }
}
