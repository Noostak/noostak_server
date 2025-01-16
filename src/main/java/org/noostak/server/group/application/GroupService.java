package org.noostak.server.group.application;

import lombok.RequiredArgsConstructor;
import org.noostak.server.group.common.GroupErrorCode;
import org.noostak.server.group.common.GroupException;
import org.noostak.server.group.domain.Group;
import org.noostak.server.group.domain.GroupInviteCode;
import org.noostak.server.group.domain.GroupRepository;
import org.noostak.server.group.domain.vo.GroupImageUrl;
import org.noostak.server.group.domain.vo.GroupName;
import org.noostak.server.group.dto.request.GroupCreateRequest;
import org.noostak.server.group.dto.response.GroupCreateResponse;
import org.noostak.server.group.dto.response.GroupResponse;
import org.noostak.server.group.dto.response.GroupsResponse;
import org.noostak.server.member.domain.Member;
import org.noostak.server.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final InviteCodeGenerator inviteCodeGenerator;

    public GroupsResponse findAllGroups() {
        List<GroupResponse> groups = groupRepository.findAll().stream()
                .map(GroupResponse::of)
                .toList();
        return GroupsResponse.of(groups);
    }

    @Transactional
    public GroupCreateResponse createGroup(Long userId, GroupCreateRequest request) {
        Member groupLeader = memberRepository.findById(userId)
                .orElseThrow(() -> new GroupException(GroupErrorCode.LEADER_NOT_FOUND));

        GroupInviteCode inviteCode = inviteCodeGenerator.generate();

        // TODO: S3에 이미지 업로드 후 URL 받아오기
        String tempGroupImageUrl = "https://noostak.s3.ap-northeast-2.amazonaws.com/group-images/1.jpg";
        String groupName = request.groupName();

        Group group = Group.of(
                groupLeader.getMemberId(),
                GroupName.from(groupName),
                GroupImageUrl.from(tempGroupImageUrl),
                inviteCode.value()
        );

        groupRepository.save(group);

        return GroupCreateResponse.of(group);
    }
}
