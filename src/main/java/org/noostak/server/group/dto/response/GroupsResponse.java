package org.noostak.server.group.dto.response;

import java.util.List;

public record GroupsResponse(
        List<GroupResponse> groups
) {
    public static GroupsResponse of(final List<GroupResponse> groups) {
        return new GroupsResponse(groups);
    }
}
