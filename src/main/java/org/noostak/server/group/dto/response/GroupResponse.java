package org.noostak.server.group.dto.response;

import lombok.Builder;
import org.noostak.server.group.domain.Group;

@Builder
public record GroupResponse(
        Long groupId,
        String groupName,
        Long groupMemberCount,
        String groupImageUrl,
        String inviteCode
) {
    public static GroupResponse of(final Group group){
        return GroupResponse.builder()
                .groupId(group.getGroupId())
                .groupName(group.getName().value())
                .groupMemberCount(group.getMemberCount().value())
                .groupImageUrl(group.getUrl().value())
                .inviteCode(group.getGroupInviteCode().value())
                .build();
    }
}
