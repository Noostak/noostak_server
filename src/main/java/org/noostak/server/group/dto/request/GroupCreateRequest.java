package org.noostak.server.group.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record GroupCreateRequest(
        String groupName,
        MultipartFile file
) {
    public static GroupCreateRequest of(String groupName, MultipartFile file) {
        return new GroupCreateRequest(groupName, file);
    }
}
