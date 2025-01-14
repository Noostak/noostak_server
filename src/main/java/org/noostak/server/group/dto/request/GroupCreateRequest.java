package org.noostak.server.group.dto.request;

public record GroupCreateRequest(
        String groupName,
        String groupImageUrl
) {
    public static GroupCreateRequest of(String groupName, String groupImageUrl) {
        return new GroupCreateRequest(groupName, groupImageUrl);
    }
}
