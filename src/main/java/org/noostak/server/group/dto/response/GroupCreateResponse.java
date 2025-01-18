package org.noostak.server.group.dto.response;

import lombok.Builder;
import org.noostak.server.group.domain.Group;

@Builder
public record GroupCreateResponse(
        Long groupId,
        String groupName,
        String groupImageUrl,
        String inviteCode
) {
    public static GroupCreateResponse of(final Group group) {
        return GroupCreateResponse.builder()
                .groupId(group.getGroupId())
                .groupName(group.getName().value())
                .groupImageUrl(group.getUrl().value())
                .inviteCode(group.getGroupInviteCode().value())
                .build();
    }
}
