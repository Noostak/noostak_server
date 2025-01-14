package org.noostak.server.group.api;

import lombok.RequiredArgsConstructor;
import org.noostak.server.global.success.core.SuccessResponse;
import org.noostak.server.group.application.GroupService;
import org.noostak.server.group.dto.request.GroupCreateRequest;
import org.noostak.server.group.dto.response.GroupCreateResponse;
import org.noostak.server.group.dto.response.GroupsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.noostak.server.group.common.GroupSuccessCode.SUCCESS_CREATE_GROUP;
import static org.noostak.server.group.common.GroupSuccessCode.SUCCESS_GET_GROUP_LIST;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping
    public ResponseEntity<SuccessResponse<GroupsResponse>> getGroups() {
        GroupsResponse groups = groupService.findAllGroups();
        return ResponseEntity.ok(SuccessResponse.of(SUCCESS_GET_GROUP_LIST, groups));
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<SuccessResponse<GroupCreateResponse>> createGroup(
            // @AuthenticationPrincipal Long userId,
            @ModelAttribute GroupCreateRequest request
    ) {

        // TODO: AuthenticationPrincipal
        Long userId = 1L;
        GroupCreateResponse groupCreateResponse = groupService.createGroup(userId, request);
        return ResponseEntity.ok((SuccessResponse.of(SUCCESS_CREATE_GROUP, groupCreateResponse)));
    }
}
